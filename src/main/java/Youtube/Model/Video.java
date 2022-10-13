package Youtube.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter@Setter@ToString@NoArgsConstructor
public class Video implements Serializable {
    private String title;
    private String description;
    private String videoId;
    private long commentCount=0;
    private String createTime;
    private List<String> hashTags;
    private long viewCount;
    private long likeCount;
    private List<Comment> comments;
    private String continueComment;
    @JsonProperty("gridVideoRenderer")
    private void unpackNested(JsonNode videos) {
        this.videoId = videos.get("videoId").textValue();
    }
}
