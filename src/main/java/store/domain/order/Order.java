package store.domain.order;

import java.util.List;
import store.domain.order.vo.OrderLine;
import store.domain.order.vo.PromotionLine;

public class Order {
    private final List<OrderLine> orderLines;
    private final List<PromotionLine> promotionLines;

    public Order(List<OrderLine> orderLines, List<PromotionLine> promotionLines) {
        this.orderLines = orderLines;
        this.promotionLines = promotionLines;
    }

    public Integer calculateTotalAmountOfOrder() {
        return orderLines.stream()
                .mapToInt(OrderLine::calculateOrderLinePrice)
                .sum();
    }

    public Integer calculateTotalAmountOfPromotion() {
        return promotionLines.stream()
                .mapToInt(PromotionLine::calculatePromotionAmount)
                .sum();
    }

    private Integer calculateTotalAmountOfPromotionApplied() {
        return promotionLines.stream()
                .mapToInt(PromotionLine::calculateAmountOfPromotionApplied)
                .sum();
    }

    public Integer calculateMembershipTargetAmount() {
        return calculateTotalAmountOfOrder() - calculateTotalAmountOfPromotionApplied();
    }
}