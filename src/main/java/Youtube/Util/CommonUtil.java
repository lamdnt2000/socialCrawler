package Youtube.Util;

import Youtube.Header.VideoHeader;
import Youtube.Model.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.InvalidArgumentException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static ObjectMapper mapper = new ObjectMapper();


    public static String convertObjectToJson(Object object) throws IllegalAccessException, JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.writeValueAsString(object).replaceAll("\\\\","").replaceAll("\"\\{","\\{").replaceAll("\\}\"","\\}");

    }


    public static long parseStringToLong(String text){
        Pattern pattern = Pattern.compile("(\\d{1,3},)?(\\d{1,3},)?(\\d{1,3},)?(\\d{1,3},)?\\d{1,3}");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()){
            return Long.parseLong(matcher.group().replaceAll(",",""));
        }
        else{
            return 0;
        }
    }



}
