package TikTok;

import Browser.Browser;
import Facebook.utils.FileUtil;
import TikTok.Model.*;
import TikTok.Thread.MultiBroswer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static TikTok.TikTokAPI.*;

public class Tiktok {
    public static String PATH = "C:\\tool\\FacebookAPI\\users";
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Browser browser = new Browser("127.0.0.1:10000");
        WebDriver driver = browser.initBrowser(true);
        ExecutorService es = Executors.newFixedThreadPool(3);
        List<Future<WebDriver>> futures = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            futures.add(es.submit(new MultiBroswer()));
        }
        for (int i=0;i<3;i++){
            drivers.add(futures.get(i).get());
        }
        try {
            List<String> ids = FileUtil.readUsers();
            for (String id:ids){
                Profile profile = getUserFeed(id,driver,true);
                ObjectMapper obj = new ObjectMapper();
                obj.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                FileWriter file = new FileWriter(PATH+"\\"+id+".json");
                file.write(obj.writeValueAsString(profile));
                file.close();
            }

            es.shutdown();
        }
        catch (TimeoutException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
