package store.domain.order;

import java.util.Collections;
import java.util.List;
import store.domain.order.vo.OrderLine;
import store.domain.order.vo.OrderResult;
import store.domain.order.vo.PromotionLine;

public class Order {
    private final List<OrderLine> orderLines;
    private final List<PromotionLine> promotionLines;

    private Order(List<OrderLine> orderLines, List<PromotionLine> promotionLines) {
        this.orderLines = orderLines;
        this.promotionLines = promotionLines;
    }

    public static Order of(List<OrderLine> orderLines, List<PromotionLine> promotionLines) {
        return new Order(orderLines, promotionLines);
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

    public Integer calculateTotalOrderQuantity() {
        return orderLines.stream()
                .mapToInt(OrderLine::getQuantity)
                .sum();
    }

    public List<OrderResult> createOrderResult() {
        return orderLines.stream()
                .map(orderLine -> OrderResult.of(orderLine.getProductName(), orderLine.getQuantity()))
                .toList();
    }

    public List<OrderLine> getOrderLines() {
        return Collections.unmodifiableList(orderLines);
    }

    public List<PromotionLine> getPromotionLines() {
        return Collections.unmodifiableList(promotionLines);
    }
}
