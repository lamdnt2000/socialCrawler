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
import java.util.concurrent.TimeUnit;

import static YoutubeService.JsonUtil.*;
import static YoutubeService.YoutubeRequest.*;

public class VideoAPI {
    public static ObjectMapper mapper = new ObjectMapper();
    public static List<String> ids = new ArrayList<>();
    public static User getUserProfile(String channelId, WebDriver driver) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, InterruptedException {
        String url = "https://www.youtube.com/";
        if (!channelId.startsWith("@")){
            if (channelId.startsWith("UC")){
                url = url + "channel/";
            }
            else{
                url  = url + "c/";
            }
        }
        url = url + channelId + "/videos";
        driver.get(url);

        Object obj = ((JavascriptExecutor) driver).executeScript("return  window.ytInitialData");

        User user = parseUser(obj);
        return user;

    }

    public static User getVideoIds(String channelId, WebDriver driver, Browser browser) throws IOException, IllegalAccessException, XPathExpressionException, ParserConfigurationException, SAXException, InterruptedException {
        User user = getUserProfile(channelId, driver);
        Object items = ((JavascriptExecutor) driver).executeScript("return  window.ytInitialData.contents.twoColumnBrowseResultsRenderer.tabs[1].tabRenderer.content");

        String continueToken = initFirstVideoList(items, ids);
        ThreadLocal<Object> tResult = new ThreadLocal<>();
        while (!continueToken.equals("")) {
            VideoHeader videoHeader = new VideoHeader(browser.getUserAgent(), continueToken);
            YoutubeRequest<VideoHeader> request = new YoutubeRequest<>(videoHeader);
            Object result = request.excuse(driver, "https://www.youtube.com/youtubei/v1/browse?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8&prettyPrint=false");
            tResult.set(result);
            continueToken = parseVideoList(result, ids);
            tResult.remove();
        }

        return user;
    }

    public static List<Video> getVideoDetails(WebDriver driver, Browser browser) throws IOException, IllegalAccessException, InterruptedException {
        List<Video> result = new ArrayList<>();
        VideoDetailHeader videoDetailHeader = new VideoDetailHeader(browser.getUserAgent(), null);
        YoutubeRequest<VideoDetailHeader> request = new YoutubeRequest<>(videoDetailHeader);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.MINUTES);
        List<String> params = bulkVideoRequestDetail(ids, browser.getUserAgent());

        //Get video detail
        ThreadLocal<Object> tResult = new ThreadLocal<>();
        for (String param : params) {
            long start = System.currentTimeMillis();

            Object item = excuseMultiFetch(param, driver);
            tResult.set(item);
            result.addAll(parseVideoDetail(item));
            tResult.remove();
            long end = System.currentTimeMillis();
            System.out.println(TimeUnit.MILLISECONDS.toSeconds(end-start));

        }

        for (int i =0; i<ids.size();i++){
            result.get(i).setVideoId(ids.get(i));
        }
        //Get video reactions
        getVideoReactions(result, browser, driver);
        //Set reaction for videos


        return result;
    }

    public static List<Video> getVideoReactions(List<Video> videos, Browser browser, WebDriver driver) throws IOException, IllegalAccessException {
        List<String> params = bulkVideoRequestRection(videos, browser.getUserAgent());
        List<Reaction> reactions = new ArrayList<>();
        ThreadLocal<Object> tResult = new ThreadLocal<>();
        for (String param : params) {
            /*System.out.println("Fetch reactions");*/

            Object item = excuseMultiFetch(param, driver);
            tResult.set(item);
            reactions.addAll(parseVideoReaction(item));

        }
        tResult.remove();
        for (int i = 0; i < videos.size(); i++) {
            Video video = videos.get(i);
            Reaction reaction = reactions.get(i);
            video.setReaction(reaction);
        }
        return videos;
    }

}
