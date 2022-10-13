package Browser;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;

public interface BrowserFactoryImpl {
    Set<Cookie> getCookiesFromBrowser(WebDriver driver);
    void setCookiesBrowser(Set<Cookie> cookies, WebDriver driver);
}
