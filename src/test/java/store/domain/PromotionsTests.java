package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.vo.PromotionData;
import store.domain.vo.PromotionProductInfo;
import store.domain.vo.PromotionResult;

public class PromotionsTests {
    @ParameterizedTest
    @CsvSource({
            "5, 5, 2, 1, 0, 2",    // 제공된 예시: 8 주문, 8 재고, 2+1 프로모션
            "9, 8, 2, 1, 1, 2",    // 재고 충분 -> 더 받는 선택과 그냥 결제하는 선택이 존재 가능
            "15, 6, 1, 1, 0, 0",   // 프로모션 정확히 적용됨
            "10, 10, 2, 1, 0, 1",
            "7,10, 2, 1, 0, 4"
    })
    @DisplayName("프로모션이 존재하고 활성 상태일 때, 올바른 PromotionData를 반환한다.")
    void testsCreatePromotionDataActivePromotion(int promotionStock, int orderQuantity, int buy, int get,
                                                 int expectedExtraStock, int expectedNonPromoQty) {
        Promotion promotion = Promotion.from(
                "promotion",
                buy,
                get,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );
        Promotions promotions = Promotions.of(Collections.singletonList(promotion));

        PromotionProductInfo info = new PromotionProductInfo("promotionProduct", 1000, promotionStock, "promotion");
        LocalDate orderDate = LocalDate.now();

        PromotionData promotionData = promotions.createPromotionData(info, orderQuantity, orderDate);

        PromotionData expectedData = new PromotionData(expectedExtraStock, expectedNonPromoQty);
        assertThat(promotionData).isEqualTo(expectedData);
    }

    @Test
    @DisplayName("프로모션이 존재하지만 비활성 상태일 때, 값이 모두 0인 PromotionData 반환한다.")
    void testCreatePromotionDataInactivePromotion() {
        Promotion promotion = Promotion.from(
                "Inactive Promotion",
                2,
                1,
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(5)
        );
        Promotions promotions = Promotions.of(Collections.singletonList(promotion));

        PromotionProductInfo info = new PromotionProductInfo("promotionProduct", 1000, 10, "Inactive Promotion");
        LocalDate orderDate = LocalDate.now(); // 현재는 비활성 상태

        PromotionData promotionData = promotions.createPromotionData(info, 2, orderDate);

        PromotionData expectedData = new PromotionData(0, 0);
        assertThat(promotionData).isEqualTo(expectedData);
    }

    @Test
    @DisplayName("프로모션이 존재하지 않을 때, 값이 모두 0인 PromotionData 반환한다.")
    void testCreatePromotionDataNonExistentPromotion() {
        Promotion promotion = Promotion.from(
                "Existing Promotion",
                2,
                1,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5)
        );
        Promotions promotions = Promotions.of(Collections.singletonList(promotion));

        PromotionProductInfo info = new PromotionProductInfo("promotionProduct", 1000, 10, "Not Exist Promotion");
        LocalDate orderDate = LocalDate.now();

        PromotionData promotionData = promotions.createPromotionData(info, 2, orderDate);

        PromotionData expectedData = new PromotionData(0, 0);
        assertThat(promotionData).isEqualTo(expectedData);
    }

    @ParameterizedTest
    @CsvSource({
            "8, 8, 2, 1, 6, 2",    // 8 재고, 2+1 프로모션, 8 주문 => 6 적용, 3 무료
            "5, 5, 2, 1, 3, 1",    // 5 재고, 2+1 프로모션, 5 주문 => 3 적용, 1 무료
            "10, 5, 2, 1, 3, 1",   // 10 재고, 2+1 프로모션, 5 주문 => 3 적용, 1 무료
            "3, 3, 1, 1, 2, 1",     // 3 재고, 1+1 프로모션, 3 주문 => 2 적용, 1 무료
            "7, 10, 2, 1, 6, 2"     // 7 재고, 2+1 프로모션, 10 주문 => 6 적용, 2 무료
    })
    @DisplayName("프로모션이 존재하고 활성 상태일 때, 올바른 PromotionResult를 반환한다.")
    void testCreatePromotionResultActivePromotion(int promotionStock, int orderQuantity, int buy, int get,
                                                  int expectedAppliedQuantity, int expectedFreeQuantity) {
        Promotion promotion = Promotion.from(
                "Active Promotion",
                buy,
                get,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5)
        );
        Promotions promotions = Promotions.of(Collections.singletonList(promotion));

        PromotionProductInfo info = new PromotionProductInfo(
                "요아정",
                2500,
                promotionStock,
                "Active Promotion"
        );
        LocalDate orderDate = LocalDate.now();

        PromotionResult promotionResult = promotions.createPromotionResult(info, orderQuantity, orderDate);

        PromotionResult expectedResult = new PromotionResult("요아정", 2500, expectedAppliedQuantity,
                expectedFreeQuantity);
        assertThat(promotionResult).isEqualTo(expectedResult);
    }
}
