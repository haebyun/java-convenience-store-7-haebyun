package store.exception;

public enum ErrorMessage {
    INVALID_PRICE("상품의 가격은 양수만 허용됩니다."),
    INVALID_STOCK_QUANTITY("재고 수량은 0 이상만 허용됩니다."),
    INVALID_DECREASE_STOCK_QUANTITY("재고 감소 수량은 0보다 커야하며, 현재 재고보다 작거나 같아야합니다.");

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_MESSAGE_PREFIX + message;
    }
}
