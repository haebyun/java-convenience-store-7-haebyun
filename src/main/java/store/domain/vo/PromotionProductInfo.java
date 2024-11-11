package store.domain.vo;

import store.domain.Product;

public record PromotionProductInfo(
        String name,
        Integer price,
        Integer stock,
        String promotionName
) {

    public static PromotionProductInfo from(Product promotionProduct) {
        return new PromotionProductInfo(
                promotionProduct.getName(),
                promotionProduct.getPriceValue(),
                promotionProduct.getStockValue(),
                promotionProduct.getPromotionName()
        );
    }
}
