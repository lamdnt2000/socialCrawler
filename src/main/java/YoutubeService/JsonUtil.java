package YoutubeService;

import Util.FileUtils;
import YoutubeService.Model.Category;
import YoutubeService.Model.Reaction;
import YoutubeService.Model.User;
import YoutubeService.Model.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class JsonUtil {
    public static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ;
    public static ParseContext jsonPath = JsonPath.using(
            Configuration.builder()
                    .jsonProvider(new JacksonJsonProvider(mapper))
                    .mappingProvider(new JacksonMappingProvider(mapper))
                    .build()
    );

    public static User parseUser(Object object) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        String json = mapper.writeValueAsString(object);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        JsonNode user = node.get("header").get("c4TabbedHeaderRenderer");
        String thumbnail = user.get("avatar").get("thumbnails").get(2).get("url").textValue();
        String userType = "";
        if (user.has("badges")) {
            userType = user.get("badges").get(0).get("metadataBadgeRenderer").get("tooltip").textValue();
        }
        String banner = user.get("banner").get("thumbnails").get(0).get("url").textValue();
        String channelId = user.get("channelId").textValue();
        String browseId = user.get("navigationEndpoint").get("browseEndpoint").get("browseId").textValue();
        String subscriber = user.get("subscriberCountText").get("simpleText").textValue();
        String title = user.get("title").textValue();
        String published = parsePublishedDate(channelId);
        JsonNode meta = node.get("microformat").get("microformatDataRenderer");
        User result = new User(thumbnail, userType, banner, channelId, browseId, subscriber, title, published);
        result.setDescription(meta.get("description").textValue());
        List<String> tags = mapper.readValue(mapper.writeValueAsString(meta.get("tags")), new TypeReference<List<String>>() {
        });
        result.setTags(tags);
        result.setCountryCode(node.get("topbar").get("desktopTopbarRenderer").get("countryCode").textValue());
        return result;
    }

    public static String parsePublishedDate(String channelId) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        URL xmlURL = new URL("https://www.youtube.com/feeds/videos.xml?channel_id=" + channelId);
        InputStream xml = xmlURL.openStream();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xml);
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("/feed/published/text()");
        return (String) expr.evaluate(doc, XPathConstants.STRING);
    }

    public static String parseVideoList(Object object, List<String> ids) throws JsonProcessingException {
        DocumentContext context = jsonPath.parse(object);
        JsonNode node = context.read("$.onResponseReceivedActions[0].appendContinuationItemsAction." +
                "continuationItems[0]",JsonNode.class);
        if (node.has("gridVideoRenderer")){
            List<String> result = context.read("$.onResponseReceivedActions[0].appendContinuationItemsAction." +
                    "continuationItems.[*].gridVideoRenderer.videoId", new TypeRef<List<String>>() {
            });
            ids.addAll(result);
            try {
                return context.read("$.onResponseReceivedActions[0].appendContinuationItemsAction.continuationItems[30].continuationItemRenderer.continuationEndpoint.continuationCommand.token");
            } catch (Exception e) {
                return "";
            }
        }
        else{
            List<String> result = context.read("$.onResponseReceivedActions[0].appendContinuationItemsAction." +
                    "continuationItems.[*].richItemRenderer.content.videoRenderer.videoId", new TypeRef<List<String>>() {
            });
            ids.addAll(result);
            try {
                return context.read("$.onResponseReceivedActions[0].appendContinuationItemsAction.continuationItems[30].continuationItemRenderer.continuationEndpoint.continuationCommand.token");
            } catch (Exception e) {
                return "";
            }
        }

    }

    public static List<Video> parseVideoDetail(Object object) throws IOException {
        JsonNode node = mapper.readValue(mapper.writeValueAsString(object), JsonNode.class);
        List<JsonNode> nodes = jsonPath.parse(object).read("$.[*].microformat", new TypeRef<List<JsonNode>>() {
        });
        return mapper.readValue(mapper.writeValueAsString(nodes), new TypeReference<List<Video>>() {});
    }

    public static List<Reaction> parseVideoReaction(Object object) throws IOException {

        JsonNode node = jsonPath.parse(object).read("$.[*].contents.twoColumnWatchNextResults.results.results", JsonNode.class);
        String json = mapper.writeValueAsString(node);

        return mapper.readValue(json, new TypeReference<List<Reaction>>() {});

    }

    public static List<String> convertIterToList(Iterator<JsonNode> iterator) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (iterator.hasNext()) {
            String item = iterator.next().get("text").textValue();
            if (item != null && !item.equals(" ")) {
                result.add(item);
            }
        }
        return result;
    }

    public static String initFirstVideoList(Object obj, List<String> ids) {
        DocumentContext context = jsonPath
                .parse(obj);
        JsonNode node = context.read("$",JsonNode.class);
        if (node.has("sectionListRenderer")){
            List<String> result = context.read("$.sectionListRenderer.contents[0].itemSectionRenderer.contents[0].gridRenderer.items." +
                            "[*].gridVideoRenderer.videoId",
                    new TypeRef<List<String>>() {
                    });
            ids.addAll(result);
            try {
                return context.read("$.sectionListRenderer.contents[0].itemSectionRenderer.contents[0].gridRenderer.items[30].continuationItemRenderer.continuationEndpoint.continuationCommand.token");
            } catch (Exception e) {
                return "";
            }
        }
        else{
            List<String> result = context.read("$.richGridRenderer.contents.[*].richItemRenderer.content.videoRenderer.videoId",
                    new TypeRef<List<String>>() {
                    });
            ids.addAll(result);
            try {
                return context.read("$.richGridRenderer.contents.[30].continuationItemRenderer.continuationEndpoint.continuationCommand.token");
            } catch (Exception e) {
                return "";
            }
        }

    }


}
