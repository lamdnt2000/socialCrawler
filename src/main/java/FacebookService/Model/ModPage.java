package FacebookService.Model;

import Model.ChannelCategory;
import com.restfb.Facebook;
import com.restfb.types.Category;
import com.restfb.types.Page;

import java.util.ArrayList;
import java.util.List;

public class ModPage extends Page {
    @Facebook("about")
    private String aboutPage;
    @Facebook("followers_count")
    private Long followerCount;
    @Facebook("page_created_time")
    private String createdDate;
    private List<ModPost> postList;
    public List<ModPost> getPostList() {
        return postList;
    }
    public void setPostList(List<ModPost> postList) {
        this.postList = postList;
    }

    public String getAboutPage() {
        return aboutPage;
    }

    public void setAboutPage(String aboutPage) {
        this.aboutPage = aboutPage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    private String profileUrl;
    private String coverUrl;
    private List<ChannelCategory> categoyIds;
    public String getProfileUrl() {
        return getPicture().getUrl();
    }

    public String getCoverUrl() {
        return getCover().getSource();
    }

    public List<ChannelCategory> getCategoyIds() {
        List<ChannelCategory> ids = new ArrayList<>();
        for (Category c: getCategoryList()){
            ChannelCategory category = new ChannelCategory();
            category.setCategoryId(Long.parseLong(c.getId()));
            ids.add(category);
        }
        return ids;
    }

    public Long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Long followerCount) {
        this.followerCount = followerCount;
    }
}
