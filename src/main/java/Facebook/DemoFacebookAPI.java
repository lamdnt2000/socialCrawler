package Facebook;

import Facebook.Model.ModPage;
import Facebook.Model.ModPost;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.*;
import com.restfb.types.*;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Facebook.Constants.FacebookConstants.*;

public class DemoFacebookAPI {
    public static String ACCESS_TOKEN = "EAAGNO4a7r2wBAIZCTCYZB5qXj1JNra7YQ89zZC56OSrMDF7TcGa8MpZCy7xGyrRZCBmss1ifD8O6tPUK7LjZBK8GLmlxK7IhNkIJLZBdtej4mueJIvNEZB9BvULxPQFOLV6R1ZCTsHV2hStEcu5F3j1xOQYz6ZBXxC79C5ZA11VDj3CthZCfg07E0OvOUq52oxG7ZAoEZD";
    public static String COOKIE = "c_user=100004437857217;xs=24%3A2VnbvPTYFcDqSA%3A2%3A1661067140%3A-1%3A6154%3A%3AAcXue_A4VRq6noO9_-AdHPpf5QLUGq6i9yURhd2IeJ7I";
    public static void main(String[] args) throws IOException, IllegalAccessException {
        ModWebRequestor modWebRequestor = new ModWebRequestor(COOKIE);
        FacebookClient client = new DefaultFacebookClient(ACCESS_TOKEN,modWebRequestor, new DefaultJsonMapper(),Version.VERSION_4_0);
        ModPage page = client.fetchObject("1306924079365589",ModPage.class,Parameter.with("fields",FACEBOOK_FETCH_PAGE_PARAM));

        Connection<ModPost> post = client.fetchConnection("1306924079365589/"+FACEBOOK_FETCH_POST,ModPost.class);
        Iterator<List<ModPost>> result = post.iterator();
        List<ModPost> postList = result.next();

        for (int i=0;i< postList.size();i++){
            ModPost modPost = postList.get(i);
            Connection<Comment> commentsConnection = client.fetchConnection(modPost.getId() + "/" + FACEBOOK_FETCH_COMMENT, Comment.class, Parameter.with("limit", FACEBOOK_MAX_LIMIT_COMMENT));
            Iterator<List<Comment>> next = commentsConnection.iterator();
            List<Comment> commentList = new ArrayList<>();
            while (next.hasNext()) {
                List<Comment> comments = next.next();
                // This is the same functionality as the example above
                commentList.addAll(comments);
                if (!commentsConnection.hasNext()) {
                    break;
                }
            }
            Comments pComments = modPost.getComments();
            FieldUtils.writeField(pComments,"data",commentList,true);

        }
        page.setPostList(postList);
        ObjectMapper obj = new ObjectMapper();
        obj.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        FileWriter file = new FileWriter("output.json");
        file.write(obj.writeValueAsString(page));
        file.close();
        /*Connection<Post> posts = client.fetchConnection("RioXmusic/"+FACEBOOK_FETCH_PAGE, Post.class, Parameter.with("limit",FACEBOOK_MAX_LIMIT_POST));
        Iterator<List<Post>> it = posts.iterator();
        int totalPost = 0;
        List<Post> postIds = new ArrayList<>();
        while (it.hasNext()){
            List<Post> myfeed = it.next();
           postIds.addAll(myfeed);
            System.out.println(postIds.size());
            if (!posts.hasNext()){
                break;
            }
        }
        for (Post p: postIds){
            System.out.println(p.getId());
        }
        List<String> ids = readIds();
        for (int i=0;i<1;i++){
            List<String> idsTen = ids.subList(i*10,(i+1)*10);
            JsonObject fetchObjectsResults = client.fetchObjects(idsTen, JsonObject.class, Parameter.with("fields","comments.limit(500)"));
            System.out.println(fetchObjectsResults.toString());
        }*/


    }
}
