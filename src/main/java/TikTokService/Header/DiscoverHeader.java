package TikTokService.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@NoArgsConstructor@ToString
public class DiscoverHeader extends BaseHeader implements Serializable {
    String browser_version;
    int count = 30;
    String from_page ="fyp";
    int noUser = 0;

    public DiscoverHeader(String browser_version) {
        this.browser_version = browser_version;
    }
}
