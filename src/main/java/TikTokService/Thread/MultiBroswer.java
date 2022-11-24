package TikTokService.Thread;

import Browser.Browser;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.Callable;

public class MultiBroswer implements Callable<WebDriver> {

    @Override
    public WebDriver call() {
        Browser browser = new Browser("");
        WebDriver driver = browser.initBrowser(true, true);
        driver.get("https://tiktok.com");

        return driver;

    }


}
