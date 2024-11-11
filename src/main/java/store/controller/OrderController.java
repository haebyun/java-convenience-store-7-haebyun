package store.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.membership.MembershipCalculator;
import store.domain.order.Order;
import store.domain.order.vo.OrderLine;
import store.domain.order.vo.OrderResult;
import store.domain.order.vo.PromotionLine;
import store.domain.product.Products;
import store.domain.promotion.Promotions;
import store.domain.vo.OrderRequest;
import store.domain.vo.OrderRequests;
import store.domain.vo.PromotionData;
import store.domain.vo.PromotionProductInfo;
import store.domain.vo.PromotionResult;
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

    public void run() {
        Products products = OrderMapper.toProductsDomain(productDataLoader.loadProducts("products.md"));
        Promotions promotions = OrderMapper.toPromotionsDomain(promotionDataLoader.loadPromotions("promotions.md"));

        Repeater.executeWhileTrue(() -> {
            outputView.printProductsStocks(products);
            List<OrderResult> orderResults = processOrderCycle(products, promotions);
            for (OrderResult orderResult : orderResults) {
                products.updateProductStock(orderResult.productName(), orderResult.quantity());
            }
        }, this::shouldTerminateLoop);
    }

    private List<OrderResult> processOrderCycle(Products products, Promotions promotions) {
        OrderRequests orderRequests = getValidOrderRequests(products);
        Order order = processOrder(orderRequests, products, promotions);
        Integer membershipDiscountAmount = applyMembership(order);
        outputView.printReceipt(order, membershipDiscountAmount);
        return order.createOrderResult();
    }

    private boolean shouldTerminateLoop() {
        UserOption userResponse = getValidOrderRepeatOption();
        return userResponse.isNo();
    }

    private Order processOrder(OrderRequests orderRequests, Products products, Promotions promotions) {
        List<PromotionLine> promotionLines = new ArrayList<>();
        List<OrderLine> orderLines = new ArrayList<>();
        LocalDate orderDate = orderRequests.orderDate();
        for (OrderRequest orderRequest : orderRequests.orderRequests()) {
            if (isPromotionApplicable(orderRequest, products)) {
                PromotionProductInfo promotionProductInfo = products.getPromotionProductInfo(
                        orderRequest.productName());
                orderRequest = updateOrderRequest(orderRequest, promotionProductInfo, promotions, orderDate);
                PromotionLine promotionLine = applyPromotion(orderRequest, promotionProductInfo, promotions, orderDate);
                promotionLines.add(promotionLine);
            }
            orderLines.add(processOrderLine(orderRequest, products));
        }
        return new Order(orderLines, promotionLines);
    }

    private boolean isPromotionApplicable(OrderRequest orderRequest, Products products) {
        return products.isPromotionProductExist(orderRequest.productName());
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

    private PromotionLine applyPromotion(
            OrderRequest orderRequest,
            PromotionProductInfo promotionProductInfo,
            Promotions promotions,
            LocalDate orderDate
    ) {
        PromotionResult promotionResult = promotions.createPromotionResult(promotionProductInfo,
                orderRequest.quantity(), orderDate);
        return new PromotionLine(
                promotionResult.productName(),
                promotionResult.productPrice(),
                promotionResult.appliedProductQuantity(),
                promotionResult.freeQuantity()
        );
    }

    private OrderLine processOrderLine(OrderRequest orderRequest, Products products) {
        Integer pricePerProduct = products.findPriceByName(orderRequest.productName());
        return new OrderLine(orderRequest.productName(), orderRequest.quantity(),
                pricePerProduct);
    }

    private Integer applyMembership(Order order) {
        UserOption membershipOption = getValidMembershipOption();
        if (membershipOption.isYes()) {
            return membershipCalculator.calculateMembershipDiscount(order);
        }
        return 0;
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
