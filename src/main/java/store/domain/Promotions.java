package store.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import store.domain.vo.PromotionData;
import store.domain.vo.PromotionProductInfo;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public static Promotions of(List<Promotion> promotions) {
        return new Promotions(promotions);
    }

    private Optional<Promotion> getByName(String name) {
        return promotions.stream()
                .filter(promo -> name.equals(promo.getName()))
                .findAny();
    }

    public PromotionData createPromotionData(PromotionProductInfo info, Integer orderQuantity, LocalDate orderDate) {
        return getByName(info.promotionName())
                .filter(promotion -> promotion.isActive(orderDate))
                .map(promotion -> buildData(promotion, info, orderQuantity))
                .orElse(new PromotionData(0, 0));
    }

    private PromotionData buildData(Promotion promotion, PromotionProductInfo info, Integer orderQuantity) {
        int extraStock = promotion.getAdditionalFreeItemCount(info.stock(), orderQuantity);
        int nonPromotionQuantity = promotion.calculateNonPromotionQuantity(info.stock(), orderQuantity);
        return new PromotionData(extraStock, nonPromotionQuantity);
    }
}
