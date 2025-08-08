package json.assertjson;

import data.constants.Constants;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;

import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;

public class AssertJson {
    private static final Logger LOGGER = Logger.getLogger(AssertJson.class.getName());

    @Description("Сравнение двух 'JSON' по конкретному полю на идентичность. Сравнивает поля на верхнем уровне JSON.")
    public static void assertJson(String json1, String json2, String fieldName, boolean shouldMatch) {
        JSONObject data1 = new JSONObject(json1);
        JSONObject data2 = new JSONObject(json2);

        String fullNameFromRequest = data1.optString(fieldName);
        String fullNameFromResponse = data2.optString(fieldName);

        if (shouldMatch) {
            // Ожидаем, что поля должны совпадать.
            if (fullNameFromRequest.equals(fullNameFromResponse)) {
                LOGGER.info(Constants.GREEN + "Поле " + Constants.RESET + Constants.BLUE + fieldName + Constants.RESET +
                        Constants.GREEN + " совпадает в обоих JSON." + Constants.RESET);
            } else {
                LOGGER.warning(Constants.RED + "Поле " + Constants.RESET + Constants.BLUE + fieldName + Constants.RESET +
                        Constants.RED + " не совпадает." + Constants.RESET);
                fail("Тест упал");
            }
        } else {
            // Ожидаем, что поля не должны совпадать.
            if (!fullNameFromRequest.equals(fullNameFromResponse)) {
                LOGGER.info(Constants.GREEN + "Поле " + Constants.RESET + Constants.BLUE + fieldName + Constants.RESET +
                        Constants.GREEN + " не совпадает. Всё в порядке." + Constants.RESET);
            } else {
                LOGGER.warning(Constants.RED + "Поле " + Constants.RESET + Constants.BLUE + fieldName + Constants.RESET +
                        Constants.RED + " совпадает, хотя не должно." + Constants.RESET);
                fail("Тест упал");
            }
        }
    }

    @Description("Сравнение двух 'JSON' по конкретному полю на идентичность. Сравнивает поля внутри объекта. " +
            "'Key' - это название того, где происходит поиск, например 'data'.")
    public static void assertJson(String json1, String json2, String key, String fieldName, boolean shouldMatch) {
        JSONObject obj1 = new JSONObject(json1);
        JSONObject obj2 = new JSONObject(json2);

        // Извлекаем значения из data.name
        String value1 = obj1.getJSONObject(key).optString(fieldName);
        String value2 = obj2.getJSONObject(key).optString(fieldName);

        if (shouldMatch) {
            if (value1.equals(value2)) {
                LOGGER.info(Constants.GREEN + "Поле " + fieldName + " совпадает: " + value1 + Constants.RESET);
            } else {
                LOGGER.warning(Constants.RED + "Поле " + fieldName + " не совпадает. "
                        + "Ожидалось: " + value2 + ", получено: " + value1 + Constants.RESET);
                fail("Тест упал: значения не совпадают");
            }
        } else {
            if (!value1.equals(value2)) {
                LOGGER.info(Constants.GREEN + "Поле " + fieldName + " различается (как и ожидалось): "
                        + value1 + " != " + value2 + Constants.RESET);
            } else {
                LOGGER.warning(Constants.RED + "Поле " + fieldName + " совпадает, хотя не должно: "
                        + value1 + Constants.RESET);
                fail("Тест упал: значения совпадают, хотя не должны");
            }
        }
    }

    @Description("Метод для проверки значения boolean-поля в JSON")
    public static void assertBooleanFieldValue(
            String jsonInput,
            String pathToObject,  // например: "data.root_id"
            String key,           // например: "is_locked"
            boolean expectedValue // ожидаемое значение (true/false)
    )
    {
        try {
            // Парсю JSON и идём по вложенному пути.
            JSONObject current = new JSONObject(jsonInput);
            for (String pathPart : pathToObject.split("\\.")) {
                current = current.getJSONObject(pathPart);
            }

            // Получаю значение поля.
            boolean actualValue = current.getBoolean(key);

            // Проверяю соответствие ожидаемому значению.
            assertThat(actualValue)
                    .as("Поле '%s' в пути '%s' должно быть %s", key, pathToObject, expectedValue)
                    .isEqualTo(expectedValue);

        } catch (Exception e) {
            throw new AssertionError(String.format(
                    "Ошибка при проверке поля '%s': %s", key, e.getMessage()), e);
        }
    }
}