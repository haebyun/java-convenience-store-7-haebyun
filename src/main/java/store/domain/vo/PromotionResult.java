package store.domain.vo;

public record PromotionResult(
        String productName,
        Integer productPrice,
        Integer appliedProductQuantity,
        Integer freeQuantity
) {
}
