package printercontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertjson.AssertJson;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONObject;
import printercontroller.pojo.AddPrinter;
import printercontroller.pojo.UpdatePrinter;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrinterController {
    private static final Logger LOGGER = Logger.getLogger(PrinterController.class.getName());

    // Методы.
    @Description("Метод для добавления принтера агрегации.")
    @Step("Добавляю принтер агрегации.")
    public int addAggregationPrinter() throws SQLException {
        String json = tryToAddAggregationPrinter();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_AGGREGATION_PRINTER, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The printer has been added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка, что в базе данных появился принтер по его 'id'.
        // Распарсиваю 'id' созданного принтера.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            // Проверяю в базе данных его существование.
            String idToCheck = String.valueOf((id));
            SqlMethods.isExist(
                    SQLCode.PRINTER_EXISTS,
                    idToCheck,
                    Credentials.DATABASE_CONVEYOR_CORE,
                    Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                    Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
            );
            return id;
        }
        throw new RuntimeException("Не удалось получить ID принтера из ответа сервера");
    }

    @Description("Метод с пагинацией для осуществления контекстного поиска принтеров агрегации.")
    @Step("Осуществляю контекстный поиск принтеров агрегации с пагинацией.")
    public String searchForPrinters(int pageNumber, int pageSize) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_AGGREGATION_PRINTERS, paramString);
    }

    @Description("Метод добавляет принтер, затем обращается в БД и забирает в коллекцию все данные таблицы и " +
            "сравнивает данные ответа от сервера с данными из БД.")
    @Step("Добавляю принтер, забираю данные из БД, забираю данные из API, сравниваю данные в API и в БД.")
    public void searchForPrintersViaApiAndCompareDataWithDatabase() throws SQLException {
        // Добавляю принтер.
        addAggregationPrinter();
        // Забираю данные из базы данных.
        List<Map<String, Object>> dbData = SqlMethods.getAllDataFromDataBase(
                SQLCode.GET_DATA_FROM_PRINTER_TABLE,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        String response = searchForPrinters(1, 50);

        // Распарсиваю JSON.
        List<Map<String, String>> apiPrinters = parsePrintersFromJson(response);

        // Проверяю количество записей.
        if (dbData.size() != apiPrinters.size()) {
            throw new AssertionError(Constants.RED + "Количество записей не совпадает: БД = " + Constants.RESET + dbData.size()
                    + Constants.RED + ", API = " + Constants.RESET + apiPrinters.size());
        }

        // Если нет записей - выход.
        if (dbData.isEmpty() && apiPrinters.isEmpty()) {
            LOGGER.info(Constants.GREEN + "Данные БД и API совпадают (оба пустые)!" + Constants.RESET);
            return;
        }

        // Сравниваю каждую запись.
        for (int i = 0; i < dbData.size(); i++) {
            Map<String, Object> dbRow = dbData.get(i);
            Map<String, String> apiPrinter = apiPrinters.get(i);

            compareField("id", dbRow, apiPrinter.get("id"));
            compareField("ip", dbRow, apiPrinter.get("ip"));
            compareField("name", dbRow, apiPrinter.get("name"));
            compareField("port", dbRow, apiPrinter.get("port"));
            compareField("template", dbRow, apiPrinter.get("template"));
            compareField("zpl_sscc_prefix", dbRow, apiPrinter.get("zpl_sscc_prefix"));
            compareField("zpl_gs1_128_prefix", dbRow, apiPrinter.get("zpl_gs1_128_prefix"));
            compareField("zpl_gs1_databar_expanded_prefix", dbRow, apiPrinter.get("zpl_gs1_databar_expanded_prefix"));
            compareField("zpl_gs_separator_replacer", dbRow, apiPrinter.get("zpl_gs_separator_replacer"));
        }
        LOGGER.info(Constants.GREEN + "Данные БД и API совпадают!" + Constants.RESET);
    }

    @Description("Метод для получения информации о принтере по его 'id' (при создании все проверки по БД на существование).")
    @Step("Создаю тестовый принтер агрегации, распарсиваю его 'id' и получаю информацию о принтере по его 'id'.")
    public String getPrinterInfoById() throws SQLException {
        int id = addAggregationPrinter();
        return ApiMethods.sendGetRequest(Swagger.GET_PRINTER_INFO_BY_ID + id);
    }

    @Description("Метод для обновления принтера агрегации.")
    @Step("Обновляю данные принтера агрегации.")
    public void updateAggregationPrinter() throws SQLException {
        // Создаю принтер и затем получаю информацию по его 'id'.
        String request = getPrinterInfoById();

        // Распарсиваю 'id' созданного заказа.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(request, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();

        // Обновляю принтер.
        String json = tryToUpdateAggregationPrinter();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_PRINTER_INFO_BY_ID + id, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
            String expectedMessage = "The printer has been successfully updated";
            String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
            assertThat(actualMessage)
                    .as("Проверка сообщения об успехе.")
                    .isEqualTo(expectedMessage);

        // 2. Сравниваю 2 'JSON' - изначальный и после обновления по полю 'order_number' (ждём false - они должны отличаться).
            AssertJson.assertJson(request, json, "name", false);
        }
    }

    @Description("Метод для удаления принтера агрегации по его 'id'.")
    @Step("Создаю принтер агрегации, распарсиваю его 'id' и затем удаляю принтер по его 'id'.")
    public void deleteAggregationPrinter() throws SQLException {
        int id = addAggregationPrinter();
        ApiMethods.sendDeleteRequest(Swagger.DELETE_PRINTER_INFO, String.valueOf((id)));
    }

    @Description("Метод для удаления тестовых данных из базы данных.")
    @Step("Удаляю все тестовые данные из базы данных.")
    public static void deleteTestData() throws SQLException {
        // Удаляю данные из таблицы 'printer'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_PRINTER,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод парсит JSON-строку (response от API) и преобразует её в список принтеров " +
            "(List<Map<String, String>>), где каждый принтер представлен в виде Map.")
    private List<Map<String, String>> parsePrintersFromJson(String json) {
        List<Map<String, String>> printers = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject printerJson = list.getJSONObject(i);
                Map<String, String> printer = new HashMap<>();

                printer.put("id", getStringValue(printerJson, "id"));
                printer.put("name", getStringValue(printerJson, "name"));
                printer.put("ip", getStringValue(printerJson, "ip"));
                printer.put("port", getStringValue(printerJson, "port"));
                printer.put("template", getStringValue(printerJson, "template"));
                printer.put("zpl_sscc_prefix", getStringValue(printerJson, "zpl_sscc_prefix"));
                printer.put("zpl_gs1_128_prefix", getStringValue(printerJson, "zpl_gs1_128_prefix"));
                printer.put("zpl_gs1_databar_expanded_prefix", getStringValue(printerJson, "zpl_gs1_databar_expanded_prefix"));
                printer.put("zpl_gs_separator_replacer", getStringValue(printerJson, "zpl_gs_separator_replacer"));

                printers.add(printer);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга JSON: " + e.getMessage(), e);
        }
        return printers;
    }

    @Description("Метод извлекает значение поля (key) из JSON-объекта (json) и возвращает его как строку.")
    private String getStringValue(JSONObject json, String key) {
        if (json.has(key) && !json.isNull(key)) {
            Object value = json.get(key);
            return value != null ? value.toString() : null;
        }
        return null;
    }

    @Description("Метод сравнивает значение поля 'fieldName' из данных БД (dbRow) со значением из API (apiValue)." +
            "Если значения не совпадают, выбрасывает исключение 'AssertionError' с детальным сообщением.")
    private void compareField(String fieldName, Map<String, Object> dbRow, String apiValue) {
        Object dbValue = dbRow.get(fieldName);
        String dbStr = dbValue != null ? dbValue.toString() : null;
        String apiStr = apiValue != null ? apiValue : null;

        if (!Objects.equals(dbStr, apiStr)) {
            throw new AssertionError(String.format(
                    "Несовпадение в поле '%s': БД='%s', API='%s'",
                    fieldName, dbStr, apiStr
            ));
        }
    }

    @Description("Вынос полей для формирования 'JSON' для добавления принтера агрегации.")
    @Step("Произвожу сериализацию для добавления принтера агрегации.")
    private String tryToAddAggregationPrinter() {
        AddPrinter addPrinter = AddPrinter.builder()
                .encoding("UTF-8")
                .ip("127.0.0." + RandomMethods.createRandomDigits(1))
                .name("TEST PRINTER: " + RandomMethods.createRandomName())
                .port(RandomMethods.createRandomDigits(4))
                .send_enter_at_end(true)
                .template("TEST: " + RandomMethods.createRandomName())
                .zpl_gs1_128_prefix(RandomMethods.createRandomName())
                .zpl_gs1_databar_expanded_prefix(RandomMethods.createRandomName())
                .zpl_gs_separator_replacer(RandomMethods.createRandomName())
                .zpl_sscc_prefix(RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Заказ контроллера успешно добавлен: " + Constants.RESET + Constants.BLUE + addPrinter.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addPrinter);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления принтера агрегации.")
    @Step("Произвожу сериализацию для обновления принтера агрегации.")
    private String tryToUpdateAggregationPrinter() {
        UpdatePrinter updatePrinter = UpdatePrinter.builder()
                .encoding("UTF-8")
                .ip("127.0.0." + RandomMethods.createRandomDigits(1))
                .name("TEST PRINTER: " + RandomMethods.createRandomName())
                .port(RandomMethods.createRandomDigits(4))
                .send_enter_at_end(true)
                .template("TEST: " + RandomMethods.createRandomName())
                .zpl_gs1_128_prefix(RandomMethods.createRandomName())
                .zpl_gs1_databar_expanded_prefix(RandomMethods.createRandomName())
                .zpl_gs_separator_replacer(RandomMethods.createRandomName())
                .zpl_sscc_prefix(RandomMethods.createRandomName())
                .build();

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updatePrinter);
    }
}