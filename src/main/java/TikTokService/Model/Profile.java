package TikTokService.Model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Profile implements Serializable {
   private String id;
   private String uniqueId;
   private String avatarLarger;
   private String secUid;
   private String nickname;
   private String signature;
   private boolean verified;
   private UserStats stats;
   private List<Video> videos;
   private String region;



}
