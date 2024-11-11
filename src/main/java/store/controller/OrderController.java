package store.controller;

import java.time.LocalDate;
import store.domain.membership.MembershipCalculator;
import store.domain.product.Products;
import store.domain.promotion.Promotions;
import store.domain.vo.OrderRequest;
import store.domain.vo.OrderRequests;
import store.domain.vo.PromotionData;
import store.domain.vo.PromotionProductInfo;
import store.domain.vo.UserOption;
import store.dto.request.OrderRequestsDTO;
import store.dto.request.UserOptionDTO;
import store.exception.ErrorMessage;
import store.loader.ProductDataLoader;
import store.loader.PromotionDataLoader;
import store.mapper.OrderMapper;
import store.util.Repeater;
import store.view.input.InputView;
import store.view.output.OutputView;

public class OrderController {
    private final InputView inputView;
    private final OutputView outputView;
    private final ProductDataLoader productDataLoader;
    private final PromotionDataLoader promotionDataLoader;
    private final MembershipCalculator membershipCalculator;

    public OrderController(
            InputView inputView,
            OutputView outputView,
            ProductDataLoader productDataLoader,
            PromotionDataLoader promotionDataLoader,
            MembershipCalculator membershipCalculator
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.productDataLoader = productDataLoader;
        this.promotionDataLoader = promotionDataLoader;
        this.membershipCalculator = membershipCalculator;
    }

    private OrderRequest updateOrderRequest(
            OrderRequest orderRequest,
            PromotionProductInfo promotionProductInfo,
            Promotions promotions,
            LocalDate orderDate
    ) {
        PromotionData promotionData = promotions.createPromotionData(promotionProductInfo, orderRequest.quantity(),
                orderDate);
        if (promotionData.extraStock() > 0) {
            return chooseAddPromotionStock(orderRequest, promotionData);
        }
        if (promotionData.fullPayQuantity() > 0) {
            return choosePurchaseWithoutFullPayQuantity(orderRequest, promotionData);
        }
        return orderRequest;
    }

    private OrderRequest chooseAddPromotionStock(OrderRequest orderRequest, PromotionData promotionData) {
        UserOption addFreeStockOption = getValidAddFreeStockOption(
                orderRequest.productName(),
                promotionData.extraStock()
        );
        if(addFreeStockOption.isYes()) {
            return OrderRequest.withAddPromotionStock(orderRequest, promotionData.fullPayQuantity());
        }
        return orderRequest;
    }

    private OrderRequest choosePurchaseWithoutFullPayQuantity(OrderRequest orderRequest, PromotionData promotionData) {
        UserOption fullPayOption = getValidFullPayOption(
                orderRequest.productName(),
                promotionData.fullPayQuantity()
        );
        if(fullPayOption.isNo()) {
            return OrderRequest.withReduceFullPayQuantity(orderRequest, promotionData.fullPayQuantity());
        }
        return orderRequest;
    }

    private OrderRequests getValidOrderRequests(Products products) {
        return Repeater.executeWithRetry(
                () -> createOrderRequestsFromInput(products),
                outputView::printError
        );
    }

    private OrderRequests createOrderRequestsFromInput(Products products) {
        OrderRequestsDTO orderRequestDTOs = inputView.inputOrderRequests();
        OrderRequests orderRequests = OrderMapper.toOrderRequests(orderRequestDTOs);
        validateProductExist(orderRequests, products);
        validateStockAvailability(orderRequests, products);
        return orderRequests;
    }

    private void validateProductExist(OrderRequests orderRequests, Products products) {
        boolean isDefinedProduct = orderRequests.orderRequests().stream()
                .anyMatch(orderRequest -> products.existProductByName(orderRequest.productName()));
        if (!isDefinedProduct) {
            throw new IllegalArgumentException(ErrorMessage.NON_EXISTENT_PRODUCT.getMessage());
        }
    }

    private void validateStockAvailability(OrderRequests orderRequests, Products products) {
        boolean isAnyOverStock = orderRequests.orderRequests().stream()
                .anyMatch(orderRequest -> products.isOverStock(orderRequest.productName(), orderRequest.quantity()));
        if (isAnyOverStock) {
            throw new IllegalArgumentException(ErrorMessage.EXCEED_STOCK_QUANTITY.getMessage());
        }
    }

    private UserOption getValidAddFreeStockOption(String productName, int additionalQuantity) {
        return Repeater.executeWithRetry(
                () -> {
                    UserOptionDTO userOptionDTO = inputView.inputAddFreeStockOption(productName, additionalQuantity);
                    return UserOption.of(userOptionDTO.option());
                },
                outputView::printError
        );
    }

    private UserOption getValidFullPayOption(String productName, int additionalQuantity) {
        return Repeater.executeWithRetry(
                () -> {
                    UserOptionDTO userOptionDTO = inputView.inputPurchaseFullPayOption(productName,
                            additionalQuantity);
                    return UserOption.of(userOptionDTO.option());
                },
                outputView::printError
        );
    }

    private UserOption getValidMembershipOption() {
        return Repeater.executeWithRetry(
                () -> {
                    UserOptionDTO userOptionDTO = inputView.inputMembershipOption();
                    return UserOption.of(userOptionDTO.option());
                },
                outputView::printError
        );
    }

    private UserOption getValidOrderRepeatOption() {
        return Repeater.executeWithRetry(
                () -> {
                    UserOptionDTO userOptionDTO = inputView.inputOrderRepeatOption();
                    return UserOption.of(userOptionDTO.option());
                },
                outputView::printError
        );
    }
}
