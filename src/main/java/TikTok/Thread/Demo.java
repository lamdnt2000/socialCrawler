package TikTok.Thread;

import TikTok.Model.Comment;
import TikTok.Model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Demo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
/*        ExecutorService es = Executors.newFixedThreadPool(5);
        String url = "https://www.tiktok.com/@podaynhe/video/7153115774037593371is_from_webapp=v1&item_id=7153115774037593371";
        Video v = new Video();
        v.setId("7153115774037593371");
        List<Future<List<Comment>>> futures = new ArrayList<>();
        for (int i=0;i<4;i++){
            Future<List<Comment>> future = es.submit(new MultiBroswer("podaynhe", v));
            futures.add(future);
        }

        for (Future<List<Comment>> future: futures){
            System.out.println(future.get());
        }
        es.shutdown();*/
    }
}
