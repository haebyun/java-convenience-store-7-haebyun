package store.domain.promotion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import store.domain.vo.PromotionData;
import store.domain.vo.PromotionProductInfo;
import store.domain.vo.PromotionResult;

public class Promotions {
    private final List<Promotion> promotions;

    private Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public static Promotions of(List<Promotion> promotions) {
        return new Promotions(promotions);
    }

    private Optional<Promotion> findPromotionByName(String name) {
        return promotions.stream()
                .filter(promo -> name.equals(promo.getName()))
                .findAny();
    }

    public PromotionData createPromotionData(PromotionProductInfo info, Integer orderQuantity, LocalDate orderDate) {
        return findPromotionByName(info.promotionName())
                .filter(promotion -> promotion.isActive(orderDate))
                .map(promotion -> buildData(promotion, info, orderQuantity))
                .orElse(new PromotionData(0, 0));
    }

    private PromotionData buildData(Promotion promotion, PromotionProductInfo info, Integer orderQuantity) {
        int extraStock = promotion.getAdditionalFreeItemCount(info.stock(), orderQuantity);
        int nonPromotionQuantity = promotion.calculateNonPromotionQuantity(info.stock(), orderQuantity);
        return new PromotionData(extraStock, nonPromotionQuantity);
    }

    public PromotionResult createPromotionResult(PromotionProductInfo info, Integer orderQuantity, LocalDate orderDate) {
        return findPromotionByName(info.promotionName())
                .filter(promo -> promo.isActive(orderDate))
                .map(promo -> buildResult(promo, info, orderQuantity))
                .orElse(new PromotionResult(info.name(), info.price(), 0, 0));
    }

    private PromotionResult buildResult(Promotion promotion, PromotionProductInfo info, Integer orderQuantity) {
        int appliedQuantity = promotion.calculateAppliedPromotionQuantity(info.stock(), orderQuantity);
        int freeQuantity = promotion.calculateFreeProductCount(appliedQuantity);
        return new PromotionResult(info.name(), info.price(), appliedQuantity, freeQuantity);
    }

    public Boolean isPromotionActiveOnDate(String promotionName, LocalDate orderDate) {
        return findPromotionByName(promotionName)
                .map(promotion -> promotion.isActive(orderDate))
                .orElse(false);
    }
}
