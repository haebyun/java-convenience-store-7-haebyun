package store.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import store.dto.file.ProductDTO;
import store.exception.ErrorMessage;

public class ProductDataLoader {
    private static final String NO_PROMOTION = "No Promotion";
    private static final String DELIMITER = ",";
    private static final String NULL = "null";

    public List<ProductDTO> loadProducts(String fileName) {
        try (BufferedReader reader = createBufferedReader(fileName)) {
            return readProductData(reader);
        } catch (IOException | NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.OTHER_INVALID_INPUT.getMessage());
        }
    }

    private BufferedReader createBufferedReader(String fileName) throws IOException {
        InputStream is = getFileInputStream(fileName);
        return new BufferedReader(new InputStreamReader(is));
    }

    private InputStream getFileInputStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    private List<ProductDTO> readProductData(BufferedReader reader) throws IOException {
        List<ProductDTO> productDTOs = new ArrayList<>();
        ProductDTO previousProduct = null;
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            previousProduct = addProduct(productDTOs, previousProduct, line);
        }
        return productDTOs;
    }

    private ProductDTO addProduct(List<ProductDTO> products, ProductDTO previousProduct, String line) {
        ProductDTO currentProduct = parseProductLine(line);
        if (previousProduct != null && shouldAddEmptyState(previousProduct, currentProduct)) {
            products.add(createEmptyStateProduct(previousProduct));
        }
        products.add(currentProduct);
        return currentProduct;
    }

    private ProductDTO parseProductLine(String line) {
        String[] parts = line.split(DELIMITER, -1);
        return ProductDTO.of(
                parts[0].trim(),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                parsePromotion(parts[3].trim())
        );
    }

    private String parsePromotion(String promotion) {
        if (promotion.equalsIgnoreCase(NULL) || promotion.isEmpty()) {
            return NO_PROMOTION;
        }
        return promotion;
    }

    private boolean shouldAddEmptyState(ProductDTO previousProduct, ProductDTO currentProduct) {
        return !previousProduct.name().equals(currentProduct.name())
                && !previousProduct.promotionName().equals(NO_PROMOTION);
    }

    private ProductDTO createEmptyStateProduct(ProductDTO product) {
        return ProductDTO.of(
                product.name(),
                product.price(),
                0,
                NO_PROMOTION
        );
    }
}