package Facebook;

import com.restfb.*;
import com.restfb.util.StringUtils;
import com.restfb.util.UrlUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;

import static com.restfb.logging.RestFBLogger.HTTP_LOGGER;

public class ModWebRequestor implements WebRequestor {
    private static final String MULTIPART_BOUNDARY = "**boundarystringwhichwill**neverbeencounteredinthewild**";

    /**
     * Line separator for multipart {@code POST}s.
     */
    private static final String MULTIPART_CARRIAGE_RETURN_AND_NEWLINE = "\r\n";

    /**
     * Hyphens for multipart {@code POST}s.
     */
    private static final String MULTIPART_TWO_HYPHENS = "--";

    /**
     * Default buffer size for multipart {@code POST}s.
     */
    private static final int MULTIPART_DEFAULT_BUFFER_SIZE = 8192;

    /**
     * By default, how long should we wait for a response (in ms)?
     */
    private static final int DEFAULT_READ_TIMEOUT_IN_MS = 180000;

    private Map<String, List<String>> currentHeaders;

    private DebugHeaderInfo debugHeaderInfo;
    private String cookies;
    /**
     * By default this is true, to prevent breaking existing usage
     */
    private boolean autocloseBinaryAttachmentStream = true;

    protected enum HttpMethod {
        GET, DELETE, POST
    }

    public ModWebRequestor(String cookies) {
        this.cookies = cookies;
    }

    @Override
    public Response executeGet(Request request) throws IOException {
        return execute(ModWebRequestor.HttpMethod.GET, request);
    }

    @Override
    public Response executePost(Request request) throws IOException {

        List<BinaryAttachment> binaryAttachments = request.getBinaryAttachments();

        if (HTTP_LOGGER.isDebugEnabled()) {
            HTTP_LOGGER.debug("Executing a POST to " + request.getUrl() + " with parameters "
                    + (!binaryAttachments.isEmpty() ? "" : "(sent in request body): ")
                    + UrlUtils.urlDecode(request.getParameters())
                    + (!binaryAttachments.isEmpty() ? " and " + binaryAttachments.size() + " binary attachment[s]." : ""));
        }

        HttpURLConnection httpUrlConnection = null;
        OutputStream outputStream = null;

        try {
            httpUrlConnection = openConnection(
                    new URL(request.getUrl() + (!binaryAttachments.isEmpty() ? "?" + request.getParameters() : "")));
            httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);

            // Allow subclasses to customize the connection if they'd like to - set
            // their own headers, timeouts, etc.
            customizeConnection(httpUrlConnection);

            httpUrlConnection.setRequestMethod(HttpMethod.POST.name());
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);

            initHeaderAccessToken(httpUrlConnection, request);

            if (!binaryAttachments.isEmpty()) {
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + MULTIPART_BOUNDARY);
            }

            if (request.hasBody()) {
                httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            }

            httpUrlConnection.connect();
            outputStream = httpUrlConnection.getOutputStream();

            // If we have binary attachments, the body is just the attachments and the
            // other parameters are passed in via the URL.
            // Otherwise the body is the URL parameter string.
            if (!binaryAttachments.isEmpty()) {
                for (BinaryAttachment binaryAttachment : binaryAttachments) {
                    StringBuilder formData = createBinaryAttachmentFormData(binaryAttachment);

                    outputStream.write(formData.toString().getBytes(StringUtils.ENCODING_CHARSET));

                    write(binaryAttachment.getData(), outputStream, MULTIPART_DEFAULT_BUFFER_SIZE);

                    outputStream.write((MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + MULTIPART_TWO_HYPHENS + MULTIPART_BOUNDARY
                            + MULTIPART_TWO_HYPHENS + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).getBytes(StringUtils.ENCODING_CHARSET));
                }
            } else {
                if (request.hasBody()) {
                    outputStream.write(request.getBody().getData().getBytes(StringUtils.ENCODING_CHARSET));
                } else {
                    outputStream.write(request.getParameters().getBytes(StringUtils.ENCODING_CHARSET));
                }
            }

            HTTP_LOGGER.debug("Response headers: {}", httpUrlConnection.getHeaderFields());

            fillHeaderAndDebugInfo(httpUrlConnection);

            Response response = fetchResponse(httpUrlConnection);

            HTTP_LOGGER.debug("Facebook responded with {}", response);
            return response;
        } finally {
            closeAttachmentsOnAutoClose(binaryAttachments);
            closeQuietly(outputStream);
            closeQuietly(httpUrlConnection);
        }
    }

    private StringBuilder createBinaryAttachmentFormData(BinaryAttachment binaryAttachment) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MULTIPART_TWO_HYPHENS).append(MULTIPART_BOUNDARY)
                .append(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).append("Content-Disposition: form-data; name=\"")
                .append(createFormFieldName(binaryAttachment)).append("\"; filename=\"")
                .append(binaryAttachment.getFilename()).append("\"");

        stringBuilder.append(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).append("Content-Type: ")
                .append(binaryAttachment.getContentType());

        stringBuilder.append(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).append(MULTIPART_CARRIAGE_RETURN_AND_NEWLINE);
        return stringBuilder;
    }

    private void closeAttachmentsOnAutoClose(List<BinaryAttachment> binaryAttachments) {
        if (autocloseBinaryAttachmentStream && !binaryAttachments.isEmpty()) {
            binaryAttachments.stream().map(BinaryAttachment::getData).forEach(this::closeQuietly);
        }
    }

    protected void initHeaderAccessToken(HttpURLConnection httpUrlConnection, Request request) {
        if (request.hasHeaderAccessToken()) {
            httpUrlConnection.setRequestProperty("Authorization", "Bearer " + request.getHeaderAccessToken());
        }
    }

    /**
     * Given a {@code url}, opens and returns a connection to it.
     * <p>
     * If you'd like to pipe your connection through a proxy, this is the place to do so.
     *
     * @param url
     *          The URL to connect to.
     * @return A connection to the URL.
     * @throws IOException
     *           If an error occurs while establishing the connection.
     * @since 1.6.3
     */
    protected HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    /**
     * Hook method which allows subclasses to easily customize the {@code connection}s created by
     * {@link #executeGet(com.restfb.WebRequestor.Request)} and {@link #executePost(com.restfb.WebRequestor.Request)} -
     * for example, setting a custom read timeout or request header.
     * <p>
     * This implementation is a no-op.
     *
     * @param connection
     *          The connection to customize.
     */
    protected void customizeConnection(HttpURLConnection connection) {
        // This implementation is a no-op
    }

    /**
     * Attempts to cleanly close a resource, swallowing any exceptions that might occur since there's no way to recover
     * anyway.
     * <p>
     * It's OK to pass {@code null} in, this method will no-op in that case.
     *
     * @param closeable
     *          The resource to close.
     */
    protected void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception t) {
                HTTP_LOGGER.warn("Unable to close {}: ", closeable, t);
            }
        }
    }

    /**
     * Attempts to cleanly close an {@code HttpURLConnection}, swallowing any exceptions that might occur since there's no
     * way to recover anyway.
     * <p>
     * It's OK to pass {@code null} in, this method will no-op in that case.
     *
     * @param httpUrlConnection
     *          The connection to close.
     */
    protected void closeQuietly(HttpURLConnection httpUrlConnection) {
        try {
            Optional.ofNullable(httpUrlConnection).ifPresent(HttpURLConnection::disconnect);
        } catch (Exception t) {
            HTTP_LOGGER.warn("Unable to disconnect {}: ", httpUrlConnection, t);
        }
    }

    /**
     * Writes the contents of the {@code source} stream to the {@code destination} stream using the given
     * {@code bufferSize}.
     *
     * @param source
     *          The source stream to copy from.
     * @param destination
     *          The destination stream to copy to.
     * @param bufferSize
     *          The size of the buffer to use during the copy operation.
     * @throws IOException
     *           If an error occurs when reading from {@code source} or writing to {@code destination}.
     * @throws NullPointerException
     *           If either {@code source} or @{code destination} is {@code null}.
     */
    protected void write(InputStream source, OutputStream destination, int bufferSize) throws IOException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Must provide non-null source and destination streams.");
        }

        int read;
        byte[] chunk = new byte[bufferSize];
        while ((read = source.read(chunk)) > 0)
            destination.write(chunk, 0, read);
    }

    /**
     * Creates the form field name for the binary attachment filename by stripping off the file extension - for example,
     * the filename "test.png" would return "test".
     *
     * @param binaryAttachment
     *          The binary attachment for which to create the form field name.
     * @return The form field name for the given binary attachment.
     */
    protected String createFormFieldName(BinaryAttachment binaryAttachment) {

        if (binaryAttachment.getFieldName() != null) {
            return binaryAttachment.getFieldName();
        }

        String name = binaryAttachment.getFilename();
        return Optional.ofNullable(name).filter(f -> f.contains(".")).map(f -> f.substring(0, f.lastIndexOf('.')))
                .orElse(name);
    }

    /**
     * returns if the binary attachment stream is closed automatically
     *
     * @since 1.7.0
     * @return {@code true} if the binary stream should be closed automatically, {@code false} otherwise
     */
    public boolean isAutocloseBinaryAttachmentStream() {
        return autocloseBinaryAttachmentStream;
    }

    /**
     * define if the binary attachment stream is closed automatically after sending the content to facebook
     *
     * @since 1.7.0
     * @param autocloseBinaryAttachmentStream
     *          {@code true} if the {@link BinaryAttachment} stream should be closed automatically, {@code false}
     *          otherwise
     */
    public void setAutocloseBinaryAttachmentStream(boolean autocloseBinaryAttachmentStream) {
        this.autocloseBinaryAttachmentStream = autocloseBinaryAttachmentStream;
    }

    /**
     * access to the current response headers
     *
     * @return the current reponse header map
     */
    public Map<String, List<String>> getCurrentHeaders() {
        return currentHeaders;
    }

    @Override
    public Response executeDelete(Request request) throws IOException {
        return execute(HttpMethod.DELETE, request);
    }

    @Override
    public DebugHeaderInfo getDebugHeaderInfo() {
        return debugHeaderInfo;
    }

    private Response execute(HttpMethod httpMethod, Request request) throws IOException {
        HTTP_LOGGER.debug("Making a {} request to {} with parameters {}", httpMethod.name(), request.getUrl(),
                request.getParameters());

        HttpURLConnection httpUrlConnection = null;

        try {
            httpUrlConnection = openConnection(new URL(request.getFullUrl()));
            httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod(httpMethod.name());
            if (cookies!=null || !cookies.isEmpty()){
                httpUrlConnection.setRequestProperty("Cookie",cookies);
            }
            initHeaderAccessToken(httpUrlConnection, request);

            // Allow subclasses to customize the connection if they'd like to - set
            // their own headers, timeouts, etc.
            customizeConnection(httpUrlConnection);

            httpUrlConnection.connect();

            HTTP_LOGGER.trace("Response headers: {}", httpUrlConnection.getHeaderFields());

            fillHeaderAndDebugInfo(httpUrlConnection);

            Response response = fetchResponse(httpUrlConnection);

            HTTP_LOGGER.debug("Facebook responded with {}", response);
            return response;
        } finally {
            closeQuietly(httpUrlConnection);
        }
    }

    protected void fillHeaderAndDebugInfo(HttpURLConnection httpUrlConnection) {
        currentHeaders = Collections.unmodifiableMap(httpUrlConnection.getHeaderFields());

        String usedApiVersion = StringUtils.trimToEmpty(httpUrlConnection.getHeaderField("facebook-api-version"));
        HTTP_LOGGER.debug("Facebook used the API {} to answer your request", usedApiVersion);

        Version usedVersion = Version.getVersionFromString(usedApiVersion);
        DebugHeaderInfo.DebugHeaderInfoFactory factory =
                DebugHeaderInfo.DebugHeaderInfoFactory.create().setVersion(usedVersion);

        Arrays.stream(FbHeaderField.values()).forEach(f -> f.getPutHeader().accept(httpUrlConnection, factory));
        debugHeaderInfo = factory.build();
    }

    protected Response fetchResponse(HttpURLConnection httpUrlConnection) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getInputStreamFromUrlConnection(httpUrlConnection);
        } catch (IOException e) {
            HTTP_LOGGER.warn("An error occurred while making a {} request to {}:", httpUrlConnection.getRequestMethod(),
                    httpUrlConnection.getURL(), e);
        }

        return new Response(httpUrlConnection.getResponseCode(), StringUtils.fromInputStream(inputStream));
    }

    private InputStream getInputStreamFromUrlConnection(HttpURLConnection httpUrlConnection) throws IOException {
        return httpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK ? httpUrlConnection.getErrorStream()
                : httpUrlConnection.getInputStream();
    }

    private enum FbHeaderField {
        X_FB_TRACE_ID((c, f) -> f.setTraceId(getHeaderOrEmpty(c, "x-fb-trace-id"))), //
        X_FB_REV((c, f) -> f.setRev(getHeaderOrEmpty(c, "x-fb-rev"))), //
        X_FB_DEBUG((c, f) -> f.setDebug(getHeaderOrEmpty(c, "x-fb-debug"))), //
        X_APP_USAGE((c, f) -> f.setAppUsage(getHeaderOrEmpty(c, "x-app-usage"))), //
        X_PAGE_USAGE((c, f) -> f.setPageUsage(getHeaderOrEmpty(c, "x-page-usage"))), //
        X_AD_ACCOUNT_USAGE((c, f) -> f.setAdAccountUsage(getHeaderOrEmpty(c, "x-ad-account-usage"))), //
        X_BUSINESS_USE_CASE_USAGE((c, f) -> f.setBusinessUseCaseUsage(getHeaderOrEmpty(c, "x-business-use-case-usage")));

        private final BiConsumer<HttpURLConnection, DebugHeaderInfo.DebugHeaderInfoFactory> putHeader;

        FbHeaderField(BiConsumer<HttpURLConnection, DebugHeaderInfo.DebugHeaderInfoFactory> headerFunction) {
            this.putHeader = headerFunction;
        }

        public BiConsumer<HttpURLConnection, DebugHeaderInfo.DebugHeaderInfoFactory> getPutHeader() {
            return putHeader;
        }

        private static String getHeaderOrEmpty(HttpURLConnection connection, String fieldName) {
            return StringUtils.trimToEmpty(connection.getHeaderField(fieldName));
        }
    }
}
