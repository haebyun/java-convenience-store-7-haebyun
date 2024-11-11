package store.view.output.formatter;

import store.domain.product.Product;
import store.domain.product.Products;

public final class OutputFormatter {
    private static final String START_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String STOCK_PREFIX = "-";
    private static final String RECEIPT_HEADER = "==============W 편의점================\n상품명\t\t\t\t수량\t\t금액\n";
    private static final String RECEIPT_FOOTER = "=============증\t\t정===============\n";
    private static final String RECEIPT_SEPARATOR = "====================================\n";

    public static String formatProductsStocks(Products products) {
        StringBuilder sb = new StringBuilder();
        sb.append(START_MESSAGE);
        for (Product product : products.getProducts()) {
            sb.append(formatSingleProductStock(product));
        }
        sb.append("\n");
        return sb.toString();
    }

    private static String formatSingleProductStock(Product product) {
        String promotionInfo = getPromotionInfo(product);
        String stockInfo = getStockInfo(product);
        return String.format("%s %s %,d원 %s %s\n",
                STOCK_PREFIX,
                product.getName(),
                product.getPriceValue(),
                stockInfo,
                promotionInfo);
    }

    private static String getPromotionInfo(Product product) {
        if (product.getPromotionName().equals("No Promotion")) {
            return "";
        } else {
            return product.getPromotionName();
        }
    }

    private static String getStockInfo(Product product) {
        if (product.getStockValue() == 0) {
            return "재고 없음";
        } else {
            return product.getStockValue() + "개";
        }
    }
}