package store.domain;

import java.util.Comparator;
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

    public void updateProductStock(String productName, Integer quantity) {
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
}
