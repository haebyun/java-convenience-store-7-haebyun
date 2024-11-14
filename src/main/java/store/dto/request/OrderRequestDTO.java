package store.dto.request;

public record OrderRequestDTO(
        String productName,
        Integer orderQuantity
) {
    public static OrderRequestDTO of(String productName, Integer orderQuantity) {
        return new OrderRequestDTO(productName, orderQuantity);
    }
}
