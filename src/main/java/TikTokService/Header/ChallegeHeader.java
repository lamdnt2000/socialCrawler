package TikTokService.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@NoArgsConstructor@ToString
public class ChallegeHeader extends BaseHeader implements Serializable  {
    private int discoverType = 0;
    private String keyWord;
    private int offset;
    private String browser_version;
    private String browser_name="Mozilla";
    private boolean needItemList = false;
    private int count = 20;
    public ChallegeHeader(String browser_version, String keyWord, int offset) {
        this.browser_version = browser_version;
        this.keyWord = keyWord;
        this.offset = offset;
        init();
    }

    public ChallegeHeader(String browser_version, int offset) {
        init();
        this.offset = offset;
        this.browser_version = browser_version;
    }

    public void init(){
        this.setFrom_page("discover");
        this.setPriority_region("VN");
    }
}
