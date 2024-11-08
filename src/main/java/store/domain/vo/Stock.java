package store.domain.vo;

public class Stock {
    private Integer quantity;

    private Stock(Integer quantity) {
        this.quantity = quantity;
    }

    public static Stock of(Integer quantity) {
        return new Stock(quantity);
    }

    public Integer getQuantity() {
        return quantity;
    }
}
