package store.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PromotionSpecificTests {

    @Test
    @DisplayName("유효한 buy와 get 값으로 PromotionSpecific 객체를 생성할 수 있다.")
    void testCreatePromotionSpecific() {
        PromotionSpecific promotionSpecific = PromotionSpecific.of(2, 1);
        assertThat(promotionSpecific).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "-1, 1",
            "2, -1"
    })
    @DisplayName("buy가 1 이상이 아니거나 get이 0 미만이면 예외가 발생한다.")
    void testsInvalidPromotionSpecificCreation(int buy, int get) {
        assertThatThrownBy(() -> PromotionSpecific.of(buy, get))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "2, 1, 3, 1",    // 2+1 프로모션 조건 충족
            "2, 1, 6, 2",    // 조건을 두 번 충족하는 경우
            "2, 1, 7, 2",    // 조건 충족 이후 나머지가 있는 경우
            "1, 1, 12, 6",    // 1+1 프로모션
            "3, 2, 7, 2",    // 조건 충족 이후 나머지가 있는 경우
            "3, 2, 6, 2"     // 3+2 프로모션
    })
    @DisplayName("구매 수량에 따라 무료 제공 수량을 계산한다.")
    void testsGetFreeCount(int buy, int get, int purchasedQuantity, int expectedBonus) {
        PromotionSpecific promotionSpecific = PromotionSpecific.of(buy, get);

        int bonus = promotionSpecific.getFreeCount(purchasedQuantity);

        assertThat(bonus).isEqualTo(expectedBonus);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    @DisplayName("구매 수량이 buy 조건에 미달하면 무료 제공 수량은 0이다.")
    void testsCalculateBonusWhenPurchasedQuantityIsSmall(int purchasedQuantity) {
        PromotionSpecific promotionSpecific = PromotionSpecific.of(3, 1);

        int bonus = promotionSpecific.getFreeCount(purchasedQuantity);

        assertThat(bonus).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({
            "2, 1, 10, 5, 1",
            "2, 1, 2, 1, 0",
            "3, 2, 15, 8, 2",
            "1, 1, 10, 9, 1",
            "2, 1, 5, 2, 1",
            "2, 1, 3, 2, 1"
    })
    @DisplayName("무료상품을 받을 수 있는 상태이면 몇 개 더 가져오면 받을 수 있는 지 반환")
    void testsGetAdditionalFreeItems(int buy, int get, int promotionStock, int orderQuantity, int expectedAdditionalFreeItems) {
        PromotionSpecific promotionSpecific = PromotionSpecific.of(buy, get);

        int additionalFreeItems = promotionSpecific.getAdditionalFreeItems(promotionStock, orderQuantity);

        assertThat(additionalFreeItems).isEqualTo(expectedAdditionalFreeItems);
    }
}
