package TikTok;

import Browser.Browser;
import TikTok.Model.*;
import TikTok.Thread.MultiBroswer;
import Util.FileUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        System.out.println("init main browser");
        ExecutorService es = Executors.newFixedThreadPool(3);
        List<Future<WebDriver>> futures = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            futures.add(es.submit(new MultiBroswer()));
        }
        for (int i = 0; i < 3; i++) {
            drivers.add(futures.get(i).get());
        }
        System.out.println("init fetch browser");
        try {
            String id = "duongbaolam";
            Profile profile = getUserFeed(id, driver);
            ObjectMapper obj = new ObjectMapper();
            FileUtils.saveData(PATH + "\\", id + ".json", profile);
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
