package YoutubeService;

import YoutubeService.Model.Reaction;
import YoutubeService.Model.User;
import YoutubeService.Model.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsfr.json.Collector;
import org.jsfr.json.JacksonParser;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.ValueBox;
import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JacksonProvider;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JsonUtil {
    public static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ;
    public static JsonPath surferVideoDetail = JsonPathCompiler.compile("$.*");
    public static JsonPath surferVideoReaction = JsonPathCompiler.compile("$.*");
    public static JsonSurfer surfer = new JsonSurfer(JacksonParser.INSTANCE, JacksonProvider.INSTANCE);
    public static JsonPath avataroDetail =
            JsonPathCompiler.compile("$.*.header.c4TabbedHeaderRenderer.avatar.thumbnails[2].url");


    public static User parseUser(Object object) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {


        ThreadLocal<Object> tobject = new ThreadLocal<>();
        ThreadLocal<JsonNode> tnode = new ThreadLocal<>();
        try {
            tobject.set(object);
            String json = mapper.writeValueAsString(object);


            JsonNode node = mapper.readValue(json, JsonNode.class);

            tnode.set(node);
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
            if (user.has("channelHandleText")) {
                JsonNode userNode = user.get("channelHandleText");
                if (userNode.has("runs")) {
                    userNode = userNode.get("runs");
                    if (userNode.has(0)) {
                        userNode = userNode.get(0).get("text");
                        String username = userNode.textValue();
                        result.setUsername(username);
                    }
                }
            }

            result.setDescription(meta.get("description").textValue());
            List<String> tags = mapper.readValue(mapper.writeValueAsString(meta.get("tags")), new TypeReference<List<String>>() {
            });
            result.setTags(tags);
            result.setCountryCode(node.get("topbar").get("desktopTopbarRenderer").get("countryCode").textValue());
            return result;
        } finally {
            tobject.remove();
            tnode.remove();
        }
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
        ThreadLocal<Object> tObject = new ThreadLocal<>();
        ThreadLocal<Collector> tCollector = new ThreadLocal<>();
        try {
            tObject.set(object);
            String continueStr = "";
            Collector collector = surfer.collector(mapper.writeValueAsString(object));
            tCollector.set(collector);
            ValueBox<Collection<String>> result = collector.collectAll("$.sectionListRenderer.contents[0].itemSectionRenderer.contents[0].gridRenderer.items[*].gridVideoRenderer.videoId", String.class);
            collector.exec();
            List<String> temp = new ArrayList<>();
            temp.addAll(result.get());
            if (temp.size() == 0) {
                result = collector.collectAll("$.onResponseReceivedActions[0].appendContinuationItemsAction.continuationItems[*].richItemRenderer.content.videoRenderer.videoId", String.class);
                collector.exec();
                temp.addAll(result.get());
            }
            ids.addAll(temp);

            ValueBox<String> continueBox = collector.collectOne("$.onResponseReceivedActions[0].appendContinuationItemsAction.continuationItems[30].continuationItemRenderer.continuationEndpoint.continuationCommand.token", String.class);
            collector.exec();
            continueStr = continueBox.get();
            return continueStr != null ? continueStr : "";
        } finally {
            tObject.remove();
            tCollector.remove();
        }
    }

    public static List<Video> parseVideoDetail(Object object) throws IOException {
        ThreadLocal<Object> tObject = new ThreadLocal<>();

        try {
            tObject.set(object);
            Collection<Video> videos = surfer.collectAll(mapper.writeValueAsString(object), Video.class, surferVideoDetail);

            return new ArrayList<>(videos);
        } finally {
            tObject.remove();
        }
    }

    public static List<Reaction> parseVideoReaction(Object object) throws IOException {
        ThreadLocal<Object> tObject = new ThreadLocal<>();

        try {
            tObject.set(object);
            Collection<Reaction> reactions = surfer.collectAll(mapper.writeValueAsString(object), Reaction.class, surferVideoReaction);
            return new ArrayList<>(reactions);
        } finally {
            tObject.remove();
        }
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


    public static String initFirstVideoList(Object obj, List<String> ids) throws IOException {
        ThreadLocal<Object> tObject = new ThreadLocal<>();
        ThreadLocal<Collector> tCollector = new ThreadLocal<>();

        try {
            tObject.set(obj);
            Collector collector = surfer.collector(mapper.writeValueAsString(obj));
            tCollector.set(collector);
            String continueStr = "";
            ValueBox<Collection<String>> result = collector.collectAll("$.sectionListRenderer.contents[0].itemSectionRenderer.contents[0].gridRenderer.items[*].gridVideoRenderer.videoId", String.class);
            collector.exec();
            ids.addAll(result.get());

            if (ids.size() == 0) {
                result = collector.collectAll("$.richGridRenderer.contents[*].richItemRenderer.content.videoRenderer.videoId", String.class);
                collector.exec();
                ids.addAll(result.get());
                if (ids.size() > 0) {
                    ValueBox<String> continueBox = collector.collectOne("$.richGridRenderer.contents[" + ids.size() + "].continuationItemRenderer.continuationEndpoint.continuationCommand.token", String.class);
                    collector.exec();
                    continueStr = continueBox.get();
                }
            } else {
                ValueBox<String> continueBox = collector.collectOne("$.sectionListRenderer.contents[0].itemSectionRenderer.contents[0].gridRenderer.items[" + ids.size() + "].continuationItemRenderer.continuationEndpoint.continuationCommand.token", String.class);
                collector.exec();
                continueStr = continueBox.get();
            }
            return continueStr != null ? continueStr : "";
        } finally {
            tObject.remove();
            tCollector.remove();
        }
    }


}
