package Facebook.Model;

import com.restfb.Facebook;
import com.restfb.types.Page;

import java.util.List;

public class ModPage extends Page {
    private List<ModPost> postList;
    public List<ModPost> getPostList() {
        return postList;
    }
    public void setPostList(List<ModPost> postList) {
        this.postList = postList;
    }
}
