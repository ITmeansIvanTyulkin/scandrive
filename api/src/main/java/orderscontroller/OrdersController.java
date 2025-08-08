package orderscontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import data.randommethods.RandomMethods;
import date.GetDate;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertjson.AssertJson;
import json.extractfromjson.ExtractingFromJson;
import orderscontroller.pojo.AddOrder;
import orderscontroller.pojo.UpdateOrder;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrdersController {
    private static String gtin;
    private static final Logger LOGGER = Logger.getLogger(OrdersController.class.getName());

    // Методы.
    @Description("Метод с пагинацией для осуществления контекстного поиска заказов контроллера.")
    @Step("Осуществляю контекстный поиск заказов контроллера с пагинацией.")
    public String searchForControllers(int pageNumber, int pageSize) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_CONTROLLERS_ORDERS, paramString);
    }

    @Description("Метод для добавления заказа контроллера.")
    @Step("Добавляю новый заказ контроллера.")
    public int addControllerOrder() throws SQLException {
        String json = tryToAddControllerOrder();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_CONTROLLER_ORDER, json);
        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The order was successfully created";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка, что в базе данных появился заказ контроллера по его 'id'.
        // Распарсиваю 'id' созданного заказа.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            // Проверяю в базе данных его существование.
            String idToCheck = String.valueOf((id));
            SqlMethods.isExist(
                    SQLCode.ORDER_EXISTS,
                    idToCheck,
                    Credentials.DATABASE_CONVEYOR_CORE,
                    Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                    Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
            );
            return id;
        }
        throw new RuntimeException("Не удалось получить ID заказа контроллера из ответа сервера");
    }

    @Description("Метод для получения информации о заказе контроллера по его 'id'.")
    @Step("Получаю информацию о заказе контроллера по его 'id'.")
    public String getOrderControllerInfo() throws SQLException {
        int id = addControllerOrder();
        return ApiMethods.sendGetRequest(Swagger.GET_ORDER_INFO + id);
    }

    @Description("Метод для обновления информации о заказе контроллера по его 'id'.")
    @Step("Обновляю информацию о заказе контроллера по его 'id'.")
    public void updateOrderController() throws SQLException {
        // Создаю заказ контроллера.
        String request = getOrderControllerInfo();
        // Распарсиваю 'id' созданного заказа.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(request, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();

        // Обновляю созданный ранее заказ контроллера.
            String json = tryToUpdateControllerOrder();
            String response = ApiMethods.sendPutRequest(Swagger.UPDATE_ORDER_INFO + id, json);

        // Проверки.
            // 1. Проверка по сообщению от сервера.
            String expectedMessage = "The order was successfully updated";
            String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
            assertThat(actualMessage)
                    .as("Проверка сообщения об успехе.")
                    .isEqualTo(expectedMessage);

            // 2. Сравниваю 2 'JSON' - изначальный и после обновления по полю 'order_number' (ждём false - они должны отличаться).
            AssertJson.assertJson(request, json, "order_number", false);
        }
    }

    @Description("Метод для удаления заказа контроллера по его 'id'.")
    @Step("Удаляю информацию о заказе контроллера по его 'id'.")
    public void deleteOrderController() throws SQLException {
        // Создаю заказ контроллера.
        int id = addControllerOrder();
        String idToDelete = String.valueOf((id));
        // Удаляю заказ контроллера.
        ApiMethods.sendDeleteRequest(Swagger.DELETE_ORDER_INFO, idToDelete);
    }

    @Description("Метод для удаления тестовых данных из базы данных.")
    @Step("Удаляю все тестовые данные из базы данных.")
    public static void deleteTestData() throws SQLException {
        // Удаляю данные из таблицы 'orders'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_ORDERS,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
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

    @Description("Вынос полей для формирования 'JSON' для добавления заказа контроллера.")
    @Step("Произвожу сериализацию для добавления заказа контроллера.")
    private String tryToAddControllerOrder() {
        gtin = gtinGenerator(1);

        AddOrder addOrder = AddOrder.builder()
                .is_code_validation_enabled(true)
                .nomenclature("Something written. Doesn't matter what.")
                .order_number(RandomMethods.createRandomDigits(2))
                .reassembly_aggregate_enabled(true)
                .sku("Something again.")
                .creation_date(GetDate.getTimeAndFormat(true))
                .gtin(gtin)
                .is_aggregation_from_subOrder(true)
                .is_load_codes_from_orchestrator_enabled(true)
                .level(1)
                .sub_order_number(RandomMethods.createRandomDigits(3))
                .user("TEST: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Заказ контроллера успешно добавлен: " + Constants.RESET + Constants.BLUE + addOrder.getUser() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addOrder);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления заказа контроллера.")
    @Step("Произвожу сериализацию для обновления заказа контроллера.")
    private String tryToUpdateControllerOrder() {
        UpdateOrder updateOrder = UpdateOrder.builder()
                .is_code_validation_enabled(true)
                .nomenclature("UPDATED")
                .order_number("UPDATED")
                .reassembly_aggregate_enabled(true)
                .sku("UPDATED")
                .creation_date(GetDate.getTimeAndFormat(true))
                .gtin(gtin)
                .is_aggregation_from_subOrder(true)
                .is_load_codes_from_orchestrator_enabled(true)
                .level(2)
                .sub_order_number(RandomMethods.createRandomDigits(3))
                .user("UPDATED")
                .build();

        LOGGER.info(Constants.GREEN + "Заказ контроллера успешно обновлён: " + Constants.RESET + Constants.BLUE + updateOrder.getOrder_number() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updateOrder);
    }
}