package store.domain.vo;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PeriodTests {

    @Test
    @DisplayName("종료일은 시작일보다 이후여야만 한다.")
    void testInvalidPeriodCreate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);

        Assertions.assertThatThrownBy(() -> Period.of(startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
