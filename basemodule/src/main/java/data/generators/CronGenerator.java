package data.generators;

import io.qameta.allure.Description;

public class CronGenerator {
    @Description("Генерирует cron-выражение по заданным параметрам.")
    public static String generateCronExpression(
            String minute,
            String hour,
            String dayOfMonth,
            String month,
            String dayOfWeek
    ) {
        // Проверка на null и пустые значения.
        if (minute == null || hour == null || dayOfMonth == null || month == null || dayOfWeek == null) {
            throw new IllegalArgumentException("Все параметры cron должны быть не null");
        }

        // Собираю cron-выражение.
        return String.join(" ", minute, hour, dayOfMonth, month, dayOfWeek).trim();
    }
}