package store.domain.vo;

public class PromotionSpecific {
    private final Integer buy;
    private final Integer get;

    private PromotionSpecific(Integer buy, Integer get) {
        this.buy = buy;
        this.get = get;
    }

    public static PromotionSpecific of(Integer buy, Integer get) {
        return new PromotionSpecific(buy, get);
    }
}
