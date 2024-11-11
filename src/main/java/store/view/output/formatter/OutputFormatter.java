package store.view.output.formatter;

import store.domain.order.Order;
import store.domain.order.vo.OrderLine;
import store.domain.order.vo.PromotionLine;
import store.domain.product.Product;
import store.domain.product.Products;

public final class OutputFormatter {
    private static final String START_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String STOCK_PREFIX = "-";
    private static final String RECEIPT_HEADER = "==============W 편의점================\n상품명\t\t\t\t수량\t\t금액\n";
    private static final String RECEIPT_FOOTER = "=============증\t\t정===============\n";
    private static final String RECEIPT_SEPARATOR = "====================================\n";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public static String formatProductsStocks(Products products) {
        StringBuilder sb = new StringBuilder();
        sb.append(START_MESSAGE);
        sb.append(LINE_SEPARATOR);
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

    public static String formatReceipt(Order order, Integer membershipDiscountAmount) {
        StringBuilder sb = new StringBuilder();
        sb.append(RECEIPT_HEADER);
        appendOrderLines(sb, order);
        sb.append(RECEIPT_FOOTER);
        appendPromotionLines(sb, order);
        sb.append(RECEIPT_SEPARATOR);
        appendSummary(sb, order, membershipDiscountAmount);
        return sb.toString();
    }

    private static void appendOrderLines(StringBuilder sb, Order order) {
        for (OrderLine orderLine : order.getOrderLines()) {
            sb.append(formatOrderLine(orderLine));
        }
    }

    private static String formatOrderLine(OrderLine orderLine) {
        return String.format("%s\t\t\t\t%d\t\t%,d\n",
                orderLine.getProductName(),
                orderLine.getQuantity(),
                orderLine.calculateOrderLinePrice());
    }

    private static void appendPromotionLines(StringBuilder sb, Order order) {
        for (PromotionLine promotionLine : order.getPromotionLines()) {
            sb.append(formatPromotionLine(promotionLine));
        }
    }

    private static String formatPromotionLine(PromotionLine promotionLine) {
        return String.format("%s\t\t\t\t%d\n",
                promotionLine.getProductName(),
                promotionLine.getFreeQuantity());
    }

    private static void appendSummary(StringBuilder sb, Order order, Integer membershipDiscountAmount) {
        int totalOrderQuantity = order.calculateTotalOrderQuantity();
        int totalOrderAmount = order.calculateTotalAmountOfOrder();
        int totalPromotionDiscount = order.calculateTotalAmountOfPromotion();
        int finalAmount = totalOrderAmount - totalPromotionDiscount - membershipDiscountAmount;
        sb.append(String.format("총구매액\t\t\t\t%d\t\t%,d\n", totalOrderQuantity, totalOrderAmount));
        sb.append(String.format("행사할인\t\t\t\t\t\t-%d\n", totalPromotionDiscount));
        sb.append(String.format("멤버십할인\t\t\t\t\t\t-%d\n", membershipDiscountAmount));
        sb.append(String.format("내실돈\t\t\t\t\t\t%,d\n", finalAmount));
    }
}