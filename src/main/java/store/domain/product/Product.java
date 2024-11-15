package store.domain.product;

import store.domain.product.vo.Price;
import store.domain.product.vo.Stock;

public class Product {
    private final String name;
    private final Price price;
    private final Stock stock;
    private final String promotionName;

    private Product(String name, Price price, Stock stock, String promotionName) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotionName = promotionName;
    }

    public static Product from(String name, Integer price, Integer stockQuantity, String promotionName) {
        return new Product(name, Price.of(price), Stock.of(stockQuantity), promotionName);
    }

    public String getName() {
        return name;
    }

    public Integer getPriceValue() {
        return price.getValue();
    }

    public Integer getStockValue() {
        return stock.getQuantity();
    }

    public String getPromotionName() {
        return promotionName;
    }

    public Boolean hasPromotion() {
        return !this.promotionName.equals("No Promotion");
    }

    public Integer decreaseStock(Integer quantity) {
        int availableQuantity = stock.getQuantity();
        int decreaseQuantity = Math.min(availableQuantity, quantity);

        stock.decrease(decreaseQuantity);

        return quantity - decreaseQuantity;
    }
}
