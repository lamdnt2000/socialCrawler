package YoutubeService.Header;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter@Setter@NoArgsConstructor
public class BaseHeader implements Serializable {
    private String context = "{" +
            "        \"client\": {" +
            "            \"userAgent\": \"%s\"," +
            "            \"clientName\": \"WEB\"," +
            "            \"clientVersion\": \"2.20220909.00.00\"" +
            "        }" +
            "    }";
    public BaseHeader(String userAgent) {
        context = String.format(context,userAgent).replaceAll("\\s","");
    }
}
