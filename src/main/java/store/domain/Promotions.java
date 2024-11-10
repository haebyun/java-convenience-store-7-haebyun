package store.domain;

import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public static Promotions of(List<Promotion> promotions) {
        return new Promotions(promotions);
    }
}
