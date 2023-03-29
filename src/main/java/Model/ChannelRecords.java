package Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelRecords {
    @JsonProperty("totalFollower")
    @JsonAlias("followerCount")
    public Long getTotalFollower() {
        return this.totalFollower;
    }

    public void setTotalFollower(Long totalFollower) {
        this.totalFollower = totalFollower;
    }

    Long totalFollower;

    @JsonProperty("totalLike")
    @JsonAlias("heartCount")
    public Long getTotalLike() {
        return this.totalLike;
    }

    public void setTotalLike(Long totalLike) {
        this.totalLike = totalLike;
    }

    Long totalLike;

    @JsonProperty("totalShare")
    public Long getTotalShare() {
        return this.totalShare;
    }

    public void setTotalShare(Long totalShare) {
        this.totalShare = totalShare;
    }

    Long totalShare;

    @JsonProperty("totalComment")
    public Long getTotalComment() {
        return this.totalComment;
    }

    public void setTotalComment(Long totalComment) {
        this.totalComment = totalComment;
    }

    Long totalComment;

    @JsonProperty("totalPost")
    @JsonAlias("videoCount")
    public int getTotalPost() {
        return this.totalPost;
    }

    public void setTotalPost(int totalPost) {
        this.totalPost = totalPost;
    }

    int totalPost;

    @JsonProperty("totalView")
    public Long getTotalView() {
        return this.totalView;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    Long totalView;


    @JsonProperty("status")
    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    boolean status = true;

    private int channelId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
}
