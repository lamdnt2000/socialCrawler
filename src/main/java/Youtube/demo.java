package Youtube;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;

import java.io.IOException;
import java.util.List;


public class demo
{
    public static void main(String[] args) throws IOException {
        YouTube youtube =new YouTube.Builder(new NetHttpTransport(),  new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("YoutubeVideoInfo")
                .setYouTubeRequestInitializer
                        (new YouTubeRequestInitializer("AIzaSyCcKDYR-3A6GWdL1IlXtiugUKdbjH0qvrs")).build();


        YouTube.Activities.List search = youtube.activities().list("contentDetails").setChannelId("UC4L6cAm9LPirrd6Va-8NheQ")
                .setKey("AIzaSyCcKDYR-3A6GWdL1IlXtiugUKdbjH0qvrs").setMaxResults(500l);

        ActivityListResponse response = search.execute();
        List<Activity> searchResultList = response.getItems();
        if (searchResultList != null) {
            for (Activity activity: searchResultList){
                System.out.println(activity);
            }
        }
    }
}
