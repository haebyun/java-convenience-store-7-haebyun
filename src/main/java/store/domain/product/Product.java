package store.domain.product;

public class Product {
    private final String name;
    private final Integer price;
    private final String promotionName;

    public Product(String name, Integer price, String promotionName) {
        this.name = name;
        this.price = price;
        this.promotionName = promotionName;
    }

    public static Product of(String name, Integer price, String promotionName) {
        return new Product(name, price, promotionName);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getPromotionName() {
        return promotionName;
    }
}
