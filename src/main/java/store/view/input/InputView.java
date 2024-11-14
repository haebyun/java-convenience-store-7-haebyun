package store.view.input;

import store.dto.request.OrderRequestsDTO;
import store.dto.request.UserOptionDTO;

public interface InputView {
    OrderRequestsDTO inputOrderRequests();

    UserOptionDTO inputAddFreeStockOption(String productName, int additionalQuantity);

    UserOptionDTO inputPurchaseFullPayOption(String productName, int nonPromotionQuantity);

    UserOptionDTO inputMembershipOption();

    UserOptionDTO inputOrderRepeatOption();
}
