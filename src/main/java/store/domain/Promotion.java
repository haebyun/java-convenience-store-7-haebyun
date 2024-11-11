package store.domain;

import java.time.LocalDate;
import store.domain.vo.Period;
import store.domain.vo.PromotionSpecific;

public class Promotion {
    private final String name;
    private final PromotionSpecific promotionSpecific;
    private final Period period;

    private Promotion(String name, PromotionSpecific promotionSpecific, Period period) {
        this.name = name;
        this.promotionSpecific = promotionSpecific;
        this.period = period;
    }

    public static Promotion from(String name, Integer buy, Integer get, LocalDate startDate, LocalDate endDate) {
        return new Promotion(name, PromotionSpecific.of(buy, get), Period.of(startDate, endDate));
    }

    public Boolean isActive(LocalDate date) {
        return period.isWithinPeriod(date);
    }

    public Integer getAdditionalFreeItemCount(Integer promotionStock, Integer orderQuantity) {
        return promotionSpecific.getAdditionalFreeItems(promotionStock, orderQuantity);
    }

    public Integer calculateNonPromotionQuantity(Integer promotionStock, Integer orderQuantity) {
        if (orderQuantity > promotionStock) {
            return orderQuantity - promotionStock + promotionSpecific.getRemainingStock(promotionStock);
        }
        return promotionSpecific.getRemainingStock(orderQuantity);
    }//4개가 프로모션 재고, 프로모션은 2+1, 8개 주문 -> 5개 리턴

    public Integer calculateFreeProductCount(Integer promotionAppliedProductQuantity) {
        return promotionSpecific.getFreeCount(promotionAppliedProductQuantity);
    }

    public Integer calculateAppliedPromotionQuantity(Integer orderQuantity, Integer promotionStock) {
        return promotionSpecific.getAppliedQuantity(promotionStock, orderQuantity);
    }
}
