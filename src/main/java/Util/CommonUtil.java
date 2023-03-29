package Util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    public static long parseStringToLong(String text) {
        Pattern pattern = Pattern.compile("\\d+\\.\\d+| \\d+ |(\\d{1,3},)?(\\d{1,3},)?(\\d{1,3},)?(\\d{1,3},)?\\d{1,3}");
        String subcribe = text.split(" ")[0];
        Matcher matcher = pattern.matcher(subcribe);
        int multiple = 1;
        if (subcribe.toLowerCase().endsWith("k")||text.toLowerCase().endsWith("n")) {
            multiple = 1000;
        }
        if (subcribe.toLowerCase().endsWith("m")) {
            multiple = 1000000;
        }
        if (matcher.find()) {
            float test = Float.parseFloat(matcher.group().replaceAll(",", ""));
            return (new Double(test* multiple)).longValue();
        } else {
            return 0;
        }
    }

    public static String parseUrlToBase64(String src) {
        try {
            URL imageUrl = new URL(src);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
