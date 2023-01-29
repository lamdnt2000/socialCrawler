package TikTokService.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
@Getter@Setter@NoArgsConstructor@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoExtraData implements Serializable {
    private String awemeId;
    private String hashtagId;
    private String hashtagName;
    private String secUid;
    private String userId;
    private String userUniqueId;
    private int type;
}
