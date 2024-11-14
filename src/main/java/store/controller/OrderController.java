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

        runOrderProcess(products, promotions);
    }

    private void runOrderProcess(Products products, Promotions promotions) {
        Repeater.executeWhileTrue(() -> {
            outputView.printProductsStocks(products);
            processOneOrderCycle(products, promotions);
        }, this::shouldContinueOrdering);
    }

    private Boolean shouldContinueOrdering() {
        UserOption userResponse = getValidOrderRepeatOption();
        return userResponse.isYes();
    }

    private void processOneOrderCycle(Products products, Promotions promotions) {
        OrderRequests orderRequests = getValidOrderRequests(products, promotions);
        Order order = createOrder(orderRequests, products, promotions);
        Integer membershipDiscount = applyMembership(order.calculateMembershipTargetAmount());
        outputView.printReceipt(order, membershipDiscount);
        updateProductStocks(products, order.createOrderResult());
    }

    private void updateProductStocks(Products products, List<OrderResult> orderResults) {
        orderResults.forEach(
                result -> products.updateProductStock(result.productName(), result.quantity())
        );
    }

    private Order createOrder(OrderRequests orderRequests, Products products, Promotions promotions) {
        List<OrderLine> orderLines = createOrderLines(orderRequests, products);
        List<PromotionLine> promotionLines = createPromotionLines(orderRequests, products, promotions);
        return Order.of(orderLines, promotionLines);
    }

    private List<OrderLine> createOrderLines(OrderRequests requests, Products products) {
        List<OrderLine> orderLines = new ArrayList<>();
        for (OrderRequest request : requests.orderRequests()) {
            orderLines.add(processOrderLine(request, products));
        }
        return orderLines;
    }

    private List<PromotionLine> createPromotionLines(OrderRequests requests, Products products, Promotions promotions) {
        List<PromotionLine> promotionLines = new ArrayList<>();
        for (OrderRequest request : requests.orderRequests()) {
            if (isPromotionApplicable(request, products, promotions, requests.orderDate())) {
                PromotionProductInfo promotionProductInfo = products.getPromotionProductInfo(request.productName());
                promotionLines.add(createPromotion(request, promotionProductInfo, promotions, requests.orderDate()));
            }
        }
        return promotionLines;
    }

    private OrderRequests getValidOrderRequests(Products products, Promotions promotions) {
        return Repeater.executeWithRetry(
                () -> {
                    OrderRequests requests = OrderMapper.toOrderRequests(inputView.inputOrderRequests());
                    validateProductExist(requests, products);
                    validateStockAvailability(requests, products);
                    return updateOrderRequests(requests, products, promotions);
                },
                outputView::printError
        );
    }

    private OrderRequests updateOrderRequests(OrderRequests requests, Products products, Promotions promotions) {
        List<OrderRequest> updatedOrderRequests = new ArrayList<>();
        for (OrderRequest request : requests.orderRequests()) {
            updatedOrderRequests.add(updateOrderRequest(request, products, promotions, requests.orderDate()));
        }
        return OrderRequests.of(updatedOrderRequests, requests.orderDate());
    }

    private OrderRequest updateOrderRequest(OrderRequest request, Products products, Promotions promotions,
                                            LocalDate orderDate) {
        if (isPromotionApplicable(request, products, promotions, orderDate)) {
            PromotionProductInfo promotionProductInfo = products.getPromotionProductInfo(request.productName());
            PromotionData promotionData = promotions.createPromotionData(promotionProductInfo, request.quantity(),
                    orderDate);
            return updateOrderRequest(request, promotionData);
        }
        return request;
    }

    private Boolean isPromotionApplicable(OrderRequest request, Products products, Promotions promotions,
                                          LocalDate orderDate) {
        String PromotionName = products.findPromotionNameByProductName(request.productName());
        return promotions.isPromotionActiveOnDate(PromotionName, orderDate);
    }

    private OrderRequest updateOrderRequest(
            OrderRequest request,
            PromotionData promotionData
    ) {
        if (promotionData.extraStock() > 0) {
            return chooseAddPromotionStock(request, promotionData);
        }
        if (promotionData.fullPayQuantity() > 0) {
            return choosePurchaseWithoutFullPayQuantity(request, promotionData);
        }
        return request;
    }

    private OrderRequest chooseAddPromotionStock(OrderRequest orderRequest, PromotionData promotionData) {
        UserOption addFreeStockOption = getValidAddFreeStockOption(
                orderRequest.productName(),
                promotionData.extraStock()
        );
        if (addFreeStockOption.isYes()) {
            return OrderRequest.withAddPromotionStock(orderRequest, promotionData.fullPayQuantity());
        }
        return orderRequest;
    }

    private OrderRequest choosePurchaseWithoutFullPayQuantity(OrderRequest orderRequest, PromotionData promotionData) {
        UserOption fullPayOption = getValidFullPayOption(
                orderRequest.productName(),
                promotionData.fullPayQuantity()
        );
        if (fullPayOption.isNo()) {
            return OrderRequest.withReduceFullPayQuantity(orderRequest, promotionData.fullPayQuantity());
        }
        return orderRequest;
    }

    private PromotionLine createPromotion(OrderRequest orderRequest, PromotionProductInfo promotionProductInfo,
                                          Promotions promotions, LocalDate orderDate) {
        PromotionResult result = promotions.createPromotionResult(promotionProductInfo,
                orderRequest.quantity(), orderDate);
        return PromotionLine.of(result.productName(), result.productPrice(), result.appliedProductQuantity(),
                result.freeQuantity());
    }

    private OrderLine processOrderLine(OrderRequest orderRequest, Products products) {
        Integer pricePerProduct = products.findPriceByName(orderRequest.productName());
        return OrderLine.of(orderRequest.productName(), orderRequest.quantity(),
                pricePerProduct);
    }

    private Integer applyMembership(Integer totalAmountOfMembershipTarget) {
        UserOption membershipOption = getValidMembershipOption();
        if (membershipOption.isYes()) {
            return membershipCalculator.calculateMembershipDiscount(totalAmountOfMembershipTarget);
        }
        return 0;
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
