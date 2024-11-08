package store.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PromotionSpecificTests {

    @Test
    @DisplayName("유효한 buy와 get 값으로 PromotionSpecific 객체를 생성할 수 있다.")
    void testCreatePromotionSpecific() {
        PromotionSpecific promotionSpecific = PromotionSpecific.of(2, 1);
        assertThat(promotionSpecific).isNotNull();
    }
}
