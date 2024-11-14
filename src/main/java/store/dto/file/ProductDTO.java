package store.dto.file;

public record ProductDTO(
        String name,
        Integer price,
        Integer quantity,
        String promotionName
) {
    public static ProductDTO of(String name, Integer price, Integer quantity, String promotionName) {
        return new ProductDTO(name, price, quantity, promotionName);
    }
}
