package FacebookService.Constants;

public class FacebookConstants {
    public static final String FACEBOOK_FETCH_PAGE_PARAM = "name,category_list,category,about,link,name_with_location_descriptor,page_created_time,username,picture.type(large),cover,followers_count,verification_status";
    public static final String FACEBOOK_FETCH_POST = "posts?fields=comments.summary(total_count).limit(0){created_time,from,message},message,created_time,reactions.summary(total_count),reactions.type(CARE).limit(0).summary(total_count).as(reaction_care),reactions.type(LIKE).limit(0).summary(total_count).as(reaction_like),reactions.type(LOVE).limit(0).summary(total_count).as(reaction_love),reactions.type(WOW).limit(0).summary(total_count).as(reaction_wow),reactions.type(HAHA).limit(0).summary(total_count).as(reaction_haha),reactions.type(SAD).limit(0).summary(total_count).as(reaction_sad),reactions.type(ANGRY).limit(0).summary(total_count).as(reaction_angry),shares,status_type";
    public static final int FACEBOOK_MAX_LIMIT_POST = 100;
    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";
}
