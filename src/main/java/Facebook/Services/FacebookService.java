package Facebook.Services;

import Facebook.Constants.FacebookConstants;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;

public class FacebookService {
    public static Page fetchFbPageWithParam(FacebookClient client, String params){
        return client.fetchObject("922026077907319", Page.class, Parameter.withFields(FacebookConstants.FACEBOOK_FETCH_PAGE_PARAM));

    }
}
