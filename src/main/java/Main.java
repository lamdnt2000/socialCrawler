import Browser.Browser;
import FacebookService.Model.ModPage;
import FacebookService.Services.FacebookService;
import TikTokService.Model.Profile;
import Util.FileUtils;
import YoutubeService.Model.User;
import YoutubeService.Model.Video;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static TikTokService.TikTokAPI.getUserFeed;
import static Util.FileUtils.saveData;
import static YoutubeService.VideoAPI.getVideoDetails;
import static YoutubeService.VideoAPI.getVideoIds;

public class Main {
    public static void main(String[] args) {
        String socialType = args[0];
        String uid = args[1];
        long start = System.currentTimeMillis();
        /*String socialType = "T";
        String uid = "nguyn69_";*/
        Browser browser;
        WebDriver driver = null;
        String resultScreen = "Success";
        String path;
        try {
            switch (socialType) {
                case "F":
                    path = "facebook";
                    String cookie = FileUtils.readToken("cookie.txt");
                    String token = FileUtils.readToken("token.txt");
                    FacebookService service = new FacebookService(cookie, token);
                    ModPage result = service.fetchPostInPage(uid);
                    saveData(path, uid, result);
                    break;
                case "Y":
                    path = "youtube";
                    browser = new Browser("");
                    driver = browser.initBrowser(false, true);
                    User user = getVideoIds(uid, driver, browser);
                    List<Video> videos = getVideoDetails(driver, browser);
                    user.setVideos(videos);
                    FileUtils.saveData(path, uid, user);
                    driver.quit();
                    break;
                case "T":
                    path = "tiktok";
                    String proxy = FileUtils.readToken("proxy.txt");
                    browser = new Browser(proxy);
                    driver = browser.initBrowser(false, false);
                    Profile profile = getUserFeed(uid, driver);
                    FileUtils.saveData(path, uid, profile);
                    driver.quit();
                    break;
                default:
                    resultScreen = "Fail";
                    break;
            }

        } catch (Exception e) {
            resultScreen = "fail";
            e.printStackTrace();
            if (driver != null) {
                driver.quit();
            }
            System.out.println(uid + " in " + socialType + " failed");
            if (e.getMessage().contains("does not exist")) {
                System.out.println("User not found");
            } else {
                System.out.println(e.getMessage());
            }
        }
        /*long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");*/
        System.out.println(resultScreen);
        System.exit(0);
    }
}
