package Youtube.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static Youtube.Util.CommonUtil.parseUrlGetVideoId;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Video implements Serializable {
    private String title;
    private String description;
    private String videoId;
    private String createTime;
    private String updateTime;
    private long viewCount;
    @JsonUnwrapped
    private Reaction reaction;
    private String category;

    @JsonProperty("playerMicroformatRenderer")
    private void unpackNested(JsonNode videos) {
        try {
            this.category = videos.get("category").textValue();
            this.title = videos.get("title").get("simpleText").textValue();
            this.description = videos.get("description").get("simpleText").textValue();
            this.createTime = videos.get("publishDate").textValue();
            this.updateTime = videos.get("uploadDate").textValue();
            this.viewCount = Long.parseLong(videos.get("viewCount").textValue());
        }
        catch (Exception e){
            System.out.println(videos.toString());
        }
    }

}
