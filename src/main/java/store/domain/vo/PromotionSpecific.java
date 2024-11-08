package store.domain.vo;

public class PromotionSpecific {
    private final Integer buy;
    private final Integer get;

    private PromotionSpecific(Integer buy, Integer get) {
        this.buy = buy;
        this.get = get;
    }

    public static PromotionSpecific of(Integer buy, Integer get) {
        if (buy <= 0 || get < 0) {
            throw new IllegalArgumentException();
        }
        return new PromotionSpecific(buy, get);
    }
}
