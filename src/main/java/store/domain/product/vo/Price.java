package store.domain.product.vo;

public class Price {
    private final Integer value;

    private Price(Integer value) {
        this.value = value;
    }

    public static Price of(Integer value) {
        validate(value);
        return new Price(value);
    }

    private static void validate(Integer value) {
        if(value<=0) {
            throw new IllegalArgumentException("상품의 가격은 양수만 허용됩니다.");
        }
    }

    public Integer getValue() {
        return value;
    }
}
