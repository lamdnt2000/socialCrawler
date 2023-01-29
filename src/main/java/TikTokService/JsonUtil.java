package TikTokService;

import TikTokService.Model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import java.io.IOException;
import java.util.*;

public class JsonUtil {
    public static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // JsonPathCompiler.compile("$[*].itemList[*]['secret', 'desc', 'textExtra', 'comments', 'createTime', 'id', 'categories', 'stats', 'video']");
    public static List<Video> parseJsonVideo(List<Object> objs) throws IOException {
        ThreadLocal<Object> tObject = new ThreadLocal<>();
        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
        tObject.set(objs);
        try {

            List<Video> videos = JsonPath.parse(mapper.writeValueAsString(objs)).read("$[*].itemList[*]", new TypeRef<List<Video>>() {});



            return videos;
        }
        finally {
            tObject.remove();

        }
    }

    public static Profile parseJsonInfo(Object object, String username) throws IOException {
        ThreadLocal<Object> tObject = new ThreadLocal<>();
        tObject.set(object);
        String json = mapper.writeValueAsString(object);
        ThreadLocal<String> tJson = new ThreadLocal<>();
        tJson.set(json);
        try {
            JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
            Profile user = mapper.readValue(mapper.writeValueAsString(jsonNode.get("users").get(username)), Profile.class);
            UserStats stats = mapper.readValue(mapper.writeValueAsString(jsonNode.get("stats").get(username)), UserStats.class);
            user.setStats(stats);
            return user;
        }
        finally {
            tObject.remove();
            tJson.remove();
        }
    }

    public static List<Comment> parseJsonComment(Object obj) throws IOException {
        ThreadLocal<Object> tObject = new ThreadLocal<>();
        ThreadLocal<JsonNode[]> tNodes = new ThreadLocal<>();
        tObject.set(obj);

        JsonNode[] nodes = mapper.readValue(mapper.writeValueAsString(obj), JsonNode[].class);
        tNodes.set(nodes);
        try {
            List<Comment> comments = new ArrayList<>();
            for (JsonNode node : nodes) {
                if (node.get("comments").size() > 0) {
                    String jsonItem = mapper.writeValueAsString(node.get("comments"));
                    Comment[] result = mapper.readValue(jsonItem, Comment[].class);
                    comments.addAll(Arrays.asList(result));
                }
            }
            return comments;
        }
        finally {
            tObject.remove();
            tNodes.remove();
        }
    }

    public static List<Discover> parseJsonDiscorver(String json) throws IOException {
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
        JsonNode node = mapper.readValue(json, JsonNode.class);
        String jsonItem = mapper.writeValueAsString(node.get("challengeInfoList"));
        System.out.println(jsonItem);
        List<ChallengeSearch> challenges = mapper.readValue(jsonItem, new TypeReference<List<ChallengeSearch>>(){});
        return challenges;

    }


}
