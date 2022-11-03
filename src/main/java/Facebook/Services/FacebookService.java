package Facebook.Services;

import Facebook.Constants.FacebookConstants;
import Facebook.ModWebRequestor;
import Facebook.Model.ModPage;
import Facebook.Model.ModPost;
import com.restfb.*;
import com.restfb.types.Page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Facebook.Constants.FacebookConstants.*;

public class FacebookService {
    public static FacebookClient client;

    public static ModPage fetchFbPage(String uid) {
        return client.fetchObject(uid, ModPage.class, Parameter.with("fields", FACEBOOK_FETCH_PAGE_PARAM));
    }

    public FacebookService() {
        ModWebRequestor modWebRequestor = new ModWebRequestor(COOKIE);
        client = new DefaultFacebookClient(ACCESS_TOKEN, modWebRequestor, new DefaultJsonMapper(), Version.VERSION_4_0);

    }

    public static ModPage fetchPostInPage(String uid){
        ModPage page = fetchFbPage(uid);
        Connection<ModPost> posts = client.fetchConnection(uid + "/" + FACEBOOK_FETCH_POST, ModPost.class, Parameter.with("limit", FACEBOOK_MAX_LIMIT_POST));
        Iterator<List<ModPost>> it = posts.iterator();
        List<ModPost> postIds = new ArrayList<>();
        while (it.hasNext()) {
            List<ModPost> myfeed = it.next();
            postIds.addAll(myfeed);
            System.out.println(postIds.size());
            if (!posts.hasNext()) {
                break;
            }
        }
        page.setPostList(postIds);
        return page;
    }
}
