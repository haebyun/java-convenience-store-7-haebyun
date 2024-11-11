package store.view.input;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import store.dto.request.OrderRequestDTO;
import store.dto.request.OrderRequestsDTO;
import store.dto.request.UserOptionDTO;
import store.view.input.parser.InputParser;

public class ConsoleInputView implements InputView {
    private static final String REQUEST_ORDER_REQUESTS = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String REQUEST_ADD_FREE_STOCK_OPTION = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n";
    private static final String REQUEST_PURCHASE_FULL_PAY_OPTION = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n";
    private static final String REQUEST_MEMBERSHIP_OPTION = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String REQUEST_ORDER_REPEAT_OPTION = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    @Override
    public OrderRequestsDTO inputOrderRequests() {
        System.out.println(REQUEST_ORDER_REQUESTS);
        String response = Console.readLine();
        List<OrderRequestDTO> orderRequestDTOs = InputParser.parseOrderRequests(response);
        return OrderRequestsDTO.of(orderRequestDTOs, DateTimes.now().toLocalDate());
    }

    @Override
    public UserOptionDTO inputAddFreeStockOption(String productName, int additionalQuantity) {
        System.out.printf(REQUEST_ADD_FREE_STOCK_OPTION, productName, additionalQuantity);
        String response = Console.readLine();
        return UserOptionDTO.of(response);
    }

    @Override
    public UserOptionDTO inputPurchaseFullPayOption(String productName, int nonPromotionQuantity) {
        System.out.printf(REQUEST_PURCHASE_FULL_PAY_OPTION, productName, nonPromotionQuantity);
        String response = Console.readLine();
        return UserOptionDTO.of(response);
    }
}