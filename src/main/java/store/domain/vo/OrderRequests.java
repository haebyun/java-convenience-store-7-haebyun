package store.domain.vo;

import java.time.LocalDate;
import java.util.List;

public record OrderRequests(
        List<OrderRequest> orderRequests,
        LocalDate orderDate
) {
    public static OrderRequests of(List<OrderRequest> orderRequests, LocalDate orderDate) {
        return new OrderRequests(orderRequests, orderDate);
    }
}
