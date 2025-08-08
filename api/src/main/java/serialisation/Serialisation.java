package serialisation;

import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import date.GetDate;
import endpointcontroller.pojo.AddingNewPoint;
import endpointcontroller.pojo.Parameters;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import serialisation.pojo.Add_info;
import serialisation.pojo.AdditionalInfoController;
import serialisation.pojo.Rout;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.fail;

public class Serialisation {
    public static String idRouteToDelete;
    private static final Logger LOGGER = Logger.getLogger(Serialisation.class.getName());

    // Методы.
    @Description("Метод добавляет новую тестовую точку 'TCP', проверяет успех создания по статус-коду 200.")
    @Step("Добавляю новую тестовую точку, осуществляю проверку успешности.")
    public String addNewTcpPoint() throws SQLException {
        String json = tryToAddNewPoint("TCP");
        String response = ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json);
        String name = ExtractingFromJson.extractingAnyFieldFromJson(response, "data", "name");
        return name;
    }

    @Description("Метод добавляет новую тестовую точку 'RABBIT_MQ', проверяет успех создания по статус-коду 200.")
    @Step("Добавляю новую тестовую точку, осуществляю проверку успешности.")
    public String addNewRabbitPoint() {
        String json = tryToAddNewRabbitPoint("RABBIT_MQ");
        String response = ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json);
        String name = ExtractingFromJson.extractingAnyFieldFromJson(response, "data", "name");
        return name;
    }

    @Description("Метод создаёт маршрут на основании 2-х ранее созданных точек.")
    @Step("Создаю маршрут.")
    public void makeRoute() throws SQLException, InterruptedException {
        String json = tryToMakeRoute();
        String response = ApiMethods.sendPostRequest(Swagger.MAKE_A_ROUTE, json);
        var id = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class).get();
        idRouteToDelete = String.valueOf(id);
        LOGGER.info(Constants.GREEN + "Создан маршрут с ID: " + Constants.RESET + Constants.BLUE + idRouteToDelete + Constants.RESET);
    }

    @Description("Метод для удаления всех существующих точек последовательно.")
    @Step("Последовательно удаляю все существующие тестовые точки.")
    public void cleanAllPointsInDmBus() throws Exception {
        // 1. Получаю JSON с ID точек.
        String jsonResponse = parsedIdsToCollection(1, 30);
        // 2. Извлекаю все ID в коллекцию.
        List<String> ids = extractIdsFromJson(jsonResponse);
        // 3. Для каждого ID отправляю DELETE-запрос.
        for (String id : ids) {
            deletePointById(id);
        }
    }

    @Description("Метод для удаления созданного тестового маршрута.")
    @Step("Удаляю созданный тестовый маршрут.")
    public void deleteRoute() throws SQLException {
        if (idRouteToDelete == null || idRouteToDelete.equalsIgnoreCase("null")) {
            LOGGER.info(Constants.DARK_YELLOW + "Маршрут не создавался, тестовые данные отсутствуют, удалять нечего.".toUpperCase() + Constants.RESET);
            return;
        }
        try {
            LOGGER.info(Constants.GREEN + "Пытаюсь удалить маршрут с ID: " + Constants.RESET + Constants.BLUE + idRouteToDelete + Constants.RESET);
            String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_A_ROUTE, idRouteToDelete);

            // Проверяем ответ сервера на наличие ошибки 500.
            if (response.contains("\"code\":500") && response.contains("NumberFormatException")) {
                LOGGER.info(Constants.GREEN + "Маршрут не существует, удалять нечего." + Constants.RESET);
            } else {
                LOGGER.info(Constants.GREEN + "Маршрут с ID " + idRouteToDelete + " успешно удален" + Constants.RESET);
            }
        } catch (Exception e) {
            // Обрабатываем случай, когда сервер возвращает 500 ошибку.
            if (e.getMessage() != null && e.getMessage().contains("NumberFormatException")) {
                LOGGER.info(Constants.GREEN + "Маршрут не существует, удалять нечего." + Constants.RESET);
            } else {
                LOGGER.severe(Constants.RED + "Ошибка при удалении маршрута: " + e.getMessage() + Constants.RESET);
                throw e;
            }
        }
    }

    @Description("Метод проверки факта, есть ли забытые/неудалённые тестовые данные и если они есть, удаляет перед " +
            "классом, чтобы запустить тесты с чистого листа.")
    @Step("Проверяю, есть ли старые тестовые данные и удаляю их перед запуском тестов.")
    public static void cleaningDatabaseBeforeTests() throws SQLException {
        // Проверяю, есть ли забытые или созданные коллегами тестовые данные (маршруты).
        String response = ApiMethods.sendGetRequest(Swagger.CONTEXT_SEARCH + "page_number=1&page_size=30");
        List<Integer> routeIds = ExtractingFromJson.extractRouteIdsFromJson(response);

        if (routeIds.isEmpty()) {
            LOGGER.info(Constants.GREEN + "Нет маршрутов для удаления.".toUpperCase() + Constants.RESET);
            return;
        }

        // Проверяю, все ли ID одинаковые.
        Set<Integer> uniqueIds = new HashSet<>(routeIds);
        boolean allIdsEqual = (uniqueIds.size() == 1);

        // Если все ID одинаковые - удаляю только первый.
        if (allIdsEqual) {
            int idToDelete = routeIds.get(0);
            ApiMethods.sendDeleteRequest(Swagger.DELETE_A_ROUTE, String.valueOf(idToDelete));
            LOGGER.info(Constants.GREEN + "Удалён маршрут с ID: " + Constants.RESET + Constants.BLUE + idToDelete + Constants.RESET);
        }
        // Если ID разные - удаляю по порядку.
        else {
            for (int id : routeIds) {
                ApiMethods.sendDeleteRequest(Swagger.DELETE_A_ROUTE, String.valueOf(id));
                LOGGER.info(Constants.GREEN + "Удалён маршрут с ID: " + Constants.RESET + Constants.BLUE + id + Constants.RESET);
            }
        }
    }

    @Description("Метод обновляет тестовую точку 'TCP', проверяет успех обновления по статус-коду 200.")
    @Step("Обновляю тестовую точку, осуществляю проверку успешности.")
    public void updateNewPoint() throws Exception {
        // Создаю новую тестовую точку.
        addNewTcpPoint();
        TimeUnit.SECONDS.sleep(5);
        // Получаю JSON с ID точки.
        String jsonResponse = parsedIdsToCollection(1, 30);
        String id = getAnyField(jsonResponse);
        // Произвожу обновление имени точки.
        String request = tryToUpdateTcpPoint("TCP");
        TimeUnit.SECONDS.sleep(5);
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_POINT_INFO + id + "?is_forced=false", request);
        // Проверка, что точка обновлена и имеет в теле 'UPDATED'.
        TimeUnit.SECONDS.sleep(5);
        String expected = "UPDATED";
        String updated = ExtractingFromJson.extractingAnyFieldFromJson(response, "data", "name");
        if (updated.contains(expected)) {
            LOGGER.info(Constants.GREEN + "Обновлённое название содержит ожидаемое значение: " + Constants.RESET + Constants.BLUE + updated + Constants.RESET + Constants.GREEN + ". Точка обновлена успешно!" + Constants.RESET);
        } else {
            fail("Ожидалось сообщение: '" + expected + "', но не получено!'");
        }
    }

    @Description("Метод добавления новой (дополнительной) информации к созданному маршруту.")
    @Step("Добавляю дополнительную информацию к существующему маршруту.")
    public void addNewInfoToExistingRoute() throws SQLException, InterruptedException {
        // Добавляю дополнительную информацию к маршруту.
        String json = tryToAddNewInfoToRoute();
        ApiMethods.sendPostRequest(Swagger.ADD_NEW_INFO_TO_ROUTE, json);
    }

    @Description("Метод удаления дополнительной информации из созданного маршрута.")
    @Step("Удаляю дополнительную информацию из существующего маршрута.")
    public void deleteAdditionalInfoFromExistingRoute() throws SQLException, InterruptedException {
        // Добавляю дополнительную информацию к маршруту.
        String json = tryToAddNewInfoToRoute();
        ApiMethods.sendPostRequest(Swagger.ADD_NEW_INFO_TO_ROUTE, json);
        // Удаляю дополнительную информацию из маршрута.
        String updatedJson = tryToDeleteAdditionalInfoFromExistingRoute();
        ApiMethods.sendPostRequest(Swagger.DELETE_ADDITIONAL_INFO_FROM_ROUTE, updatedJson);
    }

    @Description("Метод для осуществления контекстного поиска с пагинацией.")
    private static String parsedIdsToCollection(int pageNumber, int pageSize) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return ApiMethods.sendGetRequest(Swagger.CONTEXT_SEARCH, paramString);
    }

    @Description("Извлекает ID из JSON-ответа в виде коллекции строк.")
    private static List<String> extractIdsFromJson(String json) throws Exception {
        List<Integer> intIds = ExtractingFromJson.extractingAnyFieldFromJson(json);
        return intIds.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Description("Метод для удаления точки по ID: отправляю DELETE-запрос для удаления точек с ID = {id}.")
    private static void deletePointById(String id) {
        try {
            String endpoint = String.format("%s", Swagger.DELETE_POINT);
            String response = ApiMethods.sendDeleteRequest(endpoint, id);

            // Логирую результат для отладки.
            System.out.printf("Удаление точки ID=%s. Ответ: %s%n", id, response);
        } catch (Exception e) {
            System.err.printf("Ошибка при удалении точки ID=%s: %s%n", id, e.getMessage());
        }
    }

    @Description("Парсинг для получения 'id' точки.")
    private String getAnyField(String json) throws Exception {
        List<Integer> ids = ExtractingFromJson.extractingAnyFieldFromJson(json);
        int id = ids.get(0);
        return Integer.toString(id);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления новой тестовой точки типа 'TCP'.")
    private String tryToAddNewPoint(String type) {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("Test " + GetDate.getTimeAndFormat());
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType(type);
        addingNewPoint.setUrl("0.0.0.0:8095");

        Gson gson = new Gson();
        return gson.toJson(addingNewPoint);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления новой тестовой точки типа 'RABBIT_MQ'.")
    private String tryToAddNewRabbitPoint(String type) {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("Test " + GetDate.getTimeAndFormat());
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        parameters.setLOGIN("quest");
        parameters.setPASSWORD("quest");
        parameters.setQUEUE("47");
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType(type);
        addingNewPoint.setUrl("0.0.0.0:8095");

        Gson gson = new Gson();
        return gson.toJson(addingNewPoint);
    }

    @Description("Вынос полей для формирования 'JSON' для создания маршрута.")
    private String tryToMakeRoute() throws SQLException, InterruptedException {
        Rout rout = new Rout();
        Add_info addInfo = new Add_info();
        rout.setAdd_info(addInfo);
        rout.setFrom_endpoint_name(addNewTcpPoint());
        TimeUnit.SECONDS.sleep(3);
        rout.setName("TestRoute");
        Parameters parameters = new Parameters();
        rout.setParams(parameters);
        rout.setTo_endpoint_name(addNewRabbitPoint());
        TimeUnit.SECONDS.sleep(3);
        rout.setType("TCP_ASYNC_SERVER");

        Gson gson = new Gson();
        return gson.toJson(rout);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления тестовой точки типа 'TCP'.")
    private String tryToUpdateTcpPoint(String type) throws SQLException {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("TEST DATA UPDATED");
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType(type);
        addingNewPoint.setUrl("0.0.0.0:9100");

        Gson gson = new Gson();
        return gson.toJson(addingNewPoint);
    }

    @Description("Создаю новый маршрут и вычленяю имя маршрута 'routeName' для последующей передачи и обновления имени.")
    private String tryToMakeRouteToRenew() throws SQLException, InterruptedException {
        String json = tryToMakeRoute();
        String response = ApiMethods.sendPostRequest(Swagger.MAKE_A_ROUTE, json);

        // Извлекаем имя маршрута.
        String routeName = ExtractingFromJson.extractingAnyFieldFromJson(response, "data", "name");
        if (routeName == null || routeName.isEmpty()) {
            throw new RuntimeException("Не удалось получить имя маршрута");
        }

        // Извлекаем ID маршрута.
        int id = (Integer) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class).get();
        idRouteToDelete = String.valueOf(id);

        return routeName;
    }

    @Description("Вынос полей для формирования 'JSON' для добавления дополнительной информации к маршруту.")
    private String tryToAddNewInfoToRoute() throws SQLException, InterruptedException {
        AdditionalInfoController additionalInfoController = new AdditionalInfoController();
        additionalInfoController.setKey("UPDATED");
        additionalInfoController.setRouteName(tryToMakeRouteToRenew());
        additionalInfoController.setValue("UPDATED");

        Gson gson = new Gson();
        String jsonPrepared = gson.toJson(additionalInfoController);
        return "[" + jsonPrepared + "]";
    }

    @Description("Вынос полей для формирования 'JSON' для удаления дополнительной информации из маршрута.")
    private String tryToDeleteAdditionalInfoFromExistingRoute() throws SQLException, InterruptedException {
        AdditionalInfoController additionalInfoController = new AdditionalInfoController();
        additionalInfoController.setKey("UPDATED");
        additionalInfoController.setRouteName("Test");
        additionalInfoController.setValue("UPDATED");

        Gson gson = new Gson();
        return gson.toJson(additionalInfoController);
    }
}