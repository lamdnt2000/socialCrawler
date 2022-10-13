package TikTok;

import org.openqa.selenium.Cookie;

import java.lang.reflect.Field;
import java.util.*;

public class CommonUtil {

    private static Map<String, String> data;

    public static String generateDeviceId(){
        String[] nums = new String[19];
        Random rand = new Random();
        nums[0]= String.valueOf((rand.nextInt(8)) + 1);
        for (int i=1;i<19;i++){
            nums[i] = String.valueOf(rand.nextInt(9));
        }
        return String.join("",nums);
    }

    public static String convertObjectToPara(Object object) throws IllegalAccessException {
        ArrayList<String> fields = new ArrayList<>();

        if (object.getClass().getSuperclass()!=null){
            for (Field f:object.getClass().getSuperclass().getDeclaredFields()){
                f.setAccessible(true);
                String key = f.getName();
                Object value = (f.get(object));
                fields.add(key+"="+value);
            }
        }
        for (Field f:object.getClass().getDeclaredFields()){
            f.setAccessible(true);
            String key = f.getName();
            Object value = (f.get(object));
            fields.add(key+"="+value);
        }
        return String.join("&",fields);
    }
    public static List<String> convertCookiesToList(Set<Cookie> cookies){
        List<String> result = new ArrayList<>();
        int i=0;
        for (Cookie cookie: cookies){
            String name = cookie.getName();
            if (name.equals("_abck")||name.equals("msToken")||name.equals("bm_sz")||name.equals("ttwid")){
                result.add(cookie.getName()+"="+cookie.getValue());
            }


        }
        return  result;
    }


}
