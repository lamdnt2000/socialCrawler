package TikTokService;

import TikTokService.Header.ChallegeHeader;
import TikTokService.Header.CommentHeader;
import TikTokService.Header.DiscoverHeader;
import TikTokService.Header.ItemListHeader;
import TikTokService.Model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static Browser.RandomUserAgent.getRandomUserAgent;
import static TikTokService.JsonUtil.*;
import static TikTokService.TiktokConstants.*;
import static TikTokService.TiktokRequest.excuseMultiFetch;
import static Util.CommonUtil.parseUrlToBase64;
import static Util.RequestUtil.bypassCaptcha;

public class TikTokAPI {
    public static Profile userData;
    public static List<WebDriver> drivers = new ArrayList<>();
    public static boolean IS_FETCH_COMMENT = false;
    public static String verifyFp;

    public static Profile getUserProfile(String username, WebDriver driver) throws Exception {
        driver.get("https://tiktok.com/@" + username);

        driver.navigate().refresh();
        boolean isCaptcha = isCaptcha(driver);
        if (isCaptcha) {
            do {

                CaptchaResolver(driver);
                Thread.sleep(2000);
                isCaptcha = isCaptcha(driver);
            }
            while (isCaptcha);
        }
        else{
            throw  new Exception("By pass captcha failed");
        }
        GetVerifyFp(driver);

        Object basicObject = ((JavascriptExecutor) driver).executeScript("return JSON.parse(document.getElementById('SIGI_STATE').text).UserModule");

        return parseJsonInfo(basicObject, username);

    }

    public static Profile getUserFeed(String username, WebDriver driver) throws Exception {
        Profile user = getUserProfile(username, driver);
        long cursor = 0;
        boolean hasMore = false;

        String userAgent = getUserAgent(driver);

        List<Object> fetch = new ArrayList<>();
        ThreadLocal<List<Object>> tFetch = new ThreadLocal<>();
        tFetch.set(fetch);

        do {
            ItemListHeader requestHeader = new ItemListHeader(userAgent, user.getSecUid(), cursor, user.getId(), verifyFp);
            TiktokRequest tiktokRequest = new TiktokRequest(requestHeader);
            Object itemVideos = tiktokRequest.excuse(driver, URL_ITEM_LIST, true);
            fetch.add(itemVideos);

            JsonNode videoResult = mapper.readValue(mapper.writeValueAsString(itemVideos), JsonNode.class);
            hasMore = videoResult.get("hasMore").booleanValue();
            if (hasMore) {
                cursor = Long.parseLong(videoResult.get("cursor").textValue());
            }

        } while (hasMore);


        List<Video> result = JsonUtil.parseJsonVideo(fetch);

        user.setVideos(result);
        tFetch.remove();
       /* Thread.sleep(450);
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
            videos.add(v);
        }
        exService.shutdown();*/


        return user;
    }

    public static List<Comment> getCommentsByVideo(String username, Video video, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
        String id = video.getId();
        System.out.println("Start Video id: " + id);
        driver.get(String.format("https://www.tiktok.com/@%s/video/%s?is_from_webapp=v1&item_id=%s", username, id, id));
        int commentCount = (int) video.getStats().getCommentCount();
        ObjectMapper oMapper = new ObjectMapper();
        List<Comment> comments = new ArrayList<>();
        boolean flag = true;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"SIGI_STATE\"]")));
            Object categoryBoj = ((JavascriptExecutor) driver).executeScript("return JSON.parse(document.getElementById('SIGI_STATE').text).SharingVideoModule.videoData.itemInfo.itemStruct.diversificationLabels");
            List<String> categories = oMapper.readValue(mapper.writeValueAsString(categoryBoj), new TypeReference<List<String>>() {
            });
            video.setCategories(categories);
        } catch (JavascriptException e) {
            System.out.println(video.getId());
            flag = false;
        }
        if (IS_FETCH_COMMENT) {
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
            for (Comment c : temp) {
                long cid = c.getReply_id();
                if (result.containsKey(cid)) {
                    result.get(cid).add(c);
                } else {
                    List<Comment> key = new ArrayList<>();
                    key.add(c);
                    result.put(cid, key);
                }
            }
            for (int i = 0; i < comments.size(); i++) {
                Comment c = comments.get(i);
                if (result.containsKey(c.getCid())) {
                    c.setReplies(result.get(c.getCid()));
                }
                comments.set(i, c);

            }
            System.out.println("Finish videoId : " + id);
            return comments;
        }

        return null;
    }

    public static List<Comment> getCommentsReply(String videoId, List<Comment> replyComment, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
        List<Comment> comments = new ArrayList<>();
        System.out.println("Start reply video: " + videoId);
        String userAgent = getUserAgent(driver);
        List<String> params = TiktokRequest.generateReplyFromComment(replyComment, getRandomUserAgent(), videoId);
        for (String p : params) {
            Object object = excuseMultiFetch(p, driver);
            List<Comment> temp = parseJsonComment(object);
            Thread.sleep(3500);
            comments.addAll(temp);
        }
        System.out.println("Finish reply video: " + videoId);
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

    /*public static List<Video> getVideoByHashtag(ChallengeSearch challenge, WebDriver driver) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalAccessException, InterruptedException {
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
*/
    public static String getUserAgent(WebDriver driver) {
        return (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent");
    }

    public static boolean isCaptcha(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"tiktok-verify-ele\"]/div/div[2]/img[1]")));
            return true;
        } catch (Exception e) {
            System.out.println("Captcha not found");
            return false;
        }

    }

    public static void GetVerifyFp(WebDriver driver) {
        Cookie cookie = driver.manage().getCookieNamed("s_v_web_id");
        if (cookie != null) {
            verifyFp = cookie.getValue();
        }

    }

    public static void CaptchaResolver(WebDriver driver) throws IOException, InterruptedException {
        WebElement inner = driver.findElement(By.xpath("//*[@id=\"tiktok-verify-ele\"]/div/div[2]/img[2]"));
        WebElement outer = driver.findElement(By.xpath("//*[@id=\"tiktok-verify-ele\"]/div/div[2]/img[1]"));
        String srcInner = parseUrlToBase64(inner.getAttribute("src"));

        String srcOuter = parseUrlToBase64(outer.getAttribute("src"));
        WebElement handler = driver.findElement(By.xpath("//*[@id=\"secsdk-captcha-drag-wrapper\"]/div[2]"));
        Random random = new Random();
        int result = bypassCaptcha(srcInner, srcOuter);
        int px = result / 6;
        new Actions(driver).
                clickAndHold(handler)
                .pause(Duration.ofMillis(random.longs(350, 600).findFirst().getAsLong()))
                .moveByOffset(px, 0)
                .pause(Duration.ofMillis(random.longs(350, 600).findFirst().getAsLong()))
                .moveByOffset(px, 0)
                .pause(Duration.ofMillis(random.longs(350, 600).findFirst().getAsLong()))
                .moveByOffset(px, 0)
                .pause(Duration.ofMillis(random.longs(350, 600).findFirst().getAsLong()))
                .moveByOffset(px, 0)
                .pause(Duration.ofMillis(random.longs(350, 600).findFirst().getAsLong()))
                .moveByOffset(px, 0).
                pause(Duration.ofMillis(random.longs(350, 600).findFirst().getAsLong()))
                .moveByOffset(px, 0).
                pause(Duration.ofMillis(random.longs(350, 600).findFirst().getAsLong()))
                .release().perform();
    }


}
