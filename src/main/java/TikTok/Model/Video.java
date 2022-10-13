package TikTok.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter@Setter@ToString@NoArgsConstructor
public class Video implements Serializable {
    private String desc;
    private long createTime;
    private String id;
    @JsonProperty("video")
    private VideoExtra video;
    @JsonProperty("stats")
    private VideoStats stats;
    @JsonProperty("textExtra")
    private List<VideoExtraData> hashtags;
    private List<Comment> comments;
    private List<String> categories;
    private boolean secret;
}


