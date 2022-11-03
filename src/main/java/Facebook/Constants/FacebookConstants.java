package Facebook.Constants;

public class FacebookConstants {
    public static final String FACEBOOK_FETCH_PAGE_PARAM = "name,category_list,category,about,asset_score,link,name_with_location_descriptor,page_created_time,username,picture{url},cover";
    public static final String FACEBOOK_FETCH_POST = "posts?fields=comments.summary(total_count).limit(0){created_time,from,message},message,created_time,reactions.summary(total_count),reactions.type(CARE).limit(0).summary(total_count).as(reaction_care),reactions.type(LIKE).limit(0).summary(total_count).as(reaction_like),reactions.type(LOVE).limit(0).summary(total_count).as(reaction_love),reactions.type(WOW).limit(0).summary(total_count).as(reaction_wow),reactions.type(HAHA).limit(0).summary(total_count).as(reaction_haha),reactions.type(SAD).limit(0).summary(total_count).as(reaction_sad),reactions.type(ANGRY).limit(0).summary(total_count).as(reaction_angry)";
    public static final int FACEBOOK_MAX_LIMIT_POST = 100;
    public static final String ACCESS_TOKEN = "EAABsbCS1iHgBAApg1bD6uAD48r4AJk6dt8THPLVJoByXU2aok9tLKyUsu5d0mS7t50msXLOU29fkXE3P8TCceNY1Um7tsXv2EsudX3nZBrKDL2MaTAC7z3ZBhlQQ68Al54dAiZB7Q9sPm6IwTlw9ZAjZBnZAqzaK0u50DQL2xgkAZDZD";
    public static final String COOKIE = "c_user=100004437857217;xs=1%3A_fZxa0MTsLDylQ%3A2%3A1665643055%3A-1%3A6154%3A%3AAcVeifPoMQHQT47w7-NjZ9jp3gDT6SavUJlCIDLFWHBC;|Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";


}
