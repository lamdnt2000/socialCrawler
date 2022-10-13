package TikTok.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@NoArgsConstructor@ToString
public class ChallengeItemsHeader extends BaseHeader implements Serializable {
    private int count = 30;
    private int cursor = 0;
    private String browser_version;
    private String challengeID;
    public ChallengeItemsHeader(String challengeID, int count, int cursor, String browser_version) {
        this.count = count;
        this.cursor = cursor;
        this.browser_version = browser_version;
        this.challengeID = challengeID;
        init();
    }

    public void init(){
        this.setFrom_page("hashtag");
    }
}
