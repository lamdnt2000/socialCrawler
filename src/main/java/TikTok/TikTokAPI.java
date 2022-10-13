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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static TikTok.JsonUtil.*;
import static TikTok.TiktokConstants.*;

public class TikTokAPI {
    public static Profile userData;

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
            ItemListHeader requestHeader = new ItemListHeader(userAgent, user.getSecUid(), cursor, user.getId());
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
        driver.quit();
        if (isFetchComment) {
            ExecutorService es = Executors.newFixedThreadPool(3);
            List<Future<WebDriver>> futures = new ArrayList<>();
            for (int i=0;i<3;i++){
                futures.add(es.submit(new MultiBroswer()));
            }
            List<WebDriver> drivers = new ArrayList<>();
            for (int i=0;i<3;i++){
                drivers.add(futures.get(i).get());
            }
            List<Future<Video>> futuresItem = new ArrayList<>();
            for (int i=0;i< videoList.size()-3;i++){

               for (WebDriver d: drivers){
                   Video v = videoList.get(i++);
                   futuresItem.add(es.submit(new FetchComment(username,v,d)));
               }

            }
            List<Video> videos = new ArrayList<>();
            for (Future<Video> f: futuresItem){
                Video v = f.get();
                System.out.println(v.getComments().size());
                videos.add(v);
            }
            es.shutdown();
            user.setVideos(videos);
        }

        return user;
    }

    public static List<Comment> getCommentsByVideo(String username, Video video, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
        String id = video.getId();
        System.out.println("Start "+id);
        driver.get(String.format("https://www.tiktok.com/@%s/video/%s?is_from_webapp=v1&item_id=%s", username, id, id));
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
            int cursor = 0;
            int hasMore = 0;
            do {
                CommentHeader requestHeader = new CommentHeader(userAgent, id, cursor);
                TiktokRequest tiktokRequest = new TiktokRequest(requestHeader);
                Object itemVideos = tiktokRequest.excuse(driver, URL_COMMENT_LIST, false);
                String json = oMapper.writeValueAsString(itemVideos);
                List<Comment> result = JsonUtil.parseJsonComment(json);
                JsonNode videoResult = oMapper.readValue(json, JsonNode.class);
                hasMore = videoResult.get("has_more").intValue();
                if (hasMore == 1) {
                    cursor = videoResult.get("cursor").intValue();
                }
                if (result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        Comment c = result.get(i);
                        if (c.getReply_comment_total() > 0) {
                            c.setReplies(getCommentsReply(id, c, driver));
                        }
                    }
                    comments.addAll(result);
                }
                Thread.sleep(450);
            } while (hasMore == 1);
        }
        System.out.println("Finish "+id);
        return comments;
    }

    public static List<Comment> getCommentsReply(String videoId, Comment comment, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
        List<Comment> comments = new ArrayList<>();
        String userAgent = getUserAgent(driver);
        int cursor = 0;
        int hasMore = 0;
        ObjectMapper oMapper = new ObjectMapper();
        do {
            ReplyCommentHeader requestHeader = new ReplyCommentHeader(userAgent, cursor, comment.getCid(), videoId);
            TiktokRequest<ReplyCommentHeader> tiktokRequest = new TiktokRequest<>(requestHeader);
            Object itemVideos = tiktokRequest.excuse(driver, URL_REPLY_LIST, false);
            String json = oMapper.writeValueAsString(itemVideos);
            List<Comment> result = JsonUtil.parseJsonComment(json);
            JsonNode videoResult = oMapper.readValue(json, JsonNode.class);
            hasMore = videoResult.get("has_more").intValue();
            if (hasMore == 1) {
                cursor = videoResult.get("cursor").intValue();
            }
            if (result != null) {
                comments.addAll(result);
            }
            Thread.sleep(350);
        } while (hasMore == 1);
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
        } while (hasMore);
        return videos;
    }

    public static String getUserAgent(WebDriver driver) {
        return (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent");
    }

}
