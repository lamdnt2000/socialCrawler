package TikTok;

import TikTok.Header.BaseHeader;
import TikTok.Header.CommentHeader;
import TikTok.Header.ReplyCommentHeader;
import TikTok.Model.Comment;
import lombok.Getter;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.WebDriver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static TikTok.CommonUtil.*;
import static TikTok.AESUtil.*;
import static TikTok.CommonUtil.convertObjectToPara;
import static TikTok.TiktokConstants.URL_COMMENT_LIST;
import static TikTok.TiktokConstants.URL_REPLY_LIST;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
        if (apiPath.equals(URL_COMMENT_LIST)) {
            System.out.println(js);
        }
        Object object = new Object();
        try {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } catch (ScriptTimeoutException | JavascriptException e) {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } finally {
            return object;
        }
    }

    public List<String> bulkCommentRequest(int total, int count) throws IllegalAccessException {

        double max = Math.ceil((float)total/500);
        int cursor = 0;
        List<String> params = new ArrayList<>();
        for (int i=0;i<max;i++){
            List<String> list = new ArrayList<>();
            for (int j=0;j<(500/count);j++){
                CommentHeader head = (CommentHeader) this.requestHeader;
                head.setCursor(cursor);
                String param = convertObjectToPara(head);
                String js = String.format(
                        "fetch(\"" + URL_COMMENT_LIST + "?%s\", {\n" +
                                "            \"headers\": {\n" +
                                "                \"accept\": \"*/*\"" +
                                "            },\n" +
                                "            \"referrerPolicy\": \"strict-origin-when-cross-origin\",\n" +
                                "            \"body\": null,\n" +
                                "            \"method\": \"GET\",\n" +
                                "            \"mode\": \"cors\",\n" +
                                "            \"credentials\": \"include\"\n" +
                                "            }).then(response => response.json())" +
                                "    .catch(error => console.warn(error))", param);
                list.add(js);
                cursor+=count;
                if (cursor>=total){
                    break;
                }
            }
            params.add("var data = await Promise.all([" + String.join(",", list) + "]);return data");
        }

        return params ;
    }



    public static Object excuseMultiFetch(String js, WebDriver driver) throws IllegalAccessException {
        Object object = null;
        try {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } catch (ScriptTimeoutException | JavascriptException e) {
            object = ((JavascriptExecutor) driver).executeScript(js);
        } finally {
            return object;
        }
    }

    public static List<String> bulkReplyCommentRequest(int total, ReplyCommentHeader header) throws IllegalAccessException {
        double max = Math.ceil((float)total/500);
        int cursor = 0;
        List<String> params = new ArrayList<>();
        for (int i=0;i<max;i++){
            List<String> list = new ArrayList<>();
            for (int j=0;j<(500/50);j++){
                header.setCursor(cursor);
                String param = convertObjectToPara(header);
                String js = String.format(
                        "fetch(\"" + URL_REPLY_LIST + "?%s\", {\n" +
                                "            \"headers\": {\n" +
                                "                \"accept\": \"*/*\"" +
                                "            },\n" +
                                "            \"referrerPolicy\": \"strict-origin-when-cross-origin\",\n" +
                                "            \"body\": null,\n" +
                                "            \"method\": \"GET\",\n" +
                                "            \"mode\": \"cors\",\n" +
                                "            \"credentials\": \"include\"\n" +
                                "            }).then(response => response.json())" +
                                "    .catch(error => console.warn(error))", param);
                list.add(js);
                cursor+=50;
                if (cursor>=total){
                    break;
                }
            }
            params.addAll(list);
        }

        return params ;
    }

    public static List<String> generateReplyFromComment(List<Comment>  comments, String browserVersion, String videoId) throws IllegalAccessException {
        List<String> parmams = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (int i=0;i<comments.size();i++){
            Comment c = comments.get(i);
            ReplyCommentHeader replyCommentHeader = new ReplyCommentHeader(browserVersion, 0, c.getCid(),videoId);
            parmams.addAll(bulkReplyCommentRequest(c.getReply_comment_total(),replyCommentHeader));
            if (parmams.size()>=50){
                result.add("var data = await Promise.all([" + String.join(",", parmams) + "]);return data");
                parmams.clear();
            }
        }
        return result;
    }


}
