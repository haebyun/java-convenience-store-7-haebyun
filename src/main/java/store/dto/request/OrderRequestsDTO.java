package store.dto.request;

import java.time.LocalDate;
import java.util.List;

public record OrderRequestsDTO(
        List<OrderRequestDTO> orderRequests,
        LocalDate orderDate
) {
    public static OrderRequestsDTO of(List<OrderRequestDTO> orderRequestDTOs, LocalDate orderDate) {
        return new OrderRequestsDTO(orderRequestDTOs, orderDate);
    }
}
