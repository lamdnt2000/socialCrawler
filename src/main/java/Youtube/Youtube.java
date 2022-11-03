package Youtube;

import Browser.Browser;
import Youtube.Model.User;
import Youtube.Model.Video;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static Youtube.VideoAPI.getVideoDetails;
import static Youtube.VideoAPI.getVideoIds;

public class Youtube {
    public static void main(String[] args) throws IOException {
        Browser browser = new Browser("");
        WebDriver driver = browser.initBrowser(false);
        try {
            User user = getVideoIds("UC4L6cAm9LPirrd6Va-8NheQ",driver,browser);
            List<Video> videos = getVideoDetails(driver, browser);
            user.setVideos(videos);
            ObjectMapper obj = new ObjectMapper();
            obj.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            FileWriter file = new FileWriter("youtube\\"+user.getChannelId()+".json");
            file.write(obj.writeValueAsString(user));
            file.close();
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
