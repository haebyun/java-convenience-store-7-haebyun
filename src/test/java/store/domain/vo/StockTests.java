package store.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StockTests {

    @Test
    @DisplayName("재고 수량은 재고의 갯수를 나타내는 필드를 갖는다.")
    void testCreateStock() {
        Integer quantity = 100;

        Stock stock = Stock.of(quantity);

        assertThat(stock.getQuantity()).isEqualTo(quantity);
    }
}
