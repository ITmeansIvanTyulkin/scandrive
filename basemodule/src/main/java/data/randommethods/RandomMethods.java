package data.randommethods;

import data.constants.Constants;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.units.qual.C;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class RandomMethods {
    public static Set<String> useSymbols = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger(RandomMethods.class.getName());
    @Description("Беру случайное значение.")
    public static String getRandom(List<String> parsed) {
        if (parsed.isEmpty()) {
            throw new IllegalArgumentException(Constants.RED + "Список ID пуст.".toUpperCase() + Constants.RESET);
        }

        Random random = new Random();
        // Генерируем случайный индекс.
        int randomIndex = random.nextInt(parsed.size());
        LOGGER.info(Constants.GREEN + "Выбранный случайный index для запроса: " + Constants.RESET + Constants.BLUE + randomIndex + Constants.RESET);
        // Возвращаем случайный ID.
        return parsed.get(randomIndex);
    }

    @Description("Придумываю рандомное название.")
    public static String createRandomName() {
        int length = 7;
        boolean useLetters = true;
        boolean useNumbers = false;

        String generated;
        do {
            generated = RandomStringUtils.random(length, 0, 0, useLetters, useNumbers, null);
        } while
        (useSymbols.contains(generated));
        useSymbols.add(generated);
        LOGGER.info(Constants.GREEN + "Придумано случайное название: " + Constants.RESET + Constants.BLUE + generated + Constants.RESET);
        return generated;
    }

    @Description("Придумываю рандомные цифры.")
    public static String createRandomDigits(int length) {
        boolean useLetters = false;
        boolean useNumbers = true;

        String generated;
        do {
            generated = RandomStringUtils.random(length, 0, 0, useLetters, useNumbers, null);
        } while
        (useSymbols.contains(generated));
        useSymbols.add(generated);
        LOGGER.info(Constants.GREEN + "Придуманы случайные цифры: " + Constants.RESET + Constants.BLUE + generated + Constants.RESET);
        return generated;
    }
}