package suborderscontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import data.randommethods.RandomMethods;
import date.GetDate;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertchecks.AssertChecks;
import json.extractfromjson.ExtractingFromJson;
import orderscontroller.OrdersController;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import suborderscontroller.pojo.Code;
import suborderscontroller.pojo.SubOrder;
import suborderscontroller.pojo.UpdateSubOrder;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SubOrdersController {
    OrdersController ordersController = new OrdersController();
    static String idToUpdate = "";
    private static final Logger LOGGER = Logger.getLogger(SubOrdersController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для создания подзаказа к существующему заказу (сначала создаётся заказ).")
    @Step("Создаю подзаказ.")
    public String createSubOrder() throws SQLException {
        String json = tryToCreateSubOrder();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_SUB_ORDER, json);
        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "The suborder was successfully created");

        return response;
    }

    @Description("Метод осуществляет контекстный поиск всех существующих подзаказов с пагинацией.")
    @Step("Осуществляю контекстный поиск всех существующих подзаказов с пагинацией.")
    public void searchForSubOrders(int pageNumber, int pageSize) throws SQLException {
        // Осуществляю контекстный поиск.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String response =  ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_SUB_ORDERS, paramString);
        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Successfully");
    }

    @Description("Метод осуществляет вызов другого метода (предисловия) для создания заказа и затем подзаказа, " +
            "парсит его 'id' и по нему получает информацию.")
    @Step("Получаю информацию о подзаказе по его 'id'.")
    public String getSubOrderInfoById() throws SQLException {
        // 1. Преамбула: создаю заказ, подзаказ, распарсиваю 'id' подзаказа.
        int id = 0;
        try {
            String response = createSubOrder();
            id = (int) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class)
                    .orElseThrow(() ->
                            new RuntimeException("Не удалось извлечь ID из ответа"));
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
        // 2. Основной сценарий:  получаю информацию о подзаказе по его 'id'.
        String response = ApiMethods.sendGetRequest(Swagger.GET_SUB_ORDER_INFO_BY_ID + id);
        String idToBring = String.valueOf(id);
        idToUpdate = idToBring;
        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Successfully");

        return idToBring;
    }

    @Description("Метод осуществляет обновление существующего подзаказа.")
    @Step("Обновляю существующий подзаказ.")
    public void updateSubOrder() throws SQLException {
        // Преамбула: создаю заказ, подзаказ.
        String json = tryToUpdateSubOrder();
        // Основной сценарий: обновляю подзаказ - меняю по 'id' подзаказа значение поля 'code_type' с 'SSCC' на 'GS-128'.
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_SUB_ORDER_INFO_BY_ID + idToUpdate, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "The suborder was successfully updated");
        // 2. Проверка, что в поле 'code_type' данные успешно обновились и новое значение вместо 'SSCC' стало 'GS-128'.
        SqlMethods.isValueUpdated(
                SQLCode.UPDATED_IN_CODE_TYPE_EXISTS,
                "sub_orders",
                "code_type",
                String.valueOf(idToUpdate),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод для получения информации о подзаказе по его 'sub_order_number'.")
    @Step("Создаю заказ, подзаказ, распарсиваю 'sub_order_number' подзаказа, затем получаю информацию о подзаказе по его 'sub_order_number'.")
    public void getSubOrderInfoBySubOrderNumber() throws SQLException {
        // 1. Преамбула: создаю заказ, подзаказ, распарсиваю 'sub_order_number' подзаказа.
        int subOrderNumber = 0;
        try {
            String response = createSubOrder();
            subOrderNumber = (int) ExtractingFromJson.extractingAnyFieldFromJson(response, "sub_order_number", Integer.class)
                    .orElseThrow(() ->
                            new RuntimeException("Не удалось извлечь ID из ответа"));
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
        // 2. Основной сценарий:  получаю информацию о подзаказе по его 'sub_order_number'.
        String response = ApiMethods.sendGetRequest(Swagger.GET_SUB_ORDER_INFO_BY_SUB_ORDER_NUMBER + subOrderNumber);
        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Successfully");
    }

    @Description("Метод для удаления подзаказа по его 'id'.")
    @Step("Осуществляю удаление подзаказа по его 'id'.")
    public void deleteSubOrderById() {
        // 1. Преамбула: создаю заказ, подзаказ, распарсиваю 'id' заказа.
        int id = 0;
        try {
            String response = createSubOrder();
            id = (int) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class)
                    .orElseThrow(() ->
                            new RuntimeException("Не удалось извлечь ID из ответа"));
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
        // 2. Основной сценарий:  удаляю информацию о подзаказе по его 'id'.
        String idToBring = String.valueOf(id);
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_SUB_ORDER_BY_ID, idToBring);

        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Successfully");
    }

    @Description("Метод осуществляет очистку базы данных - всех связанных таблиц - от тестовых данных.")
    @Step("Очищаю все связанные таблицы в базе данных от тестовых данных.")
    public static void deleteTestDataFromDatabase() throws SQLException {
        // Удаление тестовых данных из таблицы 'codes_for_validation'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_CODES_FOR_VALIDATION,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Удаление тестовых данных из базы данных из таблицы 'sub_orders'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_SUB_ORDERS,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Удаление тестовых данных из базы данных из таблицы 'orders'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_ORDERS,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

// PRIVATE ZONE.
    @Description("Предварительное условие: создание заказа.")
    @Step("Выполняю предварительное условие тест-кейса: создаю заказ.")
    private String createOrder() throws SQLException {
        int id = ordersController.addControllerOrder();
        String idToBring = String.valueOf(id);
        // Запрос в БД для получения 'order_number'.
        return getOrderNumberFromDatabase(idToBring);
    }

    @Description("Метод для запроса в БД для получения 'order_number'.")
    @Step("Выполняю запрос в БД для получения 'order_number'.")
    private static String getOrderNumberFromDatabase(String idToBring) throws SQLException {
        return SqlMethods.getIdFromDatabase(
                SQLCode.GET_ORDER_NUMBER_FROM_ORDERS + idToBring,
                "order_number",
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

    @Description("Метод генерации кода 'GTIN' и вычленения из него 'GTIN' (01) и идентификатора (21), но без самого серийного номера.")
    private String gtinGenerator(int count) {
        List<String> generatedCodes = Gs1DataMatrixGenerator.generateMockGs1Codes(
                count,  // количество кодов
                false,  // includeExpiry
                false,  // includeBatch
                false   // includeCRC
        );
        return generatedCodes.stream()
                .findFirst()
                .map(code -> code.substring(code.indexOf("(01)") + 4, code.indexOf("(01)") + 4 + 14))
                .orElse("46000000000000");
    }

    @Description("Вынос полей для формирования 'JSON' для создания подзаказа.")
    private String tryToCreateSubOrder() throws SQLException {
        String gtin = gtinGenerator(1);

        Code code = Code.builder()
                .applied_code_date("2025-08-05T07:23:59.319Z")
                .bbd("2025-08-05T07:23:59.319Z")
                .code(RandomMethods.createRandomName())
                .code_type(RandomMethods.createRandomName())
                .creation_date("2025-08-05T07:23:59.319Z")
                .gtin(gtin)
                .level(0)
                .read_code_date("2025-08-05T07:23:59.319Z")
                .build();

        SubOrder subOrder = SubOrder.builder()
                .bbd("2025-08-05T07:23:59.319Z")
                .code_count(null)
                .code_type("SSCC")
                .codes(List.of(code))
                .creation_date("2025-08-05T07:23:59.319Z")
                .gtin(gtin)
                .is_aggregation_from_subOrder(false)
                .is_load_codes_from_orchestrator_enabled(true)
                .level(1)
                .order_number(createOrder())
                .sub_order_number(RandomMethods.createRandomDigits(2))
                .build();

        LOGGER.info(Constants.GREEN + "Подзаказ успешно создан с номером: " + Constants.RESET +
                Constants.BLUE + subOrder.getOrder_number() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(subOrder);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления подзаказа.")
    private String tryToUpdateSubOrder() throws SQLException {
        String gtin = gtinGenerator(1);

        Code code = Code.builder()
                .applied_code_date("2025-08-06T07:23:59.319Z")
                .bbd(GetDate.getTimeAndFormat(true))
                .code(RandomMethods.createRandomName())
                .code_type(RandomMethods.createRandomName())
                .creation_date("2025-08-06T07:23:59.319Z")
                .gtin(gtin)
                .level(0)
                .read_code_date("2025-08-06T07:23:59.319Z")
                .build();

        UpdateSubOrder update = UpdateSubOrder.builder()
                .bbd(GetDate.getTimeAndFormat(true))
                .code_count(null)
                .code_type("GS-128")
                .codes(List.of(code))
                .creation_date("2025-08-06T07:23:59.319Z")
                .gtin(gtin)
                .is_aggregation_from_subOrder(false)
                .is_load_codes_from_orchestrator_enabled(true)
                .level(1)
                .order_number(getSubOrderInfoById())
                .sub_order_number(RandomMethods.createRandomDigits(1))
                .build();

        LOGGER.info(Constants.GREEN + "Подзаказ успешно создан с номером: " + Constants.RESET +
                Constants.BLUE + update.getOrder_number() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(update);
    }
}