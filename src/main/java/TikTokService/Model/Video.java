package TikTokService.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter@Setter@ToString
@JsonIgnoreProperties(ignoreUnknown = true)

public class Video implements Serializable {
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("createTime")
    private long createTime;
    @JsonProperty("id")
    private String id;
    @JsonProperty("video")
    private VideoExtra video;
    @JsonProperty("stats")
    private VideoStats stats;
    @JsonProperty("textExtra")
    private List<VideoExtraData> hashtags;
    @JsonProperty("comments")
    private List<Comment> comments;
    @JsonProperty("categories")
    private List<String> categories;
    @JsonProperty("secret")
    private boolean secret;

}


