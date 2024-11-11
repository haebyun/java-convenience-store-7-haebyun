package store.domain;

import store.domain.order.Order;

public class MembershipCalculator {
    private static final double DISCOUNT_RATE = 0.3; // 30% 할인율
    private static final int MAX_DISCOUNT = 8000; // 최대 할인 금액

    public Integer calculateMembershipDiscount(Order order) {
        Integer totalAmountOfMembershipTarget = order.calculateMembershipTargetAmount();

        int discount = (int) (totalAmountOfMembershipTarget * DISCOUNT_RATE);
        return Math.min(discount, MAX_DISCOUNT);
    }
}
