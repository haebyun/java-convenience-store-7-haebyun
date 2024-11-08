package store.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTests {

    @Test
    @DisplayName("상품은 이름, 가격, 적용 프로모션 이름을 갖는다.")
    void testCreateProduct() {
        String productName = "요아정";
        Integer productPrice = 2500;
        String promotionName = "2+1";

        Product product = Product.of(productName, productPrice, promotionName);

        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getPrice()).isEqualTo(productPrice);
        assertThat(product.getPromotionName()).isEqualTo(promotionName);
    }
}
