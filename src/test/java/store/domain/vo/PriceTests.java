package store.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.vo.Price;

public class PriceTests {

    @Test
    @DisplayName("상품은 가격을 필드로 갖는다.")
    void testCreatePrice() {
        Integer value = 1000;

        Price price = Price.of(value);

        Assertions.assertThat(price.getValue()).isEqualTo(value);
    }
}
