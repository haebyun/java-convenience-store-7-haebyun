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

    public void updateProductStock(String productName, Integer quantity) {
        validateStock(productName, quantity);
        List<Product> matchedProducts = findAndSortProductsByPromotion(productName);
        decreaseProductStock(matchedProducts, quantity);
    }

    private void validateStock(String productName, Integer quantity) {
        if (isOverStock(productName, quantity)) {
            throw new IllegalStateException(ErrorMessage.EXCEED_STOCK_QUANTITY.getMessage());
        }
    }

    private List<Product> findAndSortProductsByPromotion(String productName) {
        return products.stream()
                .filter(product -> productName.equals(product.getName()))
                .sorted(Comparator.comparing(Product::hasPromotion).reversed())
                .toList();
    }

    private void decreaseProductStock(List<Product> products, Integer quantity) {
        for (Product product : products) {
            if (quantity <= 0) {
                break;
            }
            quantity = product.decreaseStock(quantity);
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

    public String findPromotionNameByProductName(String productName) {
        return products.stream()
                .filter(product -> productName.equals(product.getName()) && product.hasPromotion())
                .map(Product::getPromotionName)
                .findFirst()
                .orElse("No Promotion");
    }
}
