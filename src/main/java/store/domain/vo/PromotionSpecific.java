package store.domain.vo;

import store.exception.ErrorMessage;

public class PromotionSpecific {
    private final Integer buy;
    private final Integer get;

    private PromotionSpecific(Integer buy, Integer get) {
        this.buy = buy;
        this.get = get;
    }

    public static PromotionSpecific of(Integer buy, Integer get) {
        if (buy <= 0 || get < 0) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage());
        }
        return new PromotionSpecific(buy, get);
    }

    public Integer getFreeCount(Integer orderQuantity) {
        if (orderQuantity < buy + get) {
            return 0; // 구매 수량이 조건을 달성하지 못하면 무료 제공 없음
        }
        return (orderQuantity / (buy + get)) * get;
    }

    public Integer getAdditionalFreeItems(Integer promotionStock, Integer orderQuantity) {
        if (canProvideAdditionalFreeItems(promotionStock, orderQuantity)) {
            return get;
        }
        return 0;
    }

    private Boolean canProvideAdditionalFreeItems(Integer promotionStock, Integer orderQuantity) {
        if (orderQuantity + get > promotionStock) {
            return false;
        }
        return (orderQuantity + get) % (buy + get) == 0;
    }

    public Integer getRemainingStock(Integer quantity) {
        return quantity % (buy + get);
    }
}
