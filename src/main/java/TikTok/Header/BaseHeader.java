package TikTok.Header;

import TikTok.CommonUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter@Setter@ToString@NoArgsConstructor
public class BaseHeader implements Serializable {
    private String aid = "1988";
    private String app_name="tiktok_web";
    private String channel="tiktok_web";
    private String device_platform="web_mobile";
    private String device_id = CommonUtil.generateDeviceId();
    private String region="VN";
    private String priority_region="";
    private String os="android";
    private boolean cookie_enabled=true;
    private int screen_width=1536;
    private int screen_height=864;
    private String browser_language="en-US";
    private String browser_platform="Win32";
    private boolean browser_online = true;
    private String app_language="en";
    private String webcast_language = "en";
    private String tz_name = "Asia/Bangkok";
    private boolean is_page_visible = true;
    private boolean focus_state = true;
    private boolean is_fullscreen = false;
    private int history_len = 3;
    private int battery_info =1;
    private String from_page = "user";
    private String language = "en";
}
