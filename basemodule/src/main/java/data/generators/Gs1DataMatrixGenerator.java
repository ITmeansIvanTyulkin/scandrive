package data.generators;

import java.util.*;
import java.util.stream.*;

public class Gs1DataMatrixGenerator {
    private static final Random random = new Random();

    // Основной метод генерации N кодов.
    public static List<String> generateMockGs1Codes(int count, boolean includeExpiry, boolean includeBatch, boolean includeCRC) {
        return IntStream.range(0, count)
                .parallel()
                .mapToObj(i -> generateSingleCode(includeExpiry, includeBatch, includeCRC))
                .collect(Collectors.toList());
    }

    // Генерация одного кода.
    private static String generateSingleCode(boolean includeExpiry, boolean includeBatch, boolean includeCRC) {
        StringBuilder sb = new StringBuilder();

        // GTIN (01) - 14 цифр (может начинаться с 460-469, 00-09 и др.).
        String gtin = "4" + String.format("%013d", random.nextInt(1000000000));
        sb.append("(01)").append(gtin);

        // Серийный номер (21) - буквы + цифры.
        String serialNumber = "SN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        sb.append("(21)").append(serialNumber);

        // Срок годности (17) - YYMMDD (опционально).
        if (includeExpiry) {
            String expiryDate = generateRandomExpiryDate();
            sb.append("(17)").append(expiryDate);
        }

        // Номер партии (10) - опционально.
        if (includeBatch) {
            String batchNumber = "B" + random.nextInt(1000);
            sb.append("(10)").append(batchNumber);
        }

        // CRC (91) - 4 символа (опционально).
        if (includeCRC) {
            String crc = String.format("%04X", random.nextInt(65536));
            sb.append("(91)").append(crc);
        }
        return sb.toString();
    }

    // Случайная дата YYMMDD (на 1-5 лет вперед).
    private static String generateRandomExpiryDate() {
        int year = 25 + random.nextInt(5);  // 2025-2030
        int month = 1 + random.nextInt(12); // 1-12
        int day = 1 + random.nextInt(28);  // 1-28 (упрощенно, ибо у нас имеется февраль и високосные года)
        return String.format("%02d%02d%02d", year, month, day);
    }

    // Пример использования.
    public static void main(String[] args) {
        // Генерация 5 тестовых кодов (со сроком годности, партией, но без CRC).
        List<String> mockCodes = generateMockGs1Codes(5, true, true, false);

        // Вывод в JSON-формате.
        System.out.println("[\n  \"" + String.join("\",\n  \"", mockCodes) + "\"\n]");
    }
}