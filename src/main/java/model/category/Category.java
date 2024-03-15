package model.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.BaseModel;


@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseModel {

    @Builder
    public Category(String title, String url) {
        super(title, url);
    }

    @Override
    public String toString() {
        return "Category{" +
                "title=" + super.getTitle() +
                ", url=" + super.getUrl() +
                '}';
    }

}
