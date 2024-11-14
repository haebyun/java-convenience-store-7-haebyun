package store.view.input.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.dto.request.OrderRequestDTO;
import store.exception.ErrorMessage;

public final class InputParser {
    private static final Pattern itemPattern = Pattern.compile("\\[([a-zA-Z가-힣]+)-([0-9]+)]");
    private static final Pattern orderPattern = Pattern
            .compile(String.format("^%s(,%s)*$", itemPattern.pattern(), itemPattern.pattern()));

    public static List<OrderRequestDTO> parseOrderRequests(String input) {
        validateInputFormat(input);
        List<OrderRequestDTO> orderRequestDTOs = new ArrayList<>();
        Matcher matcher = itemPattern.matcher(input);
        while (matcher.find()) {
            OrderRequestDTO dto = parseSingleOrderRequest(matcher);
            orderRequestDTOs.add(dto);
        }
        return orderRequestDTOs;
    }

    private static void validateInputFormat(String input) {
        if (!orderPattern.matcher(input).matches()) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage());
        }
    }

    private static OrderRequestDTO parseSingleOrderRequest(Matcher matcher) {
        String productName = matcher.group(1).trim();
        String quantityStr = matcher.group(2).trim();
        int orderQuantity = parseQuantity(quantityStr);
        return OrderRequestDTO.of(productName, orderQuantity);
    }

    private static int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage(), e);
        }
    }
}
