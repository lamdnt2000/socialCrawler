package FacebookService.Services;

import FacebookService.ModWebRequestor;
import FacebookService.Model.ModPage;
import FacebookService.Model.ModPost;
import com.restfb.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static FacebookService.Constants.FacebookConstants.*;

public class FacebookService {
    public static FacebookClient client;

    public static ModPage fetchFbPage(String uid) {

        return client.fetchObject(uid, ModPage.class, Parameter.with("fields", FACEBOOK_FETCH_PAGE_PARAM));
    }

    public FacebookService() {
        ModWebRequestor modWebRequestor = new ModWebRequestor(COOKIE);
        client = new DefaultFacebookClient(ACCESS_TOKEN, modWebRequestor, new DefaultJsonMapper(), Version.VERSION_4_0);

    }

    public ModPage fetchPostInPage(String uid){
            ModPage page = fetchFbPage(uid);

        Connection<ModPost> posts = client.fetchConnection(uid + "/" + FACEBOOK_FETCH_POST, ModPost.class, Parameter.with("limit", FACEBOOK_MAX_LIMIT_POST));
        Iterator<List<ModPost>> it = posts.iterator();
        List<ModPost> postIds = new ArrayList<>();
        while (it.hasNext()) {
            List<ModPost> myfeed= new ArrayList<>();
            try {
                 myfeed = it.next();
            }
            catch (Exception e){
                System.out.println(e);
            }
            postIds.addAll(myfeed);
           /* System.out.println(postIds.size());*/
            if (!posts.hasNext()) {
                break;
            }
        }
        page.setPostList(postIds);
        return page;
    }
}
