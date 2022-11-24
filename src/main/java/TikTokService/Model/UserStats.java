package TikTokService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@NoArgsConstructor@ToString
public class UserStats {
    private long followerCount;
    private long followingCount;
    private long heartCount;
    private long videoCount;



}
