package store.domain.product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import store.domain.vo.PromotionProductInfo;
import store.exception.ErrorMessage;

public class Products {
    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products of(List<Product> products) {
        return new Products(products);
    }

    public Boolean isOverStock(String productName, Integer orderQuantity) {
        int productTotalStock = products.stream()
                .filter(product -> productName.equals(product.getName()))
                .mapToInt(Product::getStockValue)
                .sum();
        return productTotalStock < orderQuantity;
    }

    public Boolean isPromotionProductExist(String productName) {
        return products.stream()
                .anyMatch(product -> productName.equals(product.getName()) && product.hasPromotion());
    }

    public void updateProductStock(String productName, Integer quantity) {
        if (isOverStock(productName, quantity)) {
            throw new IllegalStateException(ErrorMessage.EXCEED_STOCK_QUANTITY.getMessage());
        }
        List<Product> matchedProducts = products.stream()
                .filter(product -> productName.equals(product.getName()))
                .sorted(Comparator.comparing(Product::hasPromotion).reversed())
                .toList();
        for (Product product : matchedProducts) {
            if (quantity > 0) {
                quantity = product.decreaseStock(quantity);
            }
        }
    }

    public PromotionProductInfo getPromotionProductInfo(String productName) {
        Product promotionProduct = products.stream()
                .filter(product -> product.hasPromotion() && productName.equals(product.getName()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        return PromotionProductInfo.from(promotionProduct);
    }

    public Integer findPriceByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .map(Product::getPriceValue)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Boolean existProductByName(String productName) {
        return products.stream()
                .anyMatch(product -> productName.equals(product.getName()));
    }
}
