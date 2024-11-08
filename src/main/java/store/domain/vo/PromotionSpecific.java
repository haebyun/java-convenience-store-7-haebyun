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
            throw new IllegalArgumentException(ErrorMessage.INVALID_PROMOTION_SPECIFIC_SETTING.getMessage());
        }
        return new PromotionSpecific(buy, get);
    }

    public Integer calculateFreeCount(Integer purchasedQuantity) {
        if (purchasedQuantity < buy) {
            return 0; // 구매 수량이 조건을 달성하지 못하면 무료 제공 없음
        }
        return (purchasedQuantity / buy) * get;
    }
}
