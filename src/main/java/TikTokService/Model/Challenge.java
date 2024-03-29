package TikTokService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@ToString@NoArgsConstructor
public class Challenge implements Serializable {
    private String challengeId;
    private String challengeName;
    private long views;
}
