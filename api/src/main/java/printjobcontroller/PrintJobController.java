package printjobcontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import data.randommethods.RandomMethods;
import date.GetDate;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import printjobcontroller.pojo.*;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrintJobController {
    static String name = "";
    static int id = 0;
    private static final Logger LOGGER = Logger.getLogger(PrintJobController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод добавляет задание на печать. Состоит из преамбул, согласно тест-кейсу, и основного сценария.")
    @Step("Добавляю задание на печать.")
    public String addingPrintTask() throws SQLException {
        int idToBring = 0;

        // 1. Преамбула: создаю тестовый принтер.
        name = createPrinter();
        // 2. Преамбула: создаю тестовый заказ.
        id = createOrder();

        // Основной сценарий.
        String json = tryToAddPrintTask();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_PRINT_TASK, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "Added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка на существования задания на печать в базе данных.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToBring = id;
        }
        SqlMethods.isExist(
                SQLCode.PRINT_TASK_EXISTS,
                String.valueOf(idToBring),
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
        return response;
    }

    @Description("Метод поиска информации о задании на печать по 'id'.")
    @Step("Осуществляю поиск информации о задании на печать по 'id'.")
    public int searchForPrintTask() throws SQLException {
        int idToCheck = 0;
        String serverResponse = addingPrintTask();

        // Распарсиваю 'id' из ответа для отправки на ручку.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToCheck = id;
        }

        String response = ApiMethods.sendGetRequest(Swagger.SEARCH_INFO_ABOUT_PRINT_TASK + idToCheck);

        Optional<Object> idOfPrintJob = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "id", Integer.class);
        if (idOfPrintJob.isPresent()) {
            int id = (Integer) idOfPrintJob.get();
            return id;
        }

        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        throw new RuntimeException("Не удалось получить ID из ответа сервера");
    }

    @Description("Метод действий над заданиями: cancel - отменить, finish - завершить.")
    @Step("Осуществляю действия над заданиями: cancel - отменить, finish - завершить.")
    public void tasksActing(boolean is_resend, String action) throws SQLException {
        int id = searchForPrintTask();

        // Делаю запрос.
        Map<String, Object> params = new HashMap<>();
        params.put("is_resend", is_resend);
        params.put("action", action);

        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String url = Swagger.TASKS_ACTS + id + (paramString.isEmpty() ? "" : "?" + paramString);
        String response = ApiMethods.sendPutRequest(url, paramString);

        // Проверка по сообщению от сервера.
        String expectedMessage = "Update successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод создаёт задание, затем осуществляет контекстный поиск задания.")
    @Step("Осуществляю контекстный поиск задания.")
    public void searchForTasks(String created_at_gte,
                               String created_at_lte,
                               int page_number,
                               int page_size,
                               boolean is_closed) throws SQLException {
        // Преамбула: осуществляю действия над заданиями: cancel - отменить, finish - завершить.
        tasksActing(false, "FINISH");

        // Формирую параметры запроса.
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("created_at_gte", created_at_gte);
        params.put("created_at_lte", created_at_lte);
        params.put("page_number", page_number);
        params.put("page_size", page_size);
        params.put("is_closed", is_closed);

        // Собираю query-строку.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String fullUrl = Swagger.CONTEXT_TASK_SEARCH + "?" + paramString;
        String response = ApiMethods.sendGetRequest(fullUrl);

        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод удаляет задание на печать.")
    @Step("Осуществляю удаление задания на печать.")
    public void deletePrintTask() throws SQLException {
        int idToCheck = 0;
        String serverResponse = addingPrintTask();

        // Распарсиваю 'id' из ответа для отправки на ручку.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToCheck = id;
        }

        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_PRINT_TASK, String.valueOf(idToCheck));

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "Deleted successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка, что задание на печать теперь отсутствует в базе данных.
        SqlMethods.isExist(
                SQLCode.PRINT_TASK_EXISTS,
                String.valueOf(idToCheck),
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

    @Description("Метод для удаления всех созданных в процессе регресса тестовых данных из базы данных - всех связанных таблиц.")
    @Step("Удаляю все тестовые данные из базы данных - всех связанных таблиц.")
    public void deleteTestDataFromBd() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_PRINT_JOB,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_DATAMATRIX,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_SUB_ORDERS,
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
    @Step("Преамбула: создаю тестовый принтер.")
    private String createPrinter() throws SQLException {
        String name = "";
        int idToBring = 0;

        String json = tryToCreatePrinter();
        String response = ApiMethods.sendPostRequest(Swagger.CREATING_PRINTER_FOR_JOB_CONTROLLER, json);
        Optional<Object> nameOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "name", String.class);
        if (nameOptional.isPresent()) {
            name = (String) nameOptional.get();
        }

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Device added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 1.2. Проверка на существование созданного принтера в базе данных.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToBring = id;
        }
        SqlMethods.isExist(
                SQLCode.DEVICE_EXISTS,
                String.valueOf(idToBring),
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
        return name;
    }

    @Step("Преамбула: создаю тестовый заказ.")
    private int createOrder() throws SQLException {
        int idToBring = 0;

        String json = tryToCreateOrder();
        String response = ApiMethods.sendPostRequest(Swagger.CREATING_ORDER_FOR_JOB_CONTROLLER, json);

        // Проверки.
        // 2. Проверка по сообщению от сервера.
        String expectedMessage2 = "Data added successfully";
        String actualMessage2 = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage2)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage2);

        // 2.1. Проверка на существование созданного заказа в базе данных.
        Optional<Object> idOptional2 = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (idOptional2.isPresent()) {
            int id = (Integer) idOptional2.get();
            idToBring = id;
        }
        SqlMethods.isExist(
                SQLCode.ORDER_EXISTS_IN_PRINT_JOB_CONTROLLER,
                String.valueOf(idToBring),
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
        return idToBring;
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

    @Description("Вынос полей для формирования 'JSON' для создания принтера.")
    @Step("Произвожу сериализацию для создания принтера.")
    private String tryToCreatePrinter() {
        Attributes attributes = Attributes.builder().build();
        Parameters parameters = Parameters.builder().build();

        AddingPrinter addPrinter = AddingPrinter.builder()
                .attributes(attributes)
                .batch_dm(RandomMethods.createRandomName())
                .counter(0)
                .name("TEST " + RandomMethods.createRandomName())
                .params(parameters)
                .production_date(GetDate.getTimeAndFormat(true))
                .send_printed_codes_to_queue(true)
                .state("PAUSE")
                .type("VIDEOJET")
                .url("127.0.0.1:9100")
                .build();

        LOGGER.info(Constants.GREEN + "Создание принтера произошло успешно: " + Constants.RESET +
                Constants.BLUE + addPrinter.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addPrinter);
    }

    @Description("Вынос полей для формирования 'JSON' для создания заказа.")
    @Step("Произвожу сериализацию для создания заказа.")
    private String tryToCreateOrder() {
        Order order = Order.builder()
                .gtin(gtinGenerator(1))
                .name("ORDER " + RandomMethods.createRandomName())
                .nomenclature("UPDATED")
                .sku(RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Создание заказа произошло успешно: " + Constants.RESET +
                Constants.BLUE + order.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(order);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления задания на печать.")
    @Step("Произвожу сериализацию для добавления задания на печать.")
    private String tryToAddPrintTask() {
        OrderResponse order = OrderResponse.builder()
                .id(id)
                .build();

        AddingPrintTask addingPrintTask = AddingPrintTask.builder()
                .batch(RandomMethods.createRandomName())
                .datamatrix(RandomMethods.createRandomName())
                .name(name)
                .nomenclature(RandomMethods.createRandomName())
                .order(order)
                .plan(0)
                .printed_device(RandomMethods.createRandomName())
                .production_date(GetDate.getTimeAndFormat(true))
                .sku(RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Создание заказа на печать произошло успешно: " + Constants.RESET +
                Constants.BLUE + addingPrintTask.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addingPrintTask);
    }
}