package TikTokService.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@NoArgsConstructor@ToString
public class CommentHeader extends BaseHeader{
    private String browser_version;
    private String current_region ="JP";
    private int fromWeb = 1;
    private String aweme_id;
    private String browser_name="Mozilla";
    private int count = 25;
    private int cursor = 0;

    public CommentHeader(String browser_version, String aweme_id) {
        this.browser_version = browser_version;
        this.aweme_id = aweme_id;
        setNewInitParam();
    }

    public CommentHeader(String browser_version, String aweme_id, int cursor) {
        this.browser_version = browser_version;
        this.aweme_id = aweme_id;
        this.cursor = cursor;
        setNewInitParam();
    }

    public void setNewInitParam(){
        this.setApp_language("ja-JP");
        this.setFrom_page("video");
        this.setHistory_len(4);
        this.setOs("linux");
    }

}
