package TikTok;

import Browser.Browser;
import TikTok.Model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.io.FileWriter;
import java.io.IOException;

import static TikTok.TikTokAPI.*;

public class Tiktok {
    public static void main(String[] args) throws IOException {
        Browser browser = new Browser("");
        WebDriver driver = browser.initBrowser(true);
        try {
            Profile user = getUserFeed("podaynhe", driver, true);
            ObjectMapper obj = new ObjectMapper();
            obj.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            FileWriter file = new FileWriter("1.json");
            file.write(obj.writeValueAsString(user));
            file.close();

        }
        catch (TimeoutException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
