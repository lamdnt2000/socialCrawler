package TikTokService.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Getter@Setter@ToString@NoArgsConstructor
public class Discover<T> implements Serializable {
    private String cover;
    private String description;
    private String id;
    private String link;
    private String subTitle;
    private String title;
    private DiscorverEnumType type;
    @JsonProperty("extraInfo")
    private T extraInfo;

    @SuppressWarnings("unchecked")
    @JsonProperty("cardItem")
    private void unpackNested(Map<String,Object> cardItem) {
        this.cover = (String)cardItem.get("cover");
        this.description = (String)cardItem.get("description");
        this.id = (String)cardItem.get("id");
        this.link = (String)cardItem.get("link");
        this.subTitle = (String)cardItem.get("subTitle");
        this.title = (String)cardItem.get("title");
        this.type = DiscorverEnumType.values()[(int)cardItem.get("type")-1];
        this.extraInfo =(T) cardItem.get("extraInfo");
    }
}
