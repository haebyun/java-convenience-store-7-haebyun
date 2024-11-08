package store.exception;

public enum ErrorMessage {
    INVALID_PRICE("상품의 가격은 양수만 허용됩니다."),
    INVALID_STOCK_QUANTITY("재고 수량은 0 이상만 허용됩니다."),
    INVALID_DECREASE_STOCK_QUANTITY("재고 감소 수량은 0보다 커야하며, 현재 재고보다 작거나 같아야합니다."),
    INVALID_PERIOD_SETTING("종료일은 시작일보다 이후여야 합니다."),
    INVALID_PROMOTION_SPECIFIC_SETTING("구매 수량은 1 이상이어야 하며, 무료 제공 수량은 0 이상이어야 합니다.");

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_MESSAGE_PREFIX + message;
    }
}
