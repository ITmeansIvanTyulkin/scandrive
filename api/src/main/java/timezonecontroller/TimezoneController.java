package timezonecontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONObject;
import supportutils.ApiMethods;
import timezonecontroller.pojo.RenewTimezone;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TimezoneController {
    private static final Logger LOGGER = Logger.getLogger(TimezoneController.class.getName());

    // Методы.
    @Description("Метод для получения всех доступных часовых поясов.")
    @Step("Получаю все доступные часовые пояса.")
    public List<String> getAllTimezones() throws SQLException {
        String jsonResponse = ApiMethods.sendGetRequest(Swagger.GET_TIMEZONE_ALL);

        JSONObject json = new JSONObject(jsonResponse);
        JSONArray data = json.getJSONArray("data");

        List<String> timezoneList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            timezoneList.add(data.getString(i));
        }
        // Проверки
        if (timezoneList.isEmpty()) {
            throw new RuntimeException("Список часовых поясов пуст");
        }

        if (!timezoneList.contains("Europe/Moscow")) {
            throw new RuntimeException("Europe/Moscow не найден");
        }
        return timezoneList;
    }

    @Description("Метод для получения текущего часового пояса.")
    @Step("Получаю текущий часовой пояс.")
    public String getCurrentTimezone() throws SQLException {
        String json = ApiMethods.sendGetRequest(Swagger.GET_CURRENT_TIMEZONE);

        // Получаем Optional<Object> с часовым поясом.
        Optional<Object> timezoneOpt = ExtractingFromJson.extractingAnyFieldFromJson(json, "timezoneId", String.class);

        // Преобразуем и проверяем значение.
        String currentTimezone = timezoneOpt
                .map(obj -> (String) obj)  // Безопасное приведение типа
                .orElseThrow(() -> new RuntimeException("Часовой пояс не найден в ответе"));

        // Проверка, что часовой пояс не пустой.
        if (currentTimezone == null || currentTimezone.isEmpty()) {
            throw new RuntimeException("Часовой пояс не должен быть пустым");
        }

        // Получаем список всех допустимых поясов.
        List<String> allTimezones = getAllTimezones();

        // Проверяем, что текущий пояс есть в списке.
        if (!allTimezones.contains(currentTimezone)) {
            throw new RuntimeException("Часовой пояс " + currentTimezone + " отсутствует в списке допустимых");
        }

        LOGGER.info(Constants.GREEN + "Текущий часовой пояс: " + Constants.RESET
                + Constants.BLUE + currentTimezone + Constants.RESET);
        return currentTimezone;
    }

    @Description("Метод для обновления временной зоны (часового пояса).")
    @Step("Обновляю временную зону (часовой пояс).")
    public void timezoneRenewed() throws SQLException, InterruptedException {
        // Сбрасываю часовой пояс на системный.
        useSystemTimezone();
        // Получаю текущий часовой пояс.
        String currentTimezone1 = getCurrentTimezone();
        LOGGER.info(Constants.GREEN + "Текущий часовой пояс: " + Constants.RESET + Constants.BLUE + currentTimezone1 + Constants.RESET);
        // Обновляю часовой пояс.
        TimeUnit.SECONDS.sleep(5);
        String json = tryToRenewTimezones();
        ApiMethods.sendPostRequest(Swagger.RENEW_TIMEZONE, json);
        // Проверки.
        // Проверяем текущий часовой пояс (он должен быть изменён на тот, что мы передавали при сериализации).
        String currentTimezone2 = getCurrentTimezone();
        if (currentTimezone1.equals(currentTimezone2)) {
            throw new RuntimeException("Часовой пояс не изменился. Текущий: " + currentTimezone2);
        } else {
            LOGGER.info(Constants.GREEN + "Часовой пояс изменился. Сейчас часовой пояс соответствует: " + Constants.RESET + Constants.BLUE + currentTimezone2 + Constants.RESET);
        }
    }

    @Description("Метод для использования системного часового пояса.")
    @Step("Использую системный часовой пояс.")
    public void useSystemTimezone() {
        String json = "";
        ApiMethods.sendPostRequest(Swagger.USE_SYSTEM_TIMEZONE, json);
    }

    @Description("Выбор случайного часового пояса из коллекции.")
    private String getRandomTimezone() throws SQLException {
        List<String> timezones = getAllTimezones();
        if (timezones == null || timezones.isEmpty()) {
            throw new IllegalStateException("Список часовых поясов пуст.");
        }
        return timezones.get(new Random().nextInt(timezones.size()));
    }

    @Description("Вынос полей для формирования 'JSON' для обновления временных зон.")
    private String tryToRenewTimezones() throws SQLException {
        RenewTimezone renewTimezone = new RenewTimezone();
        renewTimezone.setElementName(getRandomTimezone());

        Gson gson = new Gson();
        return gson.toJson(renewTimezone);
    }
}