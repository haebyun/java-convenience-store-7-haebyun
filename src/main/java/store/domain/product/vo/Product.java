package store.domain.product.vo;

public class Product {
    private final String name;
    private final Price price;
    private final String promotionName;

    public Product(String name, Price price, String promotionName) {
        this.name = name;
        this.price = price;
        this.promotionName = promotionName;
    }

    public static Product of(String name, Integer price, String promotionName) {
        return new Product(name, Price.of(price), promotionName);
    }

    public String getName() {
        return name;
    }

    public Integer getPriceValue() {
        return price.getValue();
    }

    public String getPromotionName() {
        return promotionName;
    }
}
