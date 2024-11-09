package store.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StockTests {
    private static final Integer initialQuantity = 100;

    @Test
    @DisplayName("재고 수량은 재고의 갯수를 나타내는 필드를 갖는다.")
    void testCreateStock() {
        Stock stock = Stock.of(initialQuantity);

        assertThat(stock.getQuantity()).isEqualTo(initialQuantity);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -5, -1})
    @DisplayName("재고 수량은 생성될 때, 0 이상이여야 한다.")
    void testsInvalidStockQuantityThenThrowsException(int quantity) {
        assertThatThrownBy(() -> Stock.of(quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("재고 감소 로직이 존재한다.")
    void testsValidDecreaseStockQuantity(int quantity) {
        Stock stock = Stock.of(initialQuantity);

        stock.decrease(quantity);

        assertThat(stock.getQuantity()).isEqualTo(initialQuantity - quantity);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -4, -10})
    @DisplayName("재고 감소 수량이 0 이하인 경우 예외가 발생한다.")
    void testsInvalidDecreaseStockQuantityThenThrowsException(int quantity) {
        Stock stock = Stock.of(initialQuantity);

        assertThatThrownBy(() -> stock.decrease(quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("재고 감소 수량이 현재 재고 수량보다 많은 경우 예외가 발생한다.")
    void testsInvalidToLargeDecreaseStockQuantityThenThrowsException(int quantity) {
        Stock stock = Stock.of(initialQuantity);

        int toLargeDecreasequantity = quantity + initialQuantity;

        assertThatThrownBy(() -> stock.decrease(toLargeDecreasequantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
