package store.domain.order.vo;

public record OrderResult(
        String productName,
        Integer quantity
) {
    public static OrderResult of(String productName, Integer quantity) {
        return new OrderResult(productName, quantity);
    }
}
