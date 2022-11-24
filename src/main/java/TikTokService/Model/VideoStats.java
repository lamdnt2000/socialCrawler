package TikTokService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@NoArgsConstructor@ToString
public class VideoStats implements Serializable {

    private long commentCount;
    private long diggCount;
    private long playCount;
    private long shareCount;
}
