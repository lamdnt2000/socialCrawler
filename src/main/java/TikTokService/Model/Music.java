package TikTokService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@NoArgsConstructor@Getter@Setter@ToString
public class Music implements Serializable {
    private String musicId;
    private String musicUrl;
    private int post;
}
