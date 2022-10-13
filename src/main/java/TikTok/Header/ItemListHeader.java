package TikTok.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@NoArgsConstructor@ToString
public class ItemListHeader extends BaseHeader {
    private String secUid;
    private int count = 20;
    private long cursor = 0;
    private String userId;
    private String browser_name;

    public ItemListHeader(String browser_name, String secUid, int count, long cursor, String userId) {
        this.browser_name = browser_name;
        this.secUid = secUid;
        this.count = count;
        this.cursor = cursor;
        this.userId = userId;
    }

    public ItemListHeader(String browser_name, String secUid, long cursor, String userId) {
        this.browser_name = browser_name;
        this.secUid = secUid;
        this.cursor = cursor;
        this.userId = userId;
    }

}


