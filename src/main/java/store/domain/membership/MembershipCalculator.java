package store.domain.membership;

public class MembershipCalculator {
    private static final double DISCOUNT_RATE = 0.3; // 30% 할인율
    private static final int MAX_DISCOUNT = 8000; // 최대 할인 금액

    public Integer calculateMembershipDiscount(Integer totalAmountOfMembershipTarget) {
        int discount = (int) (totalAmountOfMembershipTarget * DISCOUNT_RATE);
        return Math.min(discount, MAX_DISCOUNT);
    }
}
