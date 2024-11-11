package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PromotionTests {

    @Test
    @DisplayName("프로모션 이름과 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 PromotionType, 프로모션 기간(Period) 갖는다.")
    void testCreatePromotion() {
        String promotionName = "탄산 2+1";
        Integer buy = 2;
        Integer get = 1;
        LocalDate startDate = LocalDate.parse("2024-01-01");
        LocalDate endDate = LocalDate.parse("2024-12-31");

        Promotion promotion = Promotion.from(promotionName, buy, get, startDate, endDate);

        assertThat(promotion).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-01, 2024-12-31, 2024-03-30, true",
            "2024-01-01, 2024-12-31, 2024-07-28, true",
            "2024-01-01, 2024-12-31, 2000-02-17, false",
            "2024-01-01, 2024-12-31, 2024-11-20, true",
            "2024-01-01, 2024-12-31, 2024-01-01, true"
    })
    @DisplayName("프로모션이 주어진 날짜에 활성화 상태인지 확인")
    void testPromotionIsActive(String start, String end, String testDate, boolean expected) {
        String promotionName = "탄산 2+1";
        Integer buy = 2;
        Integer get = 1;
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        LocalDate dateToTest = LocalDate.parse(testDate);

        Promotion promotion = Promotion.from(promotionName, buy, get, startDate, endDate);

        boolean isActive = promotion.isActive(dateToTest);

        assertThat(isActive).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "4, 8, 2, 1, 5",
            "4, 5, 2, 1, 2",
            "3, 6, 2, 1, 3",
            "5, 10, 2, 1, 7",
            "2, 1, 2, 1, 1",
            "2, 3, 2, 1, 3",
            "0, 5, 2, 1, 5"
    })
    @DisplayName("주어진 갯수에 따라 프로모션이 적용되지 않고 남는 갯수를 반환한다.")
    void testCalculateNonPromotionQuantity(int promotionStock, int orderQuantity, int buy, int get, int expectedNonPromoQuantity) {
        Promotion promotion = Promotion.from(
                "탄산 2+1",
                buy,
                get,
                LocalDate.parse("2024-01-01"),
                LocalDate.parse("2024-12-31")
        );

        int actualNonPromoQuantity = promotion.calculateNonPromotionQuantity(promotionStock, orderQuantity);

        assertThat(actualNonPromoQuantity).isEqualTo(expectedNonPromoQuantity);
    }
}
