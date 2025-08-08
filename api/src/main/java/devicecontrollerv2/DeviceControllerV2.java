package devicecontrollerv2;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import data.randommethods.RandomMethods;
import date.GetDate;
import devicecontrollerv2.pojo.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONObject;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DeviceControllerV2 {
    static String idValue = "";
    private static final Logger LOGGER = Logger.getLogger(DeviceControllerV2.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод проверяет нанесение: добавление принтера. В методе используется 3-х уровневые проверки.")
    @Step("Проверяю нанесение: добавляю принтер, затем осуществляю проверки.")
    public static int addingPrinter() throws SQLException {
        String json = tryTpAddPrinter();
        String response = ApiMethods.sendPostRequest(Swagger.ADDING_PRINTER_DEVICE_CONTROLLER_V2, json);

        Optional<Object> id = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (id.isPresent()) {
            int idToCheck = (Integer) id.get();

            // Проверки.
            // Проверка по сообщению от сервера.
            String expectedMessage = "Device added successfully";
            String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
            assertThat(actualMessage)
                    .as("Проверка сообщения об успехе.")
                    .isEqualTo(expectedMessage);

            // Проверка, что устройство появилось и существует в базе данных.
            SqlMethods.isExist(
                    SQLCode.DEVICE_EXISTS,
                    String.valueOf(idToCheck),
                    Credentials.DATABASE_DATAMATRIX_KEEPER,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD

            );
            return idToCheck;
        }
        throw new RuntimeException("Не удалось получить ID заказа контроллера из ответа сервера");
    }

    @Description("Метод для пагинации и получения списка принтеров.")
    @Step("Получаю список принтеров.")
    public void getPrintersList(int pageNumber, int pageSize) throws SQLException {
        // Добавляю принтер.
        addingPrinter();

        // Делаю запрос.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String response = ApiMethods.sendGetRequest(Swagger.GET_PRINTERS_LIST, paramString);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверяю, что данные, полученные посредством API-запроса полностью совпадают с данными, полученными из БД.
        checkAndCompareDataFromApiAndBd(response);
    }

    @Description("Метод сосотоит из 2-х частей - первая часть - это преамбула со всеми надлежащими проверками, " +
            "вторая часть - основной сценарий с проверками.")
    @Step("Осуществляю получение информации об устройстве по его 'id'.")
    public void getDeviceById() throws SQLException {
        // Преамбула: создаю устройство и распарсиваю его 'id' для тестового сценария (с проверками).
        int id = getDeviceByIdPreamble();

        // Основной сценарий.
        String response = ApiMethods.sendGetRequest(Swagger.GET_DEVICE_BY_ID, String.valueOf(id));

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления устройства по его 'id'.")
    @Step("Создаю тестовое устройство, распарсиваю 'id' из ответа сервера и затем удаляю устройство по его 'id'.")
    public void deleteDeviceById() throws SQLException {
        // Преамбула: создаю устройство и распарсиваю его 'id' для тестового сценария (с проверками).
        int id = getDeviceByIdPreamble();

        // Основной сценарий.
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_DEVICE_BY_ID, String.valueOf(id));

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The device was successfully removed";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для обновления устройства.")
    @Step("Создаю утройство, распарсиваю его 'id', сериализую, затем обновляю устройство.")
    public void updateDevice() throws SQLException {
        String json = tryToCreateOrder();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_ORDER_DEVICE_CONTROLLER, json);

        // Распарсиваю 'id', передаю в глобальную переменную для использования в создании заказа в сериализации.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);

        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idValue = String.valueOf(id);
            LOGGER.info(Constants.GREEN + "ID: " + Constants.RESET + Constants.BLUE + id + Constants.RESET);
        } else {
            LOGGER.warning(Constants.RED + "Поле 'id' не найдено или тип не совпал" + Constants.RESET);
        }

        // Получаю 'id' устройства из базы данных для передачи на ручку.
        String idToBring = getIdOfDeviceFromDatabase();
        String jsonToBring = tryToUpdateDevice();
        String serverResponse = ApiMethods.sendPutRequest(Swagger.UPDATE_DEVICE_CONTROLLER + idToBring, jsonToBring);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Device updated successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления тестовых данных.")
    @Step("Произвожу удаление тестовых данных из базы данных таблиц 'params' и 'device'.")
    public void deleteTestData() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_PARAMS,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
                );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_DEVICE,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

// PRIVATE ZONE.
    @Description("Получаю 'id' из базы данных.")
    @Step("Получаю 'id' устройства из базы данных для передачи на ручку.")
    private String getIdOfDeviceFromDatabase() throws SQLException {
        return SqlMethods.getIdFromDatabase(
                SQLCode.GET_ID_FROM_DATABASE,
                "id",
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

    @Description("Метод для создания устройства и парсинг его 'name'.")
    @Step("Создаю устройство и получаю его 'name'.")
    private String createDeviceAndGetName() {
        // Преамбула: создаю устройство и распарсиваю его 'name'.
        String json = tryToGetDeviceById();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_DEVICE_TO_PARSE_ID, json);

        // Осуществляю парсинг 'name'.
        Optional<Object> name = ExtractingFromJson.extractingAnyFieldFromJson(response, "name", String.class);
        if (name.isPresent()) {
            String nameToCheck = (String) name.get();

            return nameToCheck;
        }
        throw new RuntimeException("Ошибка в получении name!");
    }

    @Description("Метод для создания устройства и парсинг его 'id' с проверками на существование по базе данных " +
            "и сообщению от сервера.")
    @Step("Преамбула: создаю устройство и распарсиваю его 'id' для тестового сценария.")
    private Integer getDeviceByIdPreamble() throws SQLException {
        Integer idToCheck = null;

        // Предварительное условие.
        String json = tryToGetDeviceById();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_DEVICE_TO_PARSE_ID, json);

        // Осуществляю парсинг 'id' для проверки по базе данных.
        Optional<Object> id = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (id.isPresent()) {
            idToCheck = (Integer) id.get();

            // Проверка на существование в базе данных.
            SqlMethods.isExist(
                    SQLCode.TEST_DEVICE_EXISTS,
                    String.valueOf(idToCheck),
                    Credentials.DATABASE_DATAMATRIX_KEEPER,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
            );
        }

            // Проверка по сообщению от сервера.
            String expectedMessage = "Device added successfully";
            String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
            assertThat(actualMessage)
                    .as("Проверка сообщения об успехе.")
                    .isEqualTo(expectedMessage);

        return idToCheck;
    }

    @Step("Проверяю, что данные, полученные посредством API-запроса полностью совпадают с данными, полученными из БД.")
    private void checkAndCompareDataFromApiAndBd(String response) throws SQLException {
        // Забираю данные из базы данных.
        List<Map<String, Object>> dbData = SqlMethods.getAllDataFromDataBase(
                SQLCode.GET_DATA_FROM_DEVICE_TABLE,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
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
            compareField("name", dbRow, apiPrinter.get("name"));
            compareField("url", dbRow, apiPrinter.get("url"));
            compareField("printer_type", dbRow, apiPrinter.get("printer_type"));
            compareField("batch_dm", dbRow, apiPrinter.get("batch_dm"));
            compareField("send_printed_codes_to_queue", dbRow, apiPrinter.get("send_printed_codes_to_queue"));
            compareField("state", dbRow, apiPrinter.get("state"));
        }
        LOGGER.info(Constants.GREEN + "Данные БД и API совпадают!" + Constants.RESET);
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
                printer.put("url", getStringValue(printerJson, "url"));
                printer.put("printer_type", getStringValue(printerJson, "type"));
                printer.put("batch_dm", getStringValue(printerJson, "batch_dm"));
                printer.put("send_printed_codes_to_queue", getStringValue(printerJson, "send_printed_codes_to_queue"));
                printer.put("state", getStringValue(printerJson, "state"));

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

    @Description("Метод генерации кода 'GTIN' и вычленения из него 'GTIN' (01) и идентификатора (21), но без самого серийного номера.")
    private String gtinGenerator(int count) {
        List<String> generatedCodes = Gs1DataMatrixGenerator.generateMockGs1Codes(
                count, // количество кодов
                false, // includeExpiry
                false, // includeBatch
                false // includeCRC
        );
        return generatedCodes.stream()
                .findFirst()
                .map(code -> code.substring(code.indexOf("(01)") + 4, code.indexOf("(01)") + 4 + 14))
                .orElse("46000000000000");
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

    @Description("Вынос полей для формирования 'JSON' для добавления принтера.")
    @Step("Произвожу сериализацию для добавления принтера.")
    private static String tryTpAddPrinter() {
        Attributes attributes = Attributes.builder().build();
        Parameters parameters = Parameters.builder()
                .TEMPLATE(RandomMethods.createRandomName())
                .build();

        AddingPrinter addingPrinter = AddingPrinter.builder()
                .attributes(attributes)
                .batch_dm(RandomMethods.createRandomName())
                .name("TEST: " + RandomMethods.createRandomName())
                .params(parameters)
                .production_date(GetDate.getTimeAndFormat(true))
                .send_printed_codes_to_queue(true)
                .state("RUNNING")
                .type("GODEX")
                .url("192.168.31.124:8500")
                .build();

        LOGGER.info(Constants.GREEN + "Добавление принтера произошло успешно: " + Constants.RESET +
                Constants.BLUE + addingPrinter.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addingPrinter);
    }

    @Description("Вынос полей для формирования 'JSON' для получения устройства по его 'id'.")
    @Step("Произвожу сериализацию для получения устройства по его 'id'.")
    private String tryToGetDeviceById() {
        Attributes attributes = Attributes.builder().build();
        Parameters parameters = Parameters.builder()
                .TEMPLATE(RandomMethods.createRandomName())
                .BUFFER_SIZE("100")
                .build();

        GetDeviceById getDeviceById = GetDeviceById.builder()
                .attributes(attributes)
                .batch_dm(RandomMethods.createRandomName())
                .counter(0)
                .name("PRINTER: " + RandomMethods.createRandomName())
                .params(parameters)
                .production_date(GetDate.getTimeAndFormat(true))
                .send_printed_codes_to_queue(true)
                .state("PAUSE")
                .type("GODEX")
                .url("0.0.0.0:9100")
                .build();

        LOGGER.info(Constants.GREEN + "Получение устройства по его 'id' успешно: " + Constants.RESET +
                Constants.BLUE + getDeviceById.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(getDeviceById);
    }

    @Description("Вынос полей для формирования 'JSON' для создания заказа.")
    @Step("Произвожу сериализацию для создания заказа.")
    private String tryToCreateOrder() {
        CreateOrder createOrder = CreateOrder.builder()
                .gtin(gtinGenerator(1))
                .name(createDeviceAndGetName())
                .nomenclature("UPDATED")
                .sku(RandomMethods.createRandomName())
                .build();

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(createOrder);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления устройства.")
    @Step("Произвожу сериализацию для обновления устройства.")
    private String tryToUpdateDevice() {
        Attributes attributes = Attributes.builder().build();

        Order order = Order.builder()
                .id(Integer.valueOf(idValue))
                .build();

        Parameters paarameters = Parameters.builder()
                .TEMPLATE(RandomMethods.createRandomName())
                .BUFFER_SIZE("100")
                .build();

        UpdateDevice updateDevice = UpdateDevice.builder()
                .attributes(attributes)
                .batch_dm(RandomMethods.createRandomName())
                .order(order)
                .counter(0)
                .name(RandomMethods.createRandomName())
                .params(paarameters)
                .production_date(GetDate.getTimeAndFormat(true))
                .send_printed_codes_to_queue(true)
                .state("PAUSE")
                .type("ADAPTER")
                .url(Swagger.BASE_TEST_PRINTER_IP + ":" + Swagger.BASE_TEST_PRINTER_PORT)
                .build();

        LOGGER.info(Constants.GREEN + "Обновление устройства по его 'id' успешно: " + Constants.RESET +
                Constants.BLUE + updateDevice.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updateDevice);
    }
}