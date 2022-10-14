package TikTok;

import Browser.Browser;
import TikTok.Header.*;
import TikTok.Model.*;
import TikTok.Thread.FetchComment;
import TikTok.Thread.MultiBroswer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static Browser.RandomUserAgent.getRandomUserAgent;
import static TikTok.JsonUtil.*;
import static TikTok.TiktokConstants.*;
import static TikTok.TiktokRequest.excuseMultiFetch;

public class TikTokAPI {
    public static Profile userData;
    public static List<WebDriver> drivers = new ArrayList<>();
    public static Profile getUserProfile(String username, WebDriver driver) throws IOException {
        driver.get("https://tiktok.com");
        driver.get("https://tiktok.com/@" + username);
        ObjectMapper oMapper = new ObjectMapper();
        Object basicObject = ((JavascriptExecutor) driver).executeScript("return JSON.parse(document.getElementById('SIGI_STATE').text).MobileUserModule");
        String json = oMapper.writeValueAsString(basicObject);
        return parseJsonInfo(json, username);

    }

    public static Profile getUserFeed(String username, WebDriver driver, boolean isFetchComment) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException, ExecutionException {
        Profile user = getUserProfile(username, driver);
        String userAgent = getUserAgent(driver);
        long cursor = 0;
        boolean hasMore = false;
        ObjectMapper oMapper = new ObjectMapper();
        List<Video> videoList = new ArrayList<>();
        do {
            ItemListHeader requestHeader = new ItemListHeader(getRandomUserAgent(), user.getSecUid(), cursor, user.getId());
            TiktokRequest tiktokRequest = new TiktokRequest(requestHeader);
            Object itemVideos = tiktokRequest.excuse(driver, URL_ITEM_LIST, true);
            String json = oMapper.writeValueAsString(itemVideos);
            List<Video> result = JsonUtil.parseJsonVideo(json);
            JsonNode videoResult = oMapper.readValue(json, JsonNode.class);
            hasMore = videoResult.get("hasMore").booleanValue();
            if (hasMore) {
                cursor = Long.parseLong(videoResult.get("cursor").textValue());
            }
            videoList.addAll(result);
            System.out.println(videoList.size());
            Thread.sleep(450);
        } while (hasMore);
        if (isFetchComment) {
            List<Future<Video>> futuresItem = new ArrayList<>();
            ExecutorService exService = new ThreadPoolExecutor(
                    3,
                    3,
                    300L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(videoList.size()));
            int j = 0;
            for (int i = 0; i < videoList.size(); i++) {

                Video v = videoList.get(i);
                futuresItem.add(exService.submit(new FetchComment(username, v, drivers.get(j++))));
                if (j == 3) {
                    j = 0;
                }
            }
            List<Video> videos = new ArrayList<>();
            for (int i = 0; i < videoList.size(); i++) {
                Video v = futuresItem.get(i).get();
                System.out.println(v.getComments().size());
                videos.add(v);
            }
            user.setVideos(videos);
        }

        return user;
    }

    public static List<Comment> getCommentsByVideo(String username, Video video, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
        String id = video.getId();
        System.out.println("Start " + id);
        driver.get(String.format("https://www.tiktok.com/@%s/video/%s?is_from_webapp=v1&item_id=%s", username, id, id));
        int commentCount = (int) video.getStats().getCommentCount();
        String userAgent = getUserAgent(driver);
        ObjectMapper oMapper = new ObjectMapper();
        List<Comment> comments = new ArrayList<>();
        boolean flag = true;
        try {
            Object categoryBoj = ((JavascriptExecutor) driver).executeScript("return JSON.parse(document.getElementById('SIGI_STATE').text).SharingVideoModule.videoData.itemInfo.itemStruct.diversificationLabels");
            List<String> categories = oMapper.readValue(mapper.writeValueAsString(categoryBoj), new TypeReference<List<String>>() {
            });
            video.setCategories(categories);
        } catch (JavascriptException e) {
            System.out.println(e);
            flag = false;
        }
        if (flag) {
            CommentHeader header = new CommentHeader(getRandomUserAgent(), id, 0);
            TiktokRequest<CommentHeader> request = new TiktokRequest<>(header);
            List<String> params = request.bulkCommentRequest(commentCount, 25);
            for (String p : params) {
                Object object = excuseMultiFetch(p, driver);
                List<Comment> list = parseJsonComment(object);
                comments.addAll(list);
                Thread.sleep(3500);
            }

        }
        List<Comment> replies = comments.parallelStream().filter(element -> element.getReply_comment_total() > 0).collect(Collectors.toList());
        Map<Long, List<Comment>> result = new HashMap<>();
        List<Comment> temp = getCommentsReply(id, replies, driver);
        for (Comment c: temp){
            long cid = c.getReply_id();
            if (result.containsKey(cid)){
                result.get(cid).add(c);
            }
            else{
                List<Comment> key = new ArrayList<>();
                key.add(c);
                result.put(cid, key);
            }
        }
        for (int i=0;i<comments.size();i++){
            Comment c = comments.get(i);
            if (result.containsKey(c.getCid())){
                c.setReplies(result.get(c.getCid()));
            }
            comments.set(i, c);

        }
        return comments;
    }

    public static List<Comment> getCommentsReply(String videoId, List<Comment> replyComment, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
        List<Comment> comments = new ArrayList<>();
        System.out.println("Start reply video: "+ videoId);
        String userAgent = getUserAgent(driver);
        List<String> params = TiktokRequest.generateReplyFromComment(replyComment, getRandomUserAgent(), videoId);
        for (String p: params){
            Object object = excuseMultiFetch(p,driver);
            List<Comment> temp = parseJsonComment(object);
            Thread.sleep(3500);
            comments.addAll(temp);
        }
        return comments;
    }

    public static List<Discover> getDiscover(WebDriver driver) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException {
        driver.get("https://tiktok.com");
        ObjectMapper oMapper = new ObjectMapper();
        String userAgent = getUserAgent(driver);
        List<Discover> discovers = new ArrayList<>();
        DiscoverHeader requestHeader = new DiscoverHeader(userAgent);
        TiktokRequest tiktokRequest = new TiktokRequest(requestHeader);
        Object itemsObject = tiktokRequest.excuse(driver, URL_DISCOVER_LIST, false);
        String json = oMapper.writeValueAsString(itemsObject);
        discovers = parseJsonDiscorver(json);
        return discovers;
    }

    public static List<ChallengeSearch> getChallenge(String keyword, WebDriver driver) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException {
        driver.get("https://www.tiktok.com/discover?lang=en");
        ObjectMapper oMapper = new ObjectMapper();
        List<ChallengeSearch> challenges = new ArrayList<>();
        String userAgent = getUserAgent(driver);
        ChallegeHeader requestHeader = new ChallegeHeader(userAgent, keyword, 0);
        TiktokRequest tiktokRequest = new TiktokRequest(requestHeader);
        Object itemsObject = tiktokRequest.excuse(driver, URL_CHALLEGE_LIST, false);
        String json = oMapper.writeValueAsString(itemsObject);
        challenges = parseJsonChallege(json);
        System.out.println(challenges);
        return challenges;
    }

    public static List<Video> getVideoByHashtag(ChallengeSearch challenge, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
        String title = challenge.getTitle();
        driver.get(String.format("https://www.tiktok.com/tag/%s?lang=en", title));
        ObjectMapper oMapper = new ObjectMapper();
        List<Video> videos = new ArrayList<>();
        String userAgent = getUserAgent(driver);
        int cursor = 0;
        boolean hasMore = false;
        do {
            ChallengeItemsHeader requestHeader = new ChallengeItemsHeader(challenge.getId(), 30, cursor, userAgent);
            TiktokRequest tiktokRequest = new TiktokRequest(requestHeader);
            Object itemObjects = tiktokRequest.excuse(driver, URL_CHALLEGE_ITEM_LIST, false);
            String json = oMapper.writeValueAsString(itemObjects);
            List<Video> result = JsonUtil.parseJsonVideo(json);
            JsonNode videoResult = oMapper.readValue(json, JsonNode.class);
            hasMore = videoResult.get("hasMore").booleanValue();
            if (hasMore) {
                cursor = Integer.parseInt(videoResult.get("cursor").textValue());
                System.out.println(cursor);
            }
            if (result != null) {
                videos.addAll(result);
            }
            Thread.sleep(450);
        } while (hasMore);

        return videos;
    }

    public static String getUserAgent(WebDriver driver) {
        return (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent");
    }

}
