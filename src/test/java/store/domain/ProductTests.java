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

        Product product = Product.of(productName, productPrice, stockQuantity, promotionName);

        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getPriceValue()).isEqualTo(productPrice);
        assertThat(product.getStockValue()).isEqualTo(stockQuantity);
        assertThat(product.getPromotionName()).isEqualTo(promotionName);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5})
    @DisplayName("재고 감소 로직을 가진다.")
    void testsDecreaseStockQuantity(Integer quantity) {
        String productName = "요아정";
        Integer productPrice = 2500;
        Integer stockInitialQuantity = 100;
        String promotionName = "2+1";

        Product product = Product.of(productName, productPrice, stockInitialQuantity, promotionName);
        product.decreaseStock(quantity);

        assertThat(product.getStockValue()).isEqualTo(stockInitialQuantity - quantity);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4,5})
    @DisplayName("재고 감소 로직을 가진다.")
    void testsToLargeDecreaseStockQuantity(Integer quantity) {
        String productName = "요아정";
        Integer productPrice = 2500;
        Integer stockInitialQuantity = 1;
        String promotionName = "2+1";

        Product product = Product.of(productName, productPrice, stockInitialQuantity, promotionName);

        assertThatThrownBy(() -> product.decreaseStock(quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
