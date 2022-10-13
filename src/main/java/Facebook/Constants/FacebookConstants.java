package Facebook.Constants;

public class FacebookConstants {
    public static final String FACEBOOK_FETCH_PAGE_PARAM = "name,category_list,category,about,asset_score,link,name_with_location_descriptor,page_created_time,username,picture{url,__type__},cover";
    public static final String FACEBOOK_FETCH_POST = "posts?fields=comments.summary(total_count).limit(0){created_time,from,message},message,created_time,reactions.summary(total_count),reactions.type(CARE).limit(0).summary(total_count).as(reaction_care),reactions.type(LIKE).limit(0).summary(total_count).as(reaction_like),reactions.type(LOVE).limit(0).summary(total_count).as(reaction_love),reactions.type(WOW).limit(0).summary(total_count).as(reaction_wow),reactions.type(HAHA).limit(0).summary(total_count).as(reaction_haha),reactions.type(SAD).limit(0).summary(total_count).as(reaction_sad),reactions.type(ANGRY).limit(0).summary(total_count).as(reaction_angry)";
    public static final String FACEBOOK_FETCH_COMMENT = "comments?fields=created_time,from,message,like_count,parent&filter=stream";
    public static final String FACEBOOK_FETCH_COMMENT2 = "created_time,from,message,like_count,parent&filter=stream&limit=500";
    public static final int FACEBOOK_MAX_LIMIT_POST = 100;
    public static final int FACEBOOK_MAX_LIMIT_COMMENT = 500;

}
