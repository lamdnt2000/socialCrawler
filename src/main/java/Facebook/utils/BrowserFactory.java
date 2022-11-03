package Facebook.utils;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.Set;

public class BrowserFactory {
    public static Set<Cookie> getCookiesFromBrowser(WebDriver driver) {
        return driver.manage().getCookies();
    }


    public static void setCookiesBrowser(String cookieStr, WebDriver driver) {
        Set<Cookie> cookies = parseCookies(cookieStr);
        for (Cookie cookie: cookies){
            driver.manage().addCookie(cookie);
        }
    }

    public static Set<Cookie> parseCookies(String cookieStr) {
        String[] splitCookies = cookieStr.split(";");
        Set<Cookie> cookies = new HashSet<>();
        for (int i=0;i<splitCookies.length;i++){
            String[] attrs = splitCookies[i].split("=");
            Cookie cookie = new Cookie(attrs[0], attrs[1]);
            cookies.add(cookie);
        }
        return  cookies;
    }


}
