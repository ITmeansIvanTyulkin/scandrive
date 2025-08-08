package ordersimportincms;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertchecks.AssertChecks;
import json.extractfromjson.ExtractingFromJson;
import ordersimportincms.pojo.*;
import serialisation.Serialisation;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class OrdersImportInCms extends Serialisation {
    static int id = 0;
    Serialisation serialisation = new Serialisation();
    private static final Logger LOGGER = Logger.getLogger(OrdersImportInCms.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для создания исходящей точки.")
    @Step("Создаю исходящую точку.")
    public void createOutComingPoint() {
        String json = tryToCreateOutComingPoint();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Успешно");
    }

    @Description("Метод для создания входящей точки.")
    @Step("Создаю входящую точку.")
    public void createInComingPoint() {
        String json = tryToCreateInComingPoint();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Успешно");
    }

    @Description("Базовый сценарий с предусловиями: создание исходящей и входящей точек, на их основе создание маршрута.")
    @Step("Создаю исходящую точку, создаю входящую точку, на их основе создаю маршрут.")
    public void createRoute() {
        // Создаю исходящую точку.
        createOutComingPoint();
        // Создаю входящую точку.
        createInComingPoint();

        // Основной сценарий.
        String json = tryToCreateRoute();
        // Парсинг 'id' маршрута для удаления тестовых данных.
        String response = ApiMethods.sendPostRequest(Swagger.MAKE_A_ROUTE, json);
        id = (int) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class)
                .orElseThrow(() ->
                        new RuntimeException("Не удалось извлечь ID из ответа"));

        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Успешно");
    }

    @Description("Метод для создания входящей и исходящей точек, создания маршрута и отправке данных в CMS.")
    @Step("Создаю точки - входящую и исходящую, создаю маршрут, отправляю данные в CMS.")
    public void importToCms() {
        // Создаю исходящую точку, создаю входящую точку, на их основе создаю маршрут.
        createRoute();
        // Отправляю данные в CMS.
        String json = tryToImportToCms();
        ApiMethods.sendPostRequest(Swagger.IMPORT_ORDER_TO_CMS, json);
    }

    @Description("Метод для удаления тестового маршрута, а также всех созданных точек - исходящей и входящей.")
    @Step("Удаляю маршрут и затем входящую и исходящую точки.")
    public void deleteTestData() throws Exception {
        // Удаляю созданный тестовый маршрут.
        deleteRoute();
        // Последовательно удаляю все существующие тестовые точки.
        serialisation.cleanAllPointsInDmBus();
        // Удаляю тестовые данные из базы данных 'CMS' таблиц 'datamatrix' и 'production_tasks'.
        deleteDataFromDatabase();
    }

    @Step("Удаляю созданный тестовый маршрут.")
    public void deleteRoute() {
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_A_ROUTE, String.valueOf(id));
        // Проверки.
        // Проверка по сообщению от сервера.
        AssertChecks.assertCheck(response, "Успешно");
    }

    @Description("Удаление тестовых данных из базы данных 'CMS' таблиц 'datamatrix' и 'production_tasks'.")
    @Step("Удаляю тестовые данные из базы данных 'CMS' таблиц 'datamatrix' и 'production_tasks'.")
    public void deleteDataFromDatabase() throws SQLException {
        // Удаление тестовых данных из базы данных из таблицы 'datamatrix' из базы 'CMS'.
        SqlMethods.cleanDatabase(
                SQLCode.DELETE_TEST_DATA_FROM_CMS_DATAMATRIX,
                Credentials.DATABASE_CMS,
                Credentials.DATABASE_CMS_LOGIN,
                Credentials.DATABASE_CMS_PASSWORD
        );

        // Удаление тестовых данных из базы данных из таблицы 'production_tasks' из базы 'CMS'.
        SqlMethods.cleanDatabase(
                SQLCode.DELETE_TEST_DATA_FROM_CMS_PRODUCTION_TASKS,
                Credentials.DATABASE_CMS,
                Credentials.DATABASE_CMS_LOGIN,
                Credentials.DATABASE_CMS_PASSWORD
        );
    }

// PRIVATE ZONE.
    @Description("Вынос полей для формирования 'JSON' для создания исходящей точки.")
    @Step("Произвожу сериализацию для создания исходящей точки.")
    private String tryToCreateOutComingPoint() {
        Parameters params = Parameters.builder().build();

        OutComingPoint out = OutComingPoint.builder()
                .name("rest_out")
                .parameters(params)
                .type("REST")
                .url("http://localhost:8081/core-cms/production-tasks")
                .build();

        LOGGER.info(Constants.GREEN + "Идёт процесс создания исходящей точки с именем: " + Constants.RESET +
                Constants.BLUE + out.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(out);
    }

    @Description("Вынос полей для формирования 'JSON' для создания входящей точки.")
    @Step("Произвожу сериализацию для создания входящей точки.")
    private String tryToCreateInComingPoint() {
        Parameters params = Parameters.builder().build();

        InComingPoint in = InComingPoint.builder()
                .name("rest_in")
                .parameters(params)
                .type("REST")
                .url("/order-data")
                .build();

        LOGGER.info(Constants.GREEN + "Идёт процесс создания входящей точки с именем: " + Constants.RESET +
                Constants.BLUE + in.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(in);
    }

    @Description("Вынос полей для формирования 'JSON' для создания маршрута.")
    @Step("Произвожу сериализацию для создания маршрута.")
    private String tryToCreateRoute() {
        AddInfo add_info = AddInfo.builder().build();
        RouteParameters params = RouteParameters.builder()
                .ALGORITHM("kept-upload-production-task-v1")
                .build();

        CreateRoute createRoute = CreateRoute.builder()
                .add_info(add_info)
                .from_endpoint_name("rest_in")
                .name("rest_to_rest")
                .params(params)
                .to_endpoint_name("rest_out")
                .type("REST_TO_REST")
                .build();

        LOGGER.info(Constants.GREEN + "Идёт процесс создания маршрута с именем: " + Constants.RESET +
                Constants.BLUE + createRoute.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(createRoute);
    }

    @Description("Вынос полей для формирования 'JSON' для импортирования заказа. " +
            "ХАРДКОД потому - что и сам разработчик передаёт именно в таком виде на ручку.")
    @Step("Произвожу сериализацию для импортирования заказа.")
    private String tryToImportToCms() {
        // Создаю объект с серийными номерами.
        SerialNumbers serialNumbers = SerialNumbers.builder()
                .validityDate("2025-08-24")
                .sntins(List.of(
                        "010000000000000021Drw7htdd5HHUD\u001D91UZF8\u001D92SnNpOM5Q4jjJC30zLmQGMzYm9ndTnnYny4bkYcRVaok=",
                        "010000000000000021Drw7htdd5HHUv\u001D91UZF0\u001D92SnNpOM5Q4jjJC30zLmQGMzYm9ndTnnYny4bkYcRVaok=",
                        "010000000000000021Drw7htdd5HHUx\u001D91UZF2\u001D92SnNpOM5Q4jjJC30zLmQGMzYm9ndTnnYny4bkYcRVaok=",
                        "010000000000000021Drw7htdd5HHUy\u001D91UZF3\u001D92SnNpOM5Q4jjJC30zLmQGMzYm9ndTnnYny4bkYcRVaok=",
                        "010000000000000021Drw7htdd5HHUC\u001D91UZF7\u001D92SnNpOM5Q4jjJC30zLmQGMzYm9ndTnnYny4bkYcRVaok="
                ))
                .build();

        // Собираю основной объект заказа.
        OrderData orderData = OrderData.builder()
                .orderId("401100000001")
                .gtin("00000000000000")
                .itemCode("000000123")
                .itemName("XXX")
                .batch("PL12501234")
                .productionDate("2025-05-22")
                .expirationDate("2026-05-22")
                .isMarked(true)
                .quantity(5)
                .serialNumbers(List.of(serialNumbers))
                .build();

        // Логирую информацию о создании маршрута.
        LOGGER.info(Constants.GREEN + "Идёт процесс создания маршрута с именем: " + Constants.RESET +
                Constants.BLUE + orderData.getOrderId() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(orderData);
    }
}