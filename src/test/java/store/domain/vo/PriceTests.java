package store.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PriceTests {

    @Test
    @DisplayName("상품은 가격을 필드로 갖는다.")
    void testCreatePrice() {
        Integer value = 1000;

        Price price = Price.of(value);

        assertThat(price.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1000, -100, 0})
    @DisplayName("상품의 가격은 0보다 커야 한다.")
    void testsInvalidPriceValue(int value) {
        assertThatThrownBy(() -> Price.of(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
