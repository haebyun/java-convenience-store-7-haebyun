package store.domain;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.vo.Period;
import store.domain.vo.PromotionSpecific;

public class PromotionTests {

    @Test
    @DisplayName("프로모션 이름과 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 PromotionType, 프로모션 기간(Period) 갖는다.")
    void testCreatePromotion() {
        String promotionName = "탄산 2+1";
        Integer buy = 2;
        Integer get = 1;
        LocalDate startDate = LocalDate.parse("2024-01-01");
        LocalDate endDate = LocalDate.parse("2024-12-31");

        Promotion promotion = Promotion.of(
                promotionName,
                PromotionSpecific.of(buy, get),
                Period.of(startDate, endDate)
        );

        Assertions.assertThat(promotion).isNotNull();
    }
}
