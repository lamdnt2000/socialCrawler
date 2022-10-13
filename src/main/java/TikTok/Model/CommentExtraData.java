package TikTok.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter@Setter@NoArgsConstructor@ToString
public class CommentExtraData implements Serializable {
    private int end;
    private int start;
    private String hashtag_id;
    private String hashtag_name;
    private String sec_uid;
    private String userUniqueId;
    private String user_id;


}
