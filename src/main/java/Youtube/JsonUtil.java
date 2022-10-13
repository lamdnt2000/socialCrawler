package Youtube;

import Youtube.Model.User;
import Youtube.Model.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static Youtube.Util.CommonUtil.parseStringToLong;

public class JsonUtil {
    public static ObjectMapper mapper = new ObjectMapper();

    public static User parseUser(Object object) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        String json = mapper.writeValueAsString(object);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        JsonNode user = node.get("header").get("c4TabbedHeaderRenderer");
        String thumbnail = user.get("avatar").get("thumbnails").get(0).get("url").textValue();
        String userType = "";
        if (user.has("badges")){
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
        List<String> tags = mapper.readValue(mapper.writeValueAsString(meta.get("tags")), new TypeReference<List<String>>() {});
        result.setTags(tags);
        result.setCountryCode(node.get("topbar").get("desktopTopbarRenderer").get("countryCode").textValue());
        return result;
    }

    public static String parsePublishedDate(String channelId) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        URL xmlURL = new URL("https://www.youtube.com/feeds/videos.xml?channel_id="+channelId);
        InputStream xml = xmlURL.openStream();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xml);
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("/feed/published/text()");
       return  (String)expr.evaluate(doc, XPathConstants.STRING);
    }

    public static String parseVideoList(Object object, List<Video> videos) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = mapper.writeValueAsString(object);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        List<Video> videoList = new ArrayList<>();
        if (node.has("onResponseReceivedActions")){
            node = node.get("onResponseReceivedActions").get(0).get("appendContinuationItemsAction").get("continuationItems");


        }
        if (node.size()==31){
            JsonNode[] nodes = mapper.readValue(mapper.writeValueAsString(node), JsonNode[].class);
            List<JsonNode> nodeList = new LinkedList<>(Arrays.asList(nodes));
            nodeList.remove(30);
            json = mapper.writeValueAsString(nodeList);
        }
        else {
            json = mapper.writeValueAsString(node);
        }
        videoList =mapper.readValue(json, new TypeReference<List<Video>>() {});
        String continueToken =getContinueToken(node);
        videos.addAll(videoList);
        return continueToken;
    }

    public static String getContinueToken(Object object) throws JsonProcessingException {
        String json = mapper.writeValueAsString(object);
        JsonNode[] nodes = mapper.readValue(json, JsonNode[].class);
        if (nodes.length <31){
            return "";
        }
        else{
            return nodes[nodes.length-1].get("continuationItemRenderer").get("continuationEndpoint").get("continuationCommand").get("token").textValue();
        }
    }

    public static Video parseVideoDetail(Object object) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Map<String, Object> video = new HashMap<>();
        String json = mapper.writeValueAsString(object);
        JsonNode node = mapper.readValue(json, JsonNode.class);
        node = node.get("contents").get("twoColumnWatchNextResults").get("results").get("results").get("contents");
        JsonNode videoPrimary = node.get(0).get("videoPrimaryInfoRenderer");
        String viewStr = videoPrimary.get("viewCount").get("videoViewCountRenderer").get("viewCount").get("simpleText").textValue();
        if (videoPrimary.has("superTitleLink")){
            Iterator<JsonNode> hashtagIter = videoPrimary.get("superTitleLink").get("runs").iterator();
            video.put("hashTags",convertIterToList(hashtagIter));
        }
        JsonNode likeBtn = videoPrimary.get("videoActions").get("menuRenderer").get("topLevelButtons").
                get(0);
        String likeStr;
        if (likeBtn.has("segmentedLikeDislikeButtonRenderer")){
            JsonNode insideLike = likeBtn.get("segmentedLikeDislikeButtonRenderer").get("likeButton").
                    get("toggleButtonRenderer").get("defaultText");
            if (insideLike.has("accessibility")){

                likeStr = insideLike.get("accessibility").get("accessibilityData").get("label").textValue();
            }
            else{
                likeStr = "0 Likes";
            }
        }
        else{
            JsonNode insideLike = likeBtn.get("toggleButtonRenderer").get("defaultText");
            if (insideLike.has("accessibility")){
                likeStr = likeBtn.get("toggleButtonRenderer").get("defaultText").get("accessibility").get("accessibilityData")
                        .get("label").textValue();
            }
            else{
                likeStr ="0 Likes";
            }

        }
        JsonNode videoSecondary = node.get(1).get("videoSecondaryInfoRenderer");
        String description;
        if (videoSecondary.has("attributedDescription")){
            description = videoSecondary.get("attributedDescription").get("content").textValue();
        }
        else{
            Iterator<JsonNode> descriptionIter = videoSecondary.get("description").get("runs").iterator();
            description = String.join("\n",convertIterToList(descriptionIter));
        }

        JsonNode itemSection = node.get(2).get("itemSectionRenderer").get("contents").get(0).get("commentsEntryPointHeaderRenderer");
        JsonNode commentSection = node.get(3).get("itemSectionRenderer").get("contents").get(0).get("continuationItemRenderer");
        video.put("title",videoPrimary.get("title").get("runs").get(0).get("text").textValue());
        video.put("viewCount",parseStringToLong(viewStr));
        video.put("createTime",videoPrimary.get("dateText").get("simpleText").textValue());

        video.put("likeCount", parseStringToLong(likeStr));
        video.put("description",description);
        if (itemSection.has("commentCount")){
            video.put("commentCount",itemSection.get("commentCount").get("simpleText").textValue());
        }

        video.put("continueComment",commentSection.get("continuationEndpoint").get("continuationCommand").get("token").textValue());
        return mapper.readValue(mapper.writeValueAsString(video), Video.class);

    }

    public static List<String> convertIterToList(Iterator<JsonNode> iterator){
        List<String> result = new ArrayList<>();
        int i=0;
        while (iterator.hasNext()){
            String item = iterator.next().get("text").textValue();
            if (item!=null && !item.equals(" ")){
                result.add(item);
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception{
        System.out.println(parseVideoDetail(null));
    }
}
