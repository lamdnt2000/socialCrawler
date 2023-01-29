package YoutubeService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private String thumbnail;
    private String userType;
    private String banner;
    private String channelId;
    private String browseId;
    private String subscriber;
    private String title;
    private List<String> tags;
    private String description;
    private String username;
    private String published;
    private String countryCode;
    private List<Video> videos;
    public User(String thumbnail, String userType, String banner, String channelId, String browseId, String subscriber, String title, String published) {
        this.thumbnail = thumbnail;
        this.userType = userType;
        this.banner = banner;
        this.channelId = channelId;
        this.browseId = browseId;
        this.subscriber = subscriber;
        this.title = title;
        this.published = published;
    }
}
