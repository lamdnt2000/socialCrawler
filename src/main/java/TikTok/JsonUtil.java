package TikTok;

import TikTok.Model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtil {
    public static ObjectMapper mapper = new ObjectMapper();
    public static List<Video> parseJsonVideo(String json) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        json = mapper.writeValueAsString(node.get("itemList"));
        Video[] videoList = mapper.readValue(json, Video[].class);
        return Arrays.asList(videoList);
    }

    public static Profile parseJsonInfo(String json, String username) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)   ;
        JsonNode jsonNode = mapper.readValue(json,JsonNode.class);
        Profile user = mapper.readValue(mapper.writeValueAsString(jsonNode.get("users").get(username)), Profile.class);
        UserStats stats = mapper.readValue(mapper.writeValueAsString(jsonNode.get("stats").get(username)),UserStats.class);
        user.setStats(stats);
        return user;
    }

    public static List<Comment> parseJsonComment(String json) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        String jsonItem = mapper.writeValueAsString(node.get("comments"));
        if (jsonItem.equals("null")){
            return null;
        }
        Comment[] comments  = mapper.readValue(jsonItem, Comment[].class);
        return Arrays.asList(comments);
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

    public static void testStatisticVideoStats() throws IOException{
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode node = mapper.readValue(new File("challegen.json"), JsonNode.class);
        List<Video> videos = mapper.readValue(mapper.writeValueAsString(node), new TypeReference<List<Video>>(){});
        long comment = 0;
        long view = 0;
        long heart = 0;
        long share = 0;
        int count = 0;
        for (Video video: videos){
            count++;
            System.out.println(video.getId());
            VideoStats stats = video.getStats();
            comment+=stats.getCommentCount();
            view+=stats.getPlayCount();
            share+=stats.getShareCount();
            heart+=stats.getDiggCount();
        }
        System.out.println("Total video: "+count);
        System.out.println("Total comment: "+comment);
        System.out.println("Total view: "+view);
        System.out.println("Total share: "+share);
        System.out.println("Total heart: "+heart);
    }

    public static void main(String[] args) throws IOException {
        JsonNode node =mapper.readValue(new File("demo.json"), JsonNode.class);
        String json = mapper.writeValueAsString(node);
        List<Comment> comments = parseJsonComment(json);
        for (Comment c: comments){
            System.out.println(c.getCid());
            System.out.println(c.getText());
            System.out.println(c.getUser().getUid());
            System.out.println(c.getUser().getNickname());
            System.out.println(c.getTextExtra());
        }
    }

}
