package TikTok.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@ToString@NoArgsConstructor
public class UserDiscover implements Serializable {
    private int digg;
    private int fans;
    private int following;
    private int heart;
    private int likes;
    private String secUid;
    private String userId;
    private int video;
    private boolean verified;
}
