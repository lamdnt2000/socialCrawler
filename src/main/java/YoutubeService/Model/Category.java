package YoutubeService.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter@Setter@NoArgsConstructor@ToString
public class Category implements Serializable {
    public String name;
    public String api_enum;
    public String id;

    @JsonProperty("fb_page_categories")
    public List<Category> categories;

    @Override
    public int hashCode() {
        return name.hashCode() ^ id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Category))
            return false;

        Category mdc = (Category) obj;
        return mdc.name.equals(name) && mdc.id.equals(id);
    }
}
