package Browser;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Browser {
    private String proxy;
    private String userAgent;
    public Browser(String proxy){
        this.proxy = proxy;
    }

    public ChromeOptions optionsSetting(boolean isHeadless, boolean isMobile){
        ChromeOptions chromeOptions = new ChromeOptions();
        if (proxy!=null && !proxy.isEmpty()){
            chromeOptions.addArguments("--proxy-server=" + proxy);
        }
        if (isHeadless){
            chromeOptions.addArguments("--headless");
        }
        this.userAgent = RandomUserAgent.getRandomUserAgent();
        chromeOptions.addArguments("--user-agent="+this.userAgent);
        chromeOptions.addArguments("disable-infobars");
        if (isMobile){
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "Galaxy S9+");
            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        }
        return chromeOptions;
    }

    public WebDriver initBrowser(boolean isMobile, boolean isHeaddless){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions chromeOptions = optionsSetting(isHeaddless, isMobile);
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().deleteAllCookies();
        driver.manage().window().setSize(new Dimension(280,840));
        driver.manage().timeouts().implicitlyWait(20000, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        return driver;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
