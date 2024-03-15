package model.region;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class Region extends BaseModel {

    @Builder
    public Region(String title, String url) {
        super(title, url);
    }

    @Override
    public String toString() {
        return "Region{" +
                "title=" + super.getTitle() +
                ", url=" + super.getUrl() +
                "}";
    }

}
