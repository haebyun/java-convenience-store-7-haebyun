package store.domain;

import java.util.List;

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
}
