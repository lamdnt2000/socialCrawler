package TikTok.Thread;

import Browser.Browser;
import org.openqa.selenium.WebDriver;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MultiBroswer implements Callable<WebDriver> {

    @Override
    public WebDriver call() {
        Browser browser = new Browser("");
        WebDriver driver = browser.initBrowser(true);
        driver.get("https://tiktok.com");

        return driver;

    }


}
