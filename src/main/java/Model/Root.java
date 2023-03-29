package Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.ArrayList;
@ToString

public class Root {
    @JsonProperty("locationId")
    public int getLocationId() {
        return this.locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    String createdTime;

    @JsonProperty("createdTime")
    @JsonAlias({"createdDate","published"})
    public String getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }


    int locationId;

    @JsonProperty("platformId")
    public int getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    int platformId;

    @JsonProperty("brandId")
    public int getBrandId() {
        return this.brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    int brandId;


    @JsonProperty("botId")
    public int getBotId() {
        return this.botId;
    }

    public void setBotId(int botId) {
        this.botId = botId;
    }

    int botId;

    @JsonProperty("name")
    @JsonAlias({"name","title","nickname"})
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    @JsonProperty("url")
    @JsonAlias({"link"})
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    @JsonProperty("avatarUrl")
    @JsonAlias({"profileUrl","thumbnail","avatarThumb"})
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    String avatarUrl;

    @JsonProperty("bannerUrl")
    @JsonAlias({"coverUrl","banner"})
    public String getBannerUrl() {
        return this.bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    String bannerUrl;

    @JsonProperty("status")
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int status;

    @JsonProperty("isVerify")
    public boolean getIsVerify() {
        return this.isVerify;
    }

    public void setIsVerify(boolean isVerify) {
        this.isVerify = isVerify;
    }

    boolean isVerify;

    @JsonProperty("bio")
    @JsonAlias({"about","description","signature"})
    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    String bio;

    @JsonProperty("username")
    @JsonAlias("uniqueId")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;

    @JsonProperty("cid")
    @JsonAlias({"id","channelId"})
    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    String cid;

    @JsonProperty("channelCategories")
    @JsonAlias({"categoyIds"})
    public ArrayList<ChannelCategory> getChannelCategories() {
        return this.channelCategories;
    }

    public void setChannelCategories(ArrayList<ChannelCategory> channelCategories) {
        this.channelCategories = channelCategories;
    }

    ArrayList<ChannelCategory> channelCategories;



    ChannelRecords channelRecord;

    @JsonProperty("postCrawls")
    @JsonAlias({"postList","videos"})
    public ArrayList<PostCrawl> getPostCrawls() {
        return this.postCrawls;
    }

    public void setPostCrawls(ArrayList<PostCrawl> postCrawls) {
        this.postCrawls = postCrawls;
    }

    ArrayList<PostCrawl> postCrawls;

    int organizationId=35;
    @JsonProperty("verified")
    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }
    @JsonProperty("channelRecord")
    @JsonAlias("stats")
    public ChannelRecords getChannelRecord() {
        return channelRecord;
    }

    public void setChannelRecord(ChannelRecords channelRecord) {
        this.channelRecord = channelRecord;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

}
