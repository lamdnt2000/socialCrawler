package FacebookService.Constants;

public class FacebookConstants {
    public static final String FACEBOOK_FETCH_PAGE_PARAM = "name,category_list,category,about,link,name_with_location_descriptor,page_created_time,username,picture.type(large),cover,followers_count,verification_status";
    public static final String FACEBOOK_FETCH_POST = "posts?fields=comments.summary(total_count).limit(0){created_time,from,message},message,created_time,reactions.summary(total_count),reactions.type(CARE).limit(0).summary(total_count).as(reaction_care),reactions.type(LIKE).limit(0).summary(total_count).as(reaction_like),reactions.type(LOVE).limit(0).summary(total_count).as(reaction_love),reactions.type(WOW).limit(0).summary(total_count).as(reaction_wow),reactions.type(HAHA).limit(0).summary(total_count).as(reaction_haha),reactions.type(SAD).limit(0).summary(total_count).as(reaction_sad),reactions.type(ANGRY).limit(0).summary(total_count).as(reaction_angry),shares,status_type";
    public static final int FACEBOOK_MAX_LIMIT_POST = 100;
    public static  String ACCESS_TOKEN = "EAABsbCS1iHgBAJhQRf9KCnxwdlhya7uMJhaBaVmnnUpdsXc8BheLiNXvkZCoNxZAZC2mr96WcAvzagQZAvd85SbvkouJmvdFXbeG78eHSNWjiUqSEZAhub826G289nyn6f8dUiK4iZCrVvL8eeGeRlZBeyIaMlbVK7CLZBTUirVj7AIXg7WafvsmSHPFZB9S0xBQZD";
    public static String COOKIE = "c_user=100004437857217;xs=1%3A_fZxa0MTsLDylQ%3A2%3A1665643055%3A-1%3A6154%3A%3AAcXuZlKvb88mCMohaG2OdhXFXBhhfmDP0FDu5roq9Cta;";
}
