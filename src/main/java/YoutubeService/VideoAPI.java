package YoutubeService;

import Browser.Browser;
import YoutubeService.Header.VideoDetailHeader;
import YoutubeService.Header.VideoHeader;
import YoutubeService.Model.Reaction;
import YoutubeService.Model.User;
import YoutubeService.Model.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static YoutubeService.JsonUtil.*;
import static YoutubeService.YoutubeRequest.*;

public class VideoAPI {
    public static ObjectMapper mapper = new ObjectMapper();
    public static List<String> ids = new ArrayList<>();

    public static User getUserProfile(String channelId, WebDriver driver) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        driver.get("https://www.youtube.com/channel/" + channelId + "/videos");
        Object obj = ((JavascriptExecutor) driver).executeScript("return  window.ytInitialData");
        return parseUser(obj);

    }

    public static User getVideoIds(String channelId, WebDriver driver, Browser browser) throws IOException, IllegalAccessException, XPathExpressionException, ParserConfigurationException, SAXException, InterruptedException {
        User user = getUserProfile(channelId, driver);
        Thread.sleep(3000);
        Object items = ((JavascriptExecutor) driver).executeScript("return  window.ytInitialData.contents.twoColumnBrowseResultsRenderer.tabs[1].tabRenderer.content");

        String continueToken = initFirstVideoList(items, ids);
        while (!continueToken.equals("")) {
            System.out.println(ids.size());
            VideoHeader videoHeader = new VideoHeader(browser.getUserAgent(), continueToken);
            YoutubeRequest<VideoHeader> request = new YoutubeRequest<>(videoHeader);
            Object result = request.excuse(driver, "https://www.youtube.com/youtubei/v1/browse?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8&prettyPrint=false");
            continueToken = parseVideoList(result, ids);

        }
        /*System.out.println(ids.size());*/
        return user;
    }

    public static List<Video> getVideoDetails(WebDriver driver, Browser browser) throws IOException, IllegalAccessException, InterruptedException {
        List<Video> result = new ArrayList<>();
        VideoDetailHeader videoDetailHeader = new VideoDetailHeader(browser.getUserAgent(), null);
        YoutubeRequest<VideoDetailHeader> request = new YoutubeRequest<>(videoDetailHeader);
        List<String> params = bulkVideoRequestDetail(ids, browser.getUserAgent());
        //Get video detail
        for (String param : params) {
            /*System.out.println("Fetch details");*/
            Object item = excuseMultiFetch(param, driver);
            result.addAll(parseVideoDetail(item));
        }

        //Get video reactions
        getVideoReactions(result, browser, driver);
        //Set reaction for videos

        return result;
    }

    public static List<Video> getVideoReactions(List<Video> videos, Browser browser, WebDriver driver) throws IOException, IllegalAccessException {
        List<String> params = bulkVideoRequestRection(videos, browser.getUserAgent());
        List<Reaction> reactions = new ArrayList<>();
        for (String param : params) {
            /*System.out.println("Fetch reactions");*/
            Object item = excuseMultiFetch(param, driver);
            reactions.addAll(parseVideoReaction(item));
        }
        for (int i = 0; i < videos.size(); i++) {
            Video video = videos.get(i);
            Reaction reaction = reactions.get(i);
            video.setReaction(reaction);
        }
        return videos;
    }

}
