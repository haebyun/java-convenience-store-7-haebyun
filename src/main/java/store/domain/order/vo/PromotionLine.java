package store.domain.order.vo;

public class PromotionLine {
    private final String productName;
    private final Integer productPrice;
    private final Integer promotionAppliedQuantity;
    private final Integer freeQuantity;

    private PromotionLine(String productName, Integer productPrice, Integer promotionAppliedQuantity,
                          Integer freeQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.promotionAppliedQuantity = promotionAppliedQuantity;
        this.freeQuantity = freeQuantity;
    }

    public static PromotionLine of(String productName, Integer productPrice, Integer promotionAppliedQuantity,
                                   Integer freeQuantity) {
        return new PromotionLine(productName, productPrice, promotionAppliedQuantity, freeQuantity);
    }

    public Integer calculatePromotionAmount() {
        return productPrice * freeQuantity;
    }

    public Integer calculateAmountOfPromotionApplied() {
        return productPrice * promotionAppliedQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getFreeQuantity() {
        return freeQuantity;
    }
}
