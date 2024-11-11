package store.dto.file;

import java.time.LocalDate;

public record PromotionDTO(
        String name,
        Integer buy,
        Integer get,
        LocalDate startDate,
        LocalDate endDate
) {
    public static PromotionDTO of(
            String name,
            Integer buy,
            Integer get,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return new PromotionDTO(name, buy, get, startDate, endDate);
    }
}
