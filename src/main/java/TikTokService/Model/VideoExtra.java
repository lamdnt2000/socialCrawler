package TikTokService.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
@Getter@Setter@ToString@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoExtra implements Serializable {
    private String cover;
    private int height;
    private int width;
    private int duration;
}
