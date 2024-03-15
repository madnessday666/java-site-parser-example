package model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.BaseModel;

@Getter
@Setter
public class Product extends BaseModel {

    private String availableIn;

    private String badges;

    private int comments;

    private String deliveryIn;

    private String deliveryTo;

    private int price;

    private double rating;

    @Builder
    public Product(
            String title,
            String url,
            String availableIn,
            String badges,
            int comments,
            String deliveryIn,
            String deliveryTo,
            int price,
            double rating
    ) {
        super(title, url);
        this.availableIn = availableIn;
        this.badges = badges;
        this.comments = comments;
        this.deliveryIn = deliveryIn;
        this.deliveryTo = deliveryTo;
        this.price = price;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Product{" +
                "availableIn='" + availableIn + '\'' +
                ", badges=" + badges +
                ", comments=" + comments +
                ", deliveryIn='" + deliveryIn + '\'' +
                ", deliveryTo='" + deliveryTo + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", title=" + super.getTitle() +
                ", url=" + super.getUrl() +
                '}';
    }
}
