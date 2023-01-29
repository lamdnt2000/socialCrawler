package TikTokService.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter@Setter@ToString@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements Serializable {
    private long cid;
    private String text;
    private long create_time;
    private int digg_count;
    private int reply_comment_total;
    private long reply_id;
    private int stick_position;
    private String comment_language="None";
    @JsonProperty("text_extra")
    private List<CommentExtraData> textExtra;
    @JsonProperty("user")
    private User user;
    private boolean is_author_digged;
    private List<Comment> replies;
}
