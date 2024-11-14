package store.domain.vo;

import store.exception.ErrorMessage;

public record UserOption(
        String option
) {
    public static UserOption of(String option){
        validate(option);
        return new UserOption(option);
    }

    private static void validate(String option) {
        if(isInvalidOption(option)) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage());
        }
    }

    private static Boolean isInvalidOption(String option) {
        return !option.equals("Y") && !option.equals("N");
    }

    public Boolean isYes() {
        return option.equals("Y");
    }

    public Boolean isNo() {
        return option.equals("N");
    }
}
