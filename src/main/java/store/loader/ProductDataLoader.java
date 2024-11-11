package store.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import store.dto.file.ProductDTO;
import store.exception.ErrorMessage;

public class ProductDataLoader {
    private final Stack<ProductDTO> nameStack = new Stack<>();

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
        String line;
        boolean isFirstLine = true;
        while ((line = reader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }
            parseProductInfo(line);
        }
        return new ArrayList<>(nameStack);
    }

    private void parseProductInfo(String line) {
        String[] parts = line.split(",", -1);
        String name = parts[0].trim();
        int price = Integer.parseInt(parts[1].trim());
        int quantity = Integer.parseInt(parts[2].trim());
        String promotion = parts[3].trim();

        if (promotion.equalsIgnoreCase("null") || promotion.isEmpty()) {
            promotion = "No Promotion";
        }
        ProductDTO productDTO = ProductDTO.of(name, price, quantity, promotion);
        if(!nameStack.empty()) {
            ProductDTO topProductDTO = nameStack.peek();
            if(!topProductDTO.name().equals(name) && !topProductDTO.promotionName().equals("No Promotion")) {
                ProductDTO emptyProduct = ProductDTO.of(topProductDTO.name(), topProductDTO.price(),
                        0, topProductDTO.promotionName());
                nameStack.add(emptyProduct);
            }
        }
        nameStack.add(productDTO);
    }
}