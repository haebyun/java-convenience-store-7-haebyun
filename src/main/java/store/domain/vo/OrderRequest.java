package store.domain.vo;

import store.exception.ErrorMessage;

public record OrderRequest(
        String productName,
        Integer quantity
) {
    public static OrderRequest of(String productName, Integer orderQuantity) {
        validate(orderQuantity);
        return new OrderRequest(productName, orderQuantity);
    }

    public static OrderRequest withAddPromotionStock(OrderRequest orderRequest, Integer addPromotionStock) {
        Integer orderQuantity = orderRequest.quantity + addPromotionStock;
        validate(orderQuantity);
        return new OrderRequest(orderRequest.productName, orderQuantity);
    }

    public static OrderRequest withReduceFullPayQuantity(OrderRequest orderRequest, Integer nonPromotionQuantity) {
        Integer orderQuantity = orderRequest.quantity - nonPromotionQuantity;
        validate(orderQuantity);
        return new OrderRequest(orderRequest.productName, orderQuantity);
    }

    private static void validate(Integer orderQuantity) {
        if(orderQuantity<=0) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }
}
