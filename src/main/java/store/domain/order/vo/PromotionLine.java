package store.domain.order.vo;

public class PromotionLine {
    private final String productName;
    private final Integer productPrice;
    private final Integer promotionAppliedQuantity;
    private final Integer freeQuantity;

    public PromotionLine(String productName, Integer productPrice, Integer promotionAppliedQuantity, Integer freeQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.promotionAppliedQuantity = promotionAppliedQuantity;
        this.freeQuantity = freeQuantity;
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
