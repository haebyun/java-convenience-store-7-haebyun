package store.domain.product.vo;

import store.exception.ErrorMessage;

public class Stock {
    private Integer quantity;

    private Stock(Integer quantity) {
        this.quantity = quantity;
    }

    public static Stock of(Integer quantity) {
        validate(quantity);
        return new Stock(quantity);
    }

    static void validate(Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage());
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void decrease(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage());
        }
        if (this.quantity < quantity) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage());
        }
        this.quantity -= quantity;
    }
}
