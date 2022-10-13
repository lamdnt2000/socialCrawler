package Browser;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;

public class BrwoserFactory implements BrowserFactoryImpl{
    @Override
    public Set<Cookie> getCookiesFromBrowser(WebDriver driver) {
        return driver.manage().getCookies();
    }

    @Override
    public void setCookiesBrowser(Set<Cookie> cookies, WebDriver driver) {
        for (Cookie cookie: cookies){
            driver.manage().addCookie(cookie);
        }
    }
}
