package store.view.output;

import store.domain.order.Order;
import store.domain.product.Products;
import store.view.output.formatter.OutputFormatter;

public class ConsoleOutputView implements OutputView{
    @Override
    public void printError(String errorMessage) {
        System.out.println(errorMessage + "\n");
    }

    @Override
    public void printProductsStocks(Products products) {
        String formattedStocks = OutputFormatter.formatProductsStocks(products);
        System.out.print(formattedStocks);
    }

    @Override
    public void printReceipt(Order order, Integer membershipDiscountAmount) {
        String formattedReceipt = OutputFormatter.formatReceipt(order, membershipDiscountAmount);
        System.out.println(formattedReceipt);
    }
}
