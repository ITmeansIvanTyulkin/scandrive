package monitorcontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertjson.AssertJson;
import json.extractfromjson.ExtractingFromJson;
import monitorcontroller.pojo.AddMonitor;
import monitorcontroller.pojo.Properties;
import monitorcontroller.pojo.UpdateMonitor;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MonitorController {
    private static int id;
    private static final Logger LOGGER = Logger.getLogger(MonitorController.class.getName());

    // Методы.
    @Description("Метод с пагинацией для осуществления контекстного поиска по мониторам.")
    @Step("Осуществляю контекстный поиск по мониторам с пагинацией.")
    public String searchForMonitors(int pageNumber, int pageSize) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_MONITORS, paramString);
    }

    @Description("Метод, в котором вызывается сериализация и настройка (set) полей для передачи на ручку для добавления " +
            "тестового монитора.")
    @Step("Пытаюсь добавить тестовый монитор.")
    public int addingMonitor() throws SQLException {
        String json = tryToAddMonitor();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_TEST_MONITOR, json);
        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The monitor was added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка, что в базе данных появился монитор по его 'id'.
        // Распарсиваю 'id' созданного монитора.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            // Проверяю в базе данных его существование.
            String idToCheck = String.valueOf((id));
            SqlMethods.isExist(
                    SQLCode.EXISTS,
                    idToCheck,
                    Credentials.DATABASE_CONVEYOR_CORE,
                    Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                    Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
            );
            return id;
        }
        throw new RuntimeException("Не удалось получить ID монитора из ответа сервера");
    }

    @Description("Так как база данных всегда поддерживается в чистоте, данный метод с нуля создаёт монитор, парсит его " +
            "'id', затем получает информацию о мониторе по его 'id'.")
    @Step("Получаю информацию о мониторе по его 'id'.")
    public String tryToGetMonitorInfoById() throws SQLException {
        int id = addingMonitor();
        return ApiMethods.sendGetRequest(Swagger.GET_MONITOR_INFO + id);
    }

    @Description("Метод для удаления тестовых данных из базы данных.")
    @Step("Удаляю все тестовые данные из базы данных.")
    public static void deleteTestData() throws SQLException {
        // Удаляю данные из таблицы 'monitor_algorithm_properties'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_MONITOR_ALGORITHM_PROPERTIES,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );

        // Удаляю данные из таблицы 'monitors'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_MONITOR,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод для обновления монитора - был статус 'RUNNING', а стал статус 'ERROR'.")
    @Step("Обновляю статус монитора.")
    public void updateMonitor() throws SQLException {
        String json = tryToUpdateMonitor();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_MONITOR_INFO + id, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The monitor has been successfully updated";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
        // 2. Проверка, что в поле 'статус' разные значения - был статус 'RUNNING', а стал статус 'ERROR' (ждём 'false').
        AssertJson.assertJson(json, response, "status", false);
    }

    @Description("Метод для создания нового монитора, парсинка его 'id' и удаления созданного монитора по его 'id'.")
    @Step("Удаляю монитор по его 'id'.")
    public void deleteMonitorViaApi() throws SQLException {
        // Создаю новый монитор и распарсиваю его 'id'.
        int id = addingMonitor();
        // Удаляю монитор по его 'id'.
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_MONITOR, String.valueOf(id));

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The monitor has been deleted";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
        // 2. Проверка, что в базе данных пусто.
        SqlMethods.isExist(
                SQLCode.EXISTS,
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод для получения всех уровней информации.")
    @Step("Получаю существующие уровни информации.")
    public void getAllLevelsInfo() throws SQLException {
        ApiMethods.sendGetRequest(Swagger.GET_ALL_LEVELS_INFO);
    }

    @Description("Метод для получения всех протоколов монитора.")
    @Step("Получаю все существующие протоколы монитора.")
    public void getAllMonitorProtocols() throws SQLException {
        ApiMethods.sendGetRequest(Swagger.GET_ALL_MONITOR_PROTOCOLS);
    }

    @Description("Метод для получения параметров протоколов.")
    @Step("Получаю все существующие параметры протоколов.")
    public Map<String, String> getProtocolsParameters() {
        final String PROTOCOL_PARAM = "monitor_protocol";
        final List<String> SUPPORTED_PROTOCOLS = List.of(
                "SIMPLE_AGGREGATION",
                "TCP_CLIENT",
                "MODBUS_1",
                "PLC_CONFIG"
        );

        Map<String, String> results = new LinkedHashMap<>(SUPPORTED_PROTOCOLS.size());

        for (String protocol : SUPPORTED_PROTOCOLS) {
            final String requestUrl = Swagger.GET_PROTOCOLS_PARAMETERS + PROTOCOL_PARAM + "=" + protocol;

            try {
                String response = ApiMethods.sendGetRequest(requestUrl);
                results.put(protocol, response);
                LOGGER.info(String.format(Constants.GREEN + "Успешно получены параметры для %s: %s" + Constants.RESET, protocol, response));

            } catch (Exception e) {
                String errorMessage = String.format(Constants.RED + "Ошибка получения параметров для %s: %s" + Constants.RESET, protocol, e.getMessage());
                results.put(protocol, errorMessage);
                LOGGER.severe(errorMessage);
            }
        }
        return results;
    }

    @Description("Метод для получения всех существующих статусов мониторов.")
    @Step("Получаю все существующие статусы монитора.")
    public void getAllMonitorStatuses() throws SQLException {
        ApiMethods.sendGetRequest(Swagger.GET_ALL_MONITOR_STATUSES);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления монитора.")
    @Step("Произвожу сериализацию с тестовым названием.")
    private String tryToAddMonitor() {
        Properties property = Properties.builder()
                .key("REGISTER_FOR_GTIN")
                .value("A good monitor!")
                .build();

        AddMonitor monitor = AddMonitor.builder()
                .error_count(0)
                .inform_level("INFO")
                .name("TEST " + RandomMethods.createRandomName())
                .props(List.of(property))
                .protocol("TCP_CLIENT")
                .status("RUNNING")
                .url("91.109.201.205:" + RandomMethods.createRandomDigits(4))
                .build();

        LOGGER.info(Constants.GREEN + "Монитор успешно добавлен: " + Constants.RESET + Constants.BLUE + monitor.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(monitor);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления монитора.")
    @Step("Произвожу сериализацию для последующего обновления монитора.")
    private String tryToUpdateMonitor() throws SQLException {
        // Создаю монитор и распарсиваю его имя.
        String json = tryToGetMonitorInfoById();
        Optional<Object> name = ExtractingFromJson.extractingAnyFieldFromJson(json, "name", String.class);
        if (name.isPresent()) {
            String parsedName = (String) name.get();

            // Распарсиваю 'id' созданного монитора.
            Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(json, "id", Integer.class);
            if (idOptional.isPresent()) {
                id = (Integer) idOptional.get();

                // Провожу установку значений в поля для сериализаци.
                Properties property = Properties.builder()
                        .key("REGISTER_FOR_GTIN")
                        .value("A good monitor!")
                        .build();

                UpdateMonitor updateMonitor = UpdateMonitor.builder()
                        .error_count(0)
                        .inform_level("INFO")
                        .name(parsedName)
                        .props(List.of(property))
                        .protocol("TCP_CLIENT")
                        .status("ERROR")
                        .url("91.109.201.205:" + RandomMethods.createRandomDigits(4))
                        .build();

                LOGGER.info(Constants.GREEN + "Монитор успешно обновлён: " + Constants.RESET + Constants.BLUE + updateMonitor.getName() + Constants.RESET);

                // Сериализую.
                Gson gson = new Gson();
                return gson.toJson(updateMonitor);
            }
            return json;
        }
        return json;
    }
}