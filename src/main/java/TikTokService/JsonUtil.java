package TikTokService;

import TikTokService.Model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static Util.CommonUtil.parseUrlToBase64;
import static Util.RequestUtil.bypassCaptcha;

public class JsonUtil {
    public static ObjectMapper mapper = new ObjectMapper();
    public static List<Video> parseJsonVideo(Object objs) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = mapper.writeValueAsString(objs);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        return mapper.readValue(mapper.writeValueAsString(node.get("itemList")), new TypeReference<List<Video>>() {
        });
    }

    public static Profile parseJsonInfo(String json, String username) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)   ;
        JsonNode jsonNode = mapper.readValue(json,JsonNode.class);
        Profile user = mapper.readValue(mapper.writeValueAsString(jsonNode.get("users").get(username)), Profile.class);
        UserStats stats = mapper.readValue(mapper.writeValueAsString(jsonNode.get("stats").get(username)),UserStats.class);
        user.setStats(stats);
        return user;
    }

    public static List<Comment> parseJsonComment(Object obj) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = mapper.writeValueAsString(obj);
        JsonNode[] nodes = mapper.readValue(json, JsonNode[].class);
        List<Comment> comments = new ArrayList<>();
        for (JsonNode node: nodes){
            if (node.get("comments").size()>0){
                String jsonItem = mapper.writeValueAsString(node.get("comments"));
                Comment[] result  = mapper.readValue(jsonItem, Comment[].class);
                comments.addAll(Arrays.asList(result));
            }
        }
        return comments;
    }

    public static List<Discover> parseJsonDiscorver(String json) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         JsonNode node = mapper.readValue(json, JsonNode.class);
        String jsonItem = mapper.writeValueAsString(node.get("body"));
        JsonNode[] comments  = mapper.readValue(jsonItem, JsonNode[].class);
        List<Discover<UserDiscover>> users = mapper.readValue(mapper.writeValueAsString(comments[0].get("exploreList")), new TypeReference<List<Discover<UserDiscover>>>(){});
        List<Discover<Challenge>> challenges = mapper.readValue(mapper.writeValueAsString(comments[1].get("exploreList")), new TypeReference<List<Discover<Challenge>>>(){});
        List<Discover<Music>> musics = mapper.readValue(mapper.writeValueAsString(comments[2].get("exploreList")), new TypeReference<List<Discover<Music>>>(){});
        List<Discover> discovers = new ArrayList<>();
        discovers.addAll(users);
        discovers.addAll(challenges);
        discovers.addAll(musics);
        discovers.stream().forEach(discover -> System.out.println(discover.getTitle()));
        return discovers;

    }

    public static List<ChallengeSearch> parseJsonChallege(String json) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        String jsonItem = mapper.writeValueAsString(node.get("challengeInfoList"));
        System.out.println(jsonItem);
        List<ChallengeSearch> challenges = mapper.readValue(jsonItem, new TypeReference<List<ChallengeSearch>>(){});
        return challenges;

    }


}
