package YoutubeService.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import static YoutubeService.JsonUtil.convertIterToList;
import static YoutubeService.Util.CommonUtil.parseStringToLong;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reaction implements Serializable {
    private long commentCount = 0;
    private List<String> hashTags;
    private long likeCount;

    @JsonProperty("contents")
    private void unpackNested(JsonNode node) {
            int i = 0;
            for (; i < node.size(); i++) {
                if (node.get(i).has("videoPrimaryInfoRenderer")) {
                    JsonNode videoPrimary = node.get(i).get("videoPrimaryInfoRenderer");

                    if (videoPrimary.has("superTitleLink")) {
                        Iterator<JsonNode> hashtagIter = videoPrimary.get("superTitleLink").get("runs").iterator();
                        this.setHashTags(convertIterToList(hashtagIter));
                    }
                    JsonNode likeBtn = videoPrimary.get("videoActions").get("menuRenderer").get("topLevelButtons").
                            get(0);
                    String likeStr;
                    if (likeBtn.has("segmentedLikeDislikeButtonRenderer")) {
                        JsonNode insideLike = likeBtn.get("segmentedLikeDislikeButtonRenderer").get("likeButton").
                                get("toggleButtonRenderer").get("defaultText");
                        if (insideLike.has("accessibility")) {

                            likeStr = insideLike.get("accessibility").get("accessibilityData").get("label").textValue();
                        } else {
                            likeStr = "0 Likes";
                        }
                    } else {
                        JsonNode insideLike = likeBtn.get("toggleButtonRenderer").get("defaultText");
                        if (insideLike.has("accessibility")) {
                            likeStr = likeBtn.get("toggleButtonRenderer").get("defaultText").get("accessibility").get("accessibilityData")
                                    .get("label").textValue();
                        } else {
                            likeStr = "0 Likes";
                        }

                    }
                    this.setLikeCount(parseStringToLong(likeStr));

                }
                if (node.get(i).has("itemSectionRenderer")) {
                    JsonNode itemSection = node.get(i).get("itemSectionRenderer").get("contents").get(0);
                    if (itemSection.has("commentsEntryPointHeaderRenderer")) {
                        itemSection = itemSection.get("commentsEntryPointHeaderRenderer");
                        if (itemSection.has("commentCount")) {
                            String cmtCount = itemSection.get("commentCount").get("simpleText").textValue();
                            long cmtc = parseStringToLong(cmtCount);
                            this.setCommentCount(cmtc);
                        }
                    }
                }
            }

    }
}
