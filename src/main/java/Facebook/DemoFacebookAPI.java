package Facebook;

import Facebook.Model.ModPage;
import Facebook.Model.ModPost;
import com.restfb.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Facebook.Constants.FacebookConstants.*;
import static Util.FileUtils.readUsers;
import static Util.FileUtils.saveData;

public class DemoFacebookAPI {
    public static String ACCESS_TOKEN = "EAAGNO4a7r2wBAGy2EpCZC1fgHKknoDmamu3OwDOwrpFfeKOy6OzrThyjKcatR7EhusStYAcHSufwdH7FKVsIgqJpjsvB33kH6dZAX7BXbmjCtZCBo83WpMEVJy3YhZCTXuseVZCOXJzYOBXuSQZCbVKBZCsZB5PX2IXgY4b8JvCm6juMj8EQMv4hVMGT7aAP9WIZD";
    public static String COOKIE = "c_user=100004437857217;xs=1%3A_fZxa0MTsLDylQ%3A2%3A1665643055%3A-1%3A6154%3A%3AAcUdnFhUxspQJvIcTtPzVwyFQ3bi90uy26OLsaJhOAgv;";

    public static void main(String[] args) throws IOException, IllegalAccessException {
        ModWebRequestor modWebRequestor = new ModWebRequestor(COOKIE);
        FacebookClient client = new DefaultFacebookClient(ACCESS_TOKEN, modWebRequestor, new DefaultJsonMapper(), Version.VERSION_4_0);
        List<String> ids = readUsers();
        for (String id : ids) {
            try{
                String pageId = id;
                ModPage page = client.fetchObject(pageId, ModPage.class, Parameter.with("fields", FACEBOOK_FETCH_PAGE_PARAM));

                Connection<ModPost> posts = client.fetchConnection(pageId + "/" + FACEBOOK_FETCH_POST, ModPost.class, Parameter.with("limit", FACEBOOK_MAX_LIMIT_POST));
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
                saveData("C:\\tool\\FacebookAPI\\facebook\\",id, page);

            }
            catch (Exception e){
                System.out.println(id);
            }

        }


    }
}
