package store.domain.product.vo;

public class Price {
    private final Integer value;

    private Price(Integer value) {
        this.value = value;
    }

    public static Price of(Integer value) {
        return new Price(value);
    }

    public Integer getValue() {
        return value;
    }
}
