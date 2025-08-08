package data.generators;

import java.util.Random;

public class INNGenerator {
    // Генерация ИНН в зависимости от типа (физлицо/юрлицо).
    /*
    ИНН физлица true    // 12 цифр
    ИНН юрлица: false   // 10 цифр
     */
    public static String generateInn(boolean isIndividual) {
        return isIndividual ? generateIndividualInn() : generateLegalEntityInn();
    }

    // Генерация ИНН для физлица (12 цифр).
    private static String generateIndividualInn() {
        Random random = new Random();
        int[] innDigits = new int[12];

        // Первые 4 цифры – код региона/ФНС.
        for (int i = 0; i < 4; i++) {
            innDigits[i] = random.nextInt(10);
        }

        // Следующие 6 цифр – номер записи.
        for (int i = 4; i < 10; i++) {
            innDigits[i] = random.nextInt(10);
        }

        // Контрольные цифры (11-я и 12-я).
        innDigits[10] = calculateControlDigit(innDigits, 10, new int[]{7, 2, 4, 10, 3, 5, 9, 4, 6, 8});
        innDigits[11] = calculateControlDigit(innDigits, 11, new int[]{3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8});

        return arrayToString(innDigits);
    }

    // Генерация ИНН для юрлица (10 цифр).
    private static String generateLegalEntityInn() {
        Random random = new Random();
        int[] innDigits = new int[10];

        // Первые 4 цифры – код региона/ФНС.
        for (int i = 0; i < 4; i++) {
            innDigits[i] = random.nextInt(10);
        }

        // Следующие 5 цифр – номер записи.
        for (int i = 4; i < 9; i++) {
            innDigits[i] = random.nextInt(10);
        }

        // Контрольная цифра (10-я).
        innDigits[9] = calculateControlDigit(innDigits, 9, new int[]{2, 4, 10, 3, 5, 9, 4, 6, 8});

        return arrayToString(innDigits);
    }

    // Расчёт контрольной цифры.
    private static int calculateControlDigit(int[] innDigits, int length, int[] weights) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += innDigits[i] * weights[i];
        }
        return (sum % 11) % 10;
    }

    // Преобразование массива цифр в строку.
    private static String arrayToString(int[] digits) {
        StringBuilder sb = new StringBuilder();
        for (int digit : digits) {
            sb.append(digit);
        }
        return sb.toString();
    }
}