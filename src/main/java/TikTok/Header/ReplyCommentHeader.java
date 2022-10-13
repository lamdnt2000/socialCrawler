package TikTok.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReplyCommentHeader extends BaseHeader {
    private String current_region = "JP";
    private int count = 40;
    private int cursor = 0;
    private long comment_id;
    private String item_id;
    private String browser_name = "Mozilla";
    private String browser_version;

    public void init() {
        this.setFrom_page("video");
        this.setApp_language("ja-JP");
    }

    public ReplyCommentHeader(String browser_version, int cursor, long comment_id, String item_id) {
        this.browser_version = browser_version;
        this.cursor = cursor;
        this.comment_id = comment_id;
        this.item_id = item_id;
        init();
    }
}
