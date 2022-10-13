package Youtube;

import Youtube.Header.BaseHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static TikTok.CommonUtil.convertObjectToPara;
import static Youtube.Util.CommonUtil.convertObjectToJson;

public class YoutubeRequest<T extends BaseHeader> {
    private T requestHeader;

    public YoutubeRequest(T requestHeader) {
        this.requestHeader = requestHeader;
    }

    public synchronized Object excuse(WebDriver driver, String apiPath) throws IllegalAccessException, JsonProcessingException {
        String param = convertObjectToJson(requestHeader);
        String js = String.format(
                "var data = await fetch(\"%s\", {\n" +
                        "            \"headers\": {\n" +
                        "                \"accept\": \"*/*\",\n" +
                        "                \"Content-Type\": \"application/json\",\n" +
                        "            },\n" +
                        "            \"body\": '%s',\n" +
                        "            \"method\": \"POST\",\n" +
                        "            \"redirect\": \"follow\"\n" +
                        "            }).then(response => response.json())"+
                        "    .catch(error => console.warn(error));" +
                        "return data;",apiPath,param);
        return ((JavascriptExecutor)driver).executeScript(js);
    }
}
