package store.domain;

import store.domain.vo.Period;
import store.domain.vo.PromotionSpecific;

public class Promotion {
    private final String name;
    private final PromotionSpecific promotionSpecific;
    private final Period period;

    private Promotion(String name, PromotionSpecific promotionSpecific, Period period) {
        this.name = name;
        this.promotionSpecific = promotionSpecific;
        this.period = period;
    }

    public static Promotion of(String name, PromotionSpecific promotionSpecific, Period period) {
        return new Promotion(name, promotionSpecific, period);
    }
}
