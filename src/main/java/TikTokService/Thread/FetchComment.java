package TikTokService.Thread;

import TikTokService.Model.Comment;
import TikTokService.Model.Video;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.Callable;

import static TikTokService.TikTokAPI.getCommentsByVideo;

public class FetchComment implements Callable<Video> {
    private String username;
    private Video v;
    private WebDriver driver;

    public FetchComment(String username, Video v, WebDriver driver) {
        this.username = username;
        this.v = v;
        this.driver = driver;
    }

    @Override
    public Video call() throws Exception {
        List<Comment> comments = getCommentsByVideo(username,v,driver);
        v.setComments(comments);
        return v;

    }


}
