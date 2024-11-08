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
}
