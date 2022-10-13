package Youtube;

import Browser.Browser;
import Youtube.Model.User;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

import static Youtube.VideoAPI.getVideoIds;

public class Youtube {
    public static void main(String[] args) throws IOException {
        Browser browser = new Browser("");
        WebDriver driver = browser.initBrowser(false);
        try {

            User user = getVideoIds("UC4L6cAm9LPirrd6Va-8NheQ",driver,browser);

        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
