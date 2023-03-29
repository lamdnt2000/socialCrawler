package Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class RequestUtil {
    public static synchronized int bypassCaptcha(String inner, String outer) throws IOException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("type_job_id", "23");
        data.put("image_base64", inner + "|" + outer);
        jsonObject.put("api_token", "ShlSa9UxX8GV3VowypZcBsVsx00i5PU0JVqXnJusAXbL50gOn65GdN7Bg9IBbMgLZ0Bnmdeux6UtHYbt");
        jsonObject.put("data", data);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder()
                .url("https://omocaptcha.com/api/createJob")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(Objects.requireNonNull(response.body()).string(), Map.class);
        int result = 0;
        int jobID = (int) map.get("job_id");
        do {
            result = getResultCaptcha(jobID);
            Thread.sleep(3500);
        }
        while (result == 0);
        return result;
    }

    public static synchronized int getResultCaptcha(int jobID) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("api_token", "ShlSa9UxX8GV3VowypZcBsVsx00i5PU0JVqXnJusAXbL50gOn65GdN7Bg9IBbMgLZ0Bnmdeux6UtHYbt");
        jsonObject.put("job_id", jobID);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder()
                .url("https://omocaptcha.com/api/getJobResult")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(Objects.requireNonNull(response.body()).string(), Map.class);
        if (((String) map.get("status")).equals("success")) {
            return Integer.parseInt((String) map.get("result"));
        }
        return 0;

    }



}
