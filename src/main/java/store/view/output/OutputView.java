package store.view.output;

import store.domain.product.Products;
import store.domain.order.Order;

public interface OutputView {
    void printError(String errorMessage);

    void printProductsStocks(Products products);

    void printReceipt(Order order, Integer membershipDiscountAmount);
}
