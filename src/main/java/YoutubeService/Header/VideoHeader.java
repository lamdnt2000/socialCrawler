package YoutubeService.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString@NoArgsConstructor
public class VideoHeader extends BaseHeader{
    private String continuation;

    public VideoHeader(String userAgent, String continuation) {
        super(userAgent);
        this.continuation = continuation;
    }
}
