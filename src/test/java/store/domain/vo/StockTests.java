package store.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StockTests {
    private final Integer initialQuantity = 100;

    @Test
    @DisplayName("재고 수량은 재고의 갯수를 나타내는 필드를 갖는다.")
    void testCreateStock() {
        Stock stock = Stock.of(initialQuantity);

        assertThat(stock.getQuantity()).isEqualTo(initialQuantity);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100,-10,-5,-1})
    @DisplayName("재고 수량은 생성될 때, 0 이상이여야 한다.")
    void testsInvalidStockQuantityThenThrowsException(int quantity) {
        assertThatThrownBy(() -> Stock.of(quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
