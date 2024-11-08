package store.domain.vo;

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
        if(quantity < 0) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_STOCK_QUANTITY.getMessage());
        }
    }

    public Integer getQuantity() {
        return quantity;
    }
}
