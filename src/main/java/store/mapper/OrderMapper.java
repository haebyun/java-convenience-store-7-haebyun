package store.mapper;

import java.util.List;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.promotion.Promotion;
import store.domain.promotion.Promotions;
import store.dto.ProductDTO;
import store.dto.PromotionDTO;

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

    public static Promotions toPromotionsDomain(List<PromotionDTO> promotionDTOs) {
        List<Promotion> promotions = promotionDTOs.stream()
                .map(promotionDTO -> Promotion.from(
                        promotionDTO.name(),
                        promotionDTO.buy(),
                        promotionDTO.get(),
                        promotionDTO.startDate(),
                        promotionDTO.endDate()
                ))
                .toList();
        return Promotions.of(promotions);
    }
}
