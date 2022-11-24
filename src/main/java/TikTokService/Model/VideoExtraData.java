package TikTokService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@NoArgsConstructor@ToString
public class VideoExtraData implements Serializable {
    private String awemeId;
    private String hashtagId;
    private String hashtagName;
    private String secUid;
    private String userId;
    private String userUniqueId;
    private int type;
}
