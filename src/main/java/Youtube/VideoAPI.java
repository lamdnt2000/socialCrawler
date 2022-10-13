package Youtube;

import Browser.Browser;
import Youtube.Header.VideoDetailHeader;
import Youtube.Header.VideoHeader;
import Youtube.Model.User;
import Youtube.Model.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Youtube.JsonUtil.*;

public class VideoAPI {
    public static ObjectMapper mapper = new ObjectMapper();

    public static User getUserProfile(String channelId, WebDriver driver) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        driver.get("https://www.youtube.com/channel/" + channelId + "/videos");
        Object obj = ((JavascriptExecutor) driver).executeScript("return  window.ytInitialData");
        return parseUser(obj);

    }

    public static User getVideoIds(String channelId, WebDriver driver, Browser browser) throws IOException, IllegalAccessException, XPathExpressionException, ParserConfigurationException, SAXException {
        User user = getUserProfile(channelId, driver);
        Object items = ((JavascriptExecutor) driver).executeScript("return  window.ytInitialData.contents.twoColumnBrowseResultsRenderer.tabs[1].tabRenderer.content.sectionListRenderer.contents[0].itemSectionRenderer.contents[0].gridRenderer.items");

        List<Video> videos = new ArrayList<>();
        String continueToken = parseVideoList(items, videos);
        while (continueToken != "") {
            VideoHeader videoHeader = new VideoHeader(browser.getUserAgent(), continueToken);
            YoutubeRequest<VideoHeader> request = new YoutubeRequest<>(videoHeader);
            Object result = request.excuse(driver, "https://www.youtube.com/youtubei/v1/browse?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8&prettyPrint=false");
            continueToken = parseVideoList(result, videos);

        }
        /*videos = getVideoIds(videos, driver, browser);*/
        user.setVideos(videos);
        for (Video v: videos){
            System.out.println(v.getVideoId());
        }
        return user;
    }

    public static List<Video> getVideoIds(List<Video> videos, WebDriver driver, Browser browser) throws IOException, IllegalAccessException {
        List<Video> result = new ArrayList<>();
        for (Video v : videos) {
            String videoId = v.getVideoId();
            System.out.println(videoId);
            VideoDetailHeader videoDetailHeader = new VideoDetailHeader(browser.getUserAgent(), videoId);
            YoutubeRequest<VideoDetailHeader> request = new YoutubeRequest<>(videoDetailHeader);
            Object item = request.excuse(driver, "https://www.youtube.com/youtubei/v1/next?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8&prettyPrint=false");
            Video video = parseVideoDetail(item);
            video.setVideoId(videoId);
            result.add(video);
        }
        return result;
    }


}
