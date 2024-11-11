package store.dto.request;

public record UserOptionDTO(
        String option
) {
    public static UserOptionDTO of(String option) {
        return new UserOptionDTO(option);
    }
}
