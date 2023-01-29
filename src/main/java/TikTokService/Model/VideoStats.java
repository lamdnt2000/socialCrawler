package TikTokService.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
@Getter@Setter@NoArgsConstructor@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoStats implements Serializable {

    private long commentCount;
    private long diggCount;
    private long playCount;
    private long shareCount;
}
