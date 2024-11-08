package store.exception;

public enum ErrorMessage {
    INVALID_PRICE("상품의 가격은 양수만 허용됩니다.");

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_MESSAGE_PREFIX + message;
    }
}
