package store.domain.order.vo;

public class OrderLine {
    private final String productName;
    private final Integer quantity;
    private final Integer pricePerProduct;

    public OrderLine(String productName, Integer quantity, Integer pricePerProduct) {
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerProduct = pricePerProduct;
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
