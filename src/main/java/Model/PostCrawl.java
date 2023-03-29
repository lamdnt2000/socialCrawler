package Model;

import com.fasterxml.jackson.annotation.*;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;

public class PostCrawl {
    @JsonProperty("pid")
    @JsonAlias({"id","videoId"})
    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }



    String postCreatedDate;

    @JsonIgnore
    public String getPostCreatedDate() {
        return this.postCreatedDate;
    }
    @JsonSetter("createdTime")
    @JsonAlias({"createTime"})
    public void setPostCreatedDate(String createdTime) {
        this.postCreatedDate = createdTime;

    }

    String pid;

    @JsonProperty("title")
    @JsonAlias({"message","title", "desc"})
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    @JsonProperty("description")
    @JsonAlias({"description"})
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

    @JsonProperty("postType")
    @JsonAlias({"statusType","category"})
    public String getPostType() {
        return this.postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    String postType;

    @JsonProperty("hashtagId")
    @JsonIgnore
    public int getHashtagId() {
        return this.hashtagId;
    }

    public void setHashtagId(int hashtagId) {
        this.hashtagId = hashtagId;
    }

    int hashtagId;

    @JsonProperty("body")
    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    String body;

    @JsonProperty("channelId")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    int channelId;

    @JsonProperty("status")
    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    boolean status = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public ArrayList<Reaction> getReactions() {
        return this.reactions;
    }

    @JsonSetter("stats")
    public void setReactions(ArrayList<Reaction> reactions) {
        this.reactions = reactions;
    }

    ArrayList<Reaction> reactions;

    String createdTime;


    public String getCreatedTime() {
        String time;
        if (postCreatedDate.matches("\\d*")){
            long milesecons = (postCreatedDate.length()<=10)?Long.parseLong(postCreatedDate)*1000:Long.parseLong(postCreatedDate);
            time = new DateTime(milesecons).toString();
        }
        else{
            long timestamp = new DateTime(postCreatedDate).getValue();
            time = new DateTime(timestamp).toString();
        }
        return time.replaceAll("\\+07:00","");
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}

