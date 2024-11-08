package store.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    void testInvalidPromotionSpecificCreation(int buy, int get) {
        assertThatThrownBy(() -> PromotionSpecific.of(buy, get))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
