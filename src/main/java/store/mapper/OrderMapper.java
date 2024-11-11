package store.mapper;

import java.util.List;
import store.domain.product.Product;
import store.domain.product.Products;
import store.dto.ProductDTO;

public final class OrderMapper {
    public static Products toProductsDomain(List<ProductDTO> productDTOs) {
        List<Product> products = productDTOs.stream()
                .map(productDTO -> Product.from(
                        productDTO.name(),
                        productDTO.price(),
                        productDTO.quantity(),
                        productDTO.promotionName())
                )
                .toList();
        return Products.of(products);
    }
}
