package YoutubeService;

import YoutubeService.Header.BaseHeader;
import YoutubeService.Header.VideoDetailHeader;
import YoutubeService.Model.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static YoutubeService.Constants.*;
import static YoutubeService.Util.CommonUtil.convertObjectToJson;

public class YoutubeRequest<T extends BaseHeader> {
    private T requestHeader;

    public YoutubeRequest(T requestHeader) {
        this.requestHeader = requestHeader;
    }

    public synchronized Object excuse(WebDriver driver, String apiPath) throws IllegalAccessException, JsonProcessingException {
        String param = convertObjectToJson(requestHeader);
        String js = String.format(
                "var data = await fetch(\"%s\", {\n" +
                        "            \"headers\": {\n" +
                        "                \"accept\": \"*/*\",\n" +
                        "                \"Content-Type\": \"application/json\",\n" +
                        "            },\n" +
                        "            \"body\": '%s',\n" +
                        "            \"method\": \"POST\",\n" +
                        "            \"redirect\": \"follow\"\n" +
                        "            }).then(response => response.json())"+
                        "    .catch(error => console.warn(error));" +
                        "return data;",apiPath,param);
        return ((JavascriptExecutor)driver).executeScript(js);
    }

    public static List<String> bulkVideoRequestDetail(List<String> ids, String userAgent) throws IllegalAccessException, JsonProcessingException {

        int count = 0;
        List<String> params = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (int i=0;i<ids.size();i++){

            VideoDetailHeader head = new VideoDetailHeader(userAgent, ids.get(i));
            String param = convertObjectToJson(head);
            String js = String.format(
                    "fetch(\"%s\", {\n" +
                            "            \"headers\": {\n" +
                            "                \"accept\": \"*/*\",\n" +
                            "                \"Content-Type\": \"application/json\",\n" +
                            "            },\n" +
                            "            \"referrerPolicy\": \"strict-origin-when-cross-origin\",\n" +
                            "            \"body\": '%s',\n" +
                            "            \"method\": \"POST\",\n" +
                            "            \"mode\": \"cors\",\n" +
                            "            \"credentials\": \"include\"\n" +
                            "            }).then(response => response.json())" +
                            "    .catch(error => console.warn(error))", URL_ITEM_DETAILS, param);
            list.add(js);

            if ((count++)==99|| i==ids.size()-1){
                params.add("var data = await Promise.all([" + String.join(",", list) + "]);return data");
                count = 0;
                list = new ArrayList<>();
            }
        }

        return params ;

    }

    public static List<String> bulkVideoRequestRection(List<Video> videos, String userAgent) throws IllegalAccessException, JsonProcessingException {

        int count = 0;
        List<String> params = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (int i=0;i<videos.size();i++){

            VideoDetailHeader head = new VideoDetailHeader(userAgent, videos.get(i).getVideoId());
            String param = convertObjectToJson(head);
            String js = String.format(
                    "fetch(\"%s\", {\n" +
                            "            \"headers\": {\n" +
                            "                \"accept\": \"*/*\",\n" +
                            "                \"Content-Type\": \"application/json\",\n" +
                            "            },\n" +
                            "            \"referrerPolicy\": \"strict-origin-when-cross-origin\",\n" +
                            "            \"body\": '%s',\n" +
                            "            \"method\": \"POST\",\n" +
                            "            \"mode\": \"cors\",\n" +
                            "            \"credentials\": \"include\"\n" +
                            "            }).then(response => response.json())" +
                            "    .catch(error => console.warn(error))", URL_ITEM_REACTIONS, param);
            list.add(js);

            if ((count++)==99|| i==videos.size()-1){
                params.add("var data = await Promise.all([" + String.join(",", list) + "]);return data");
                count = 0;
                list = new ArrayList<>();
            }
        }

        return params ;

    }


    public static Object excuseMultiFetch(String js, WebDriver driver) throws IllegalAccessException {
        Object object = null;
        try {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } catch (ScriptTimeoutException | JavascriptException e) {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } finally {
            return object;
        }
    }
}
