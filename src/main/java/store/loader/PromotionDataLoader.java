package store.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.dto.file.PromotionDTO;
import store.exception.ErrorMessage;

public class PromotionDataLoader {
    private static final String DELIMITER = ",";

    public List<PromotionDTO> loadPromotions(String fileName) {
        try (BufferedReader reader = createBufferedReader(fileName)) {
            return readPromotionData(reader);
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

    private List<PromotionDTO> readPromotionData(BufferedReader reader) throws IOException {
        List<PromotionDTO> promotionDTOs = new ArrayList<>();
        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            promotionDTOs.add(parsePromotion(line));
        }
        return promotionDTOs;
    }

    private PromotionDTO parsePromotion(String line) {
        String[] parts = line.split(DELIMITER, -1);
        return PromotionDTO.of(
                parts[0].trim(),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                LocalDate.parse(parts[3].trim()),
                LocalDate.parse(parts[4].trim())
        );
    }
}
