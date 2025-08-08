package date;

import io.qameta.allure.Description;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GetDate {
    @Description("Запрашиваю текущее время и форматирую его для отправки в запросе на ручку.")
    public static String getTimeAndFormat() {
        // Получаю текущее время.
        LocalDateTime now = LocalDateTime.now();

        // Определяю формат для преобразования в строку.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");

        // Преобразую текущее время в строку.
        String dateTimeNow = now.format(formatter);
        return dateTimeNow;
    }

    /**
     * Более универсальный метод.
     * Возвращает текущее время в заданном формате.
     *
     * @param utc Если true, возвращает время в формате UTC, иначе в обычном формате.
     * @return Форматированное время.
     */
    public static String getTimeAndFormat(boolean utc) {
        // Определяем формат даты и времени
        DateTimeFormatter formatter;
        if (utc) {
            // Формат для UTC времени
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        } else {
            // Обычный формат времени
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        }

        // Получаем текущее время
        if (utc) {
            // Получаем текущее время в UTC + 1 год.
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).plusYears(1);
            return now.format(formatter);
        } else {
            // Получаем текущее местное время
            LocalDateTime now = LocalDateTime.now();
            return now.format(formatter);
        }
    }
    public static void main(String[] args) {
        System.out.println(getTimeAndFormat());
    }
}