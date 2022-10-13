package TikTok.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@ToString@NoArgsConstructor
public class VideoExtra implements Serializable {
    private String cover;
    private int height;
    private int width;
    private int duration;
}
