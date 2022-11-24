package TikTokService.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter@Setter@NoArgsConstructor@ToString
public class ChallengeSearch implements Serializable {
    private String coverThumb;
    private String desc;
    private String id;
    private String profileThumb;
    private String title;
    private long videoCount;
    private long viewCount;
    private List<Video> items;
    private long commentCount;
    private long shareCount;
    private long heartCount;
    @SuppressWarnings("unchecked")
    @JsonProperty("challenge")
    private void unpackNested(Map<String,Object> challenge) {
        this.coverThumb = (String)challenge.get("coverThumb");
        this.desc = (String)challenge.get("desc");
        this.id = (String)challenge.get("id");
        this.profileThumb = (String)challenge.get("profileThumb");
        this.title = (String)challenge.get("title");
        Map<String, Object> stats = (Map<String, Object>) challenge.get("stats");
        this.videoCount = ((Number)stats.get("videoCount")).longValue();
        this.viewCount =((Number)stats.get("viewCount")).longValue();
    }

}
