package store.domain.order.vo;

public class OrderLine {
    private final String productName;
    private final Integer quantity;
    private final Integer pricePerProduct;

    private OrderLine(String productName, Integer quantity, Integer pricePerProduct) {
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerProduct = pricePerProduct;
    }

    public static OrderLine of(String productName, Integer quantity, Integer pricePerProduct) {
        return new OrderLine(productName, quantity, pricePerProduct);
    }

    public Integer calculateOrderLinePrice() {
        return quantity * pricePerProduct;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
