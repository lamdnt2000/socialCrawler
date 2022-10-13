package TikTok;

import TikTok.Header.BaseHeader;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.WebDriver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static TikTok.CommonUtil.*;
import static TikTok.AESUtil.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TiktokRequest<T extends BaseHeader> {
    private T requestHeader;

    public TiktokRequest(T requestHeader) {
        this.requestHeader = requestHeader;
    }

    public synchronized Object excuse(WebDriver driver, String apiPath, boolean isTtParam) throws IllegalAccessException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String param = convertObjectToPara(requestHeader);
        String ttParam = (isTtParam) ? "\"x-tt-params\":\"" + encrypt(param) + "\"\n" : "";
        String js = String.format(
                "var data = await fetch(\"" + apiPath + "?%s\", {\n" +
                        "            \"headers\": {\n" +
                        "                \"accept\": \"*/*\",\n" +
                        "                %s" +
                        "            },\n" +
                        "            \"referrerPolicy\": \"strict-origin-when-cross-origin\",\n" +
                        "            \"body\": null,\n" +
                        "            \"method\": \"GET\",\n" +
                        "            \"mode\": \"cors\",\n" +
                        "            \"credentials\": \"include\"\n" +
                        "            }).then(response => response.json())" +
                        "    .catch(error => console.warn(error));" +
                        "return data;", param, ttParam);
        Object object = new Object();
        try {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } catch (ScriptTimeoutException | JavascriptException e) {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } finally {
            return object;
        }
    }
}
