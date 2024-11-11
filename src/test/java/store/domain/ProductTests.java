package store.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ProductTests {

    @Test
    @DisplayName("상품은 이름, 가격, 재고 수량, 적용 프로모션 이름을 갖는다.")
    void testCreateProduct() {
        String productName = "요아정";
        Integer productPrice = 2500;
        Integer stockQuantity = 100;
        String promotionName = "2+1";

        Product product = Product.from(productName, productPrice, stockQuantity, promotionName);

        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getPriceValue()).isEqualTo(productPrice);
        assertThat(product.getStockValue()).isEqualTo(stockQuantity);
        assertThat(product.getPromotionName()).isEqualTo(promotionName);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("재고를 정상적으로 감소시킨다.")
    void testDecreaseStockQuantity(int quantity) {
        String productName = "요아정";
        Integer productPrice = 2500;
        Integer stockInitialQuantity = 100;
        String promotionName = "2+1";

        Product product = Product.from(productName, productPrice, stockInitialQuantity, promotionName);

        int remainingQuantity = product.decreaseStock(quantity);

        assertThat(product.getStockValue()).isEqualTo(stockInitialQuantity - quantity);
        assertThat(remainingQuantity).isEqualTo(0); // 모든 주문 수량이 재고에서 차감되었으므로 남은 수량은 0
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    @DisplayName("주문 수량이 재고를 초과할 때, 가능한 만큼만 재고를 감소시키고 남은 수량을 반환한다.")
    void testDecreaseStockQuantityExceedingStock(int quantity) {
        String productName = "요아정";
        Integer productPrice = 2500;
        Integer stockInitialQuantity = 1;
        String promotionName = "2+1";

        Product product = Product.from(productName, productPrice, stockInitialQuantity, promotionName);

        int remainingQuantity = product.decreaseStock(quantity);

        assertThat(product.getStockValue()).isEqualTo(0); // 재고는 모두 소진
        assertThat(remainingQuantity).isEqualTo(quantity - stockInitialQuantity); // 남은 수량은 주문 수량에서 초기 재고를 뺀 값
    }
}
