package TikTokService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter@Setter@NoArgsConstructor@ToString
public class User implements Serializable {
    private String uid;
    private String sec_uid;
    private String nickname;
    private String unique_id;

}
