package store.domain.promotion.vo;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PeriodTests {

    @Test
    @DisplayName("종료일은 시작일보다 이후여야만 한다.")
    void testInvalidPeriodCreate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);

        assertThatThrownBy(() -> Period.of(startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "2023-01-01, 2023-12-31, 2023-06-15, true",   // 기간 내 날짜
            "2023-01-01, 2023-12-31, 2023-01-01, true",   // 시작일
            "2023-01-01, 2023-12-31, 2023-12-31, true",   // 종료일
            "2023-01-01, 2023-12-31, 2022-12-31, false",  // 기간 이전 날짜
            "2023-01-01, 2023-12-31, 2024-01-01, false"   // 기간 이후 날짜
    })
    @DisplayName("주어진 날짜가 기간 내에 있는지 확인")
    void testsIsWithinPeriod(String start, String end, String testDate, boolean expected) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        LocalDate dateToTest = LocalDate.parse(testDate);
        Period period = Period.of(startDate, endDate);

        assertThat(period.isWithinPeriod(dateToTest)).isEqualTo(expected);
    }
}
