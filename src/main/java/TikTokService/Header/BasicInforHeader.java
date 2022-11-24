package TikTokService.Header;

public class BasicInforHeader extends BaseHeader{
    private String browser_version;
    private String secUid;
    private String uniqueId;

    public BasicInforHeader(String browser_version, String secUid, String uniqueId) {
        this.browser_version = browser_version;
        this.secUid = secUid;
        this.uniqueId = uniqueId;
    }

    public String getSecUid() {
        return secUid;
    }

    public void setSecUid(String secUid) {
        this.secUid = secUid;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getBrowser_version() {
        return browser_version;
    }

    public void setBrowser_version(String browser_version) {
        this.browser_version = browser_version;
    }
}
