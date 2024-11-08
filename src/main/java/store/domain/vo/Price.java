package store.domain.vo;

import store.exception.ErrorMessage;

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
        if (value <= 0) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_PRICE.getMessage());
        }
    }

    public Integer getValue() {
        return value;
    }
}
