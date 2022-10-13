package Youtube.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString@NoArgsConstructor
public class VideoDetailHeader extends BaseHeader{
    private String videoId;

    public VideoDetailHeader(String userAgent, String videoId) {
        super(userAgent);
        this.videoId = videoId;
    }
}
