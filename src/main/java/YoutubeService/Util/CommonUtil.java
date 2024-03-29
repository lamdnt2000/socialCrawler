package YoutubeService.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static ObjectMapper mapper = new ObjectMapper();


    public static String convertObjectToJson(Object object) throws IllegalAccessException, JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.writeValueAsString(object).replaceAll("\\\\", "").replaceAll("\"\\{", "\\{").replaceAll("\\}\"", "\\}");

    }


    public static long parseStringToLong(String text) {
        Pattern pattern = Pattern.compile("(\\d+,)?(\\d+,)?(\\d+,)?(\\d+,)?\\d+");
        Matcher matcher = pattern.matcher(text);
        int multiple = 1;
        if (text.toLowerCase().endsWith("k")||text.toLowerCase().endsWith("N")) {
            multiple = 1000;
        }
        if (text.toLowerCase().endsWith("M")) {
            multiple = 1000000;
        }
        if (matcher.find()) {
            return Long.parseLong(matcher.group().replaceAll(",", "")) * multiple;
        } else {
            return 0;
        }
    }

    public static String parseUrlGetVideoId(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }


}
