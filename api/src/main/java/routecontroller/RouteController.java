package routecontroller;

import com.google.gson.Gson;
import data.endpoints.apiendpoints.Swagger;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import routecontroller.pojo.ResetCount;
import routecontroller.pojo.RestartRoute;
import routecontroller.pojo.RouteNames;
import serialisation.Serialisation;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RouteController extends Serialisation {
    Serialisation serialisation = new Serialisation();
    public static String idRouteToDelete;
    private static final Logger LOGGER = Logger.getLogger(RouteController.class.getName());

    // Методы.
    @Description("Метод для получения всех типов маршрута.")
    @Step("Получаю все существующие типы маршрутов.")
    public List<String> getAllRouteTypes() throws SQLException {
        return Collections.singletonList(ApiMethods.sendGetRequest(Swagger.GET_ALL_ROUTE_TYPES));
    }

    @Description("Метод для получения всех активных маршрутов (технические данные).")
    @Step("Получаю все активные маршруты (технические данные).")
    public String getAllActiveRoutes() throws SQLException {
        return ApiMethods.sendGetRequest(Swagger.GET_ALL_ACTIVE_ROUTES);
    }

    @Description("Метод для получения типов входящих точек.")
    @Step("Получаю типы входящих точек.")
    public void getIncomingPoints() throws SQLException {
        Map<String, String> responses = new LinkedHashMap<>();
        // Получаем JSON-строку из списка (первый и единственный элемент).
        String routeTypesJson = getAllRouteTypes().get(0);

        try {
            // Парсим JSON.
            JSONObject jsonResponse = new JSONObject(routeTypesJson);
            JSONArray dataArray = jsonResponse.getJSONArray("data");

            // Перебираем все типы маршрутов.
            for (int i = 0; i < dataArray.length(); i++) {
                String routeType = dataArray.getString(i);
                try {
                    String response = switch (routeType) {
                        case "TCP_ASYNC_SERVER" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "TCP_ASYNC_SERVER");
                        case "TCP_ASYNC_CLIENT" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "TCP_ASYNC_CLIENT");
                        case "TCP_TO_FOLDER_ASYNC_SERVER" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "TCP_TO_FOLDER_ASYNC_SERVER");
                        case "TCP_TO_CONVEYOR_SYNC_SERVER" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "TCP_TO_CONVEYOR_SYNC_SERVER");
                        case "TCP_TO_FOLDER_ASYNC_CLIENT" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "TCP_TO_FOLDER_ASYNC_CLIENT");
                        case "TCP_SYNC_SERVER" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "TCP_SYNC_SERVER");
                        case "RABBIT" -> ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "RABBIT");
                        case "RABBIT_PLAIN_TRANSFER" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "RABBIT_PLAIN_TRANSFER");
                        case "FOLDER_TO_DATAMATRIX_KEEPER" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "FOLDER_TO_DATAMATRIX_KEEPER");
                        case "TME_AGGREGATION" ->
                                ApiMethods.sendGetRequest(Swagger.GET_INCOMING_POINTS_TYPES + "TME_AGGREGATION");
                        default -> throw new IllegalArgumentException("Неизвестный тип маршрута: " + routeType);
                    };

                    responses.put(routeType, response);
                    LOGGER.info("Получен ответ для типа " + routeType + ": " + response);

                } catch (Exception e) {
                    responses.put(routeType, "Ошибка: " + e.getMessage());
                    LOGGER.warning("Ошибка при запросе для типа " + routeType + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Ошибка при парсинге JSON: " + e.getMessage() + "\nСодержимое JSON: " + routeTypesJson);
            responses.put("PARSE_ERROR", "Ошибка при парсинге JSON: " + e.getMessage());
        }
    }

    @Description("Метод для получения типов исходящих точек.")
    @Step("Получаю типы исходящих точек.")
    public void getOutcomingPoints() {
        List<String> routeTypes = new ArrayList<>();
        routeTypes.add("RABBIT");
        routeTypes.add("RABBIT_PLAIN_TRANSFER");
        routeTypes.add("FOLDER_TO_DATAMATRIX_KEEPER");
        routeTypes.add("TME_AGGREGATION");

        List<String> results = new ArrayList<>();

        for (String routeType : routeTypes) {
            try {
                String response = switch (routeType) {
                    case "RABBIT" -> ApiMethods.sendGetRequest(Swagger.GET_OUTCOMING_POINTS_TYPES + "RABBIT");
                    case "RABBIT_PLAIN_TRANSFER" ->
                            ApiMethods.sendGetRequest(Swagger.GET_OUTCOMING_POINTS_TYPES + "RABBIT_PLAIN_TRANSFER");
                    case "FOLDER_TO_DATAMATRIX_KEEPER" ->
                            ApiMethods.sendGetRequest(Swagger.GET_OUTCOMING_POINTS_TYPES + "FOLDER_TO_DATAMATRIX_KEEPER");
                    case "TME_AGGREGATION" ->
                            ApiMethods.sendGetRequest(Swagger.GET_OUTCOMING_POINTS_TYPES + "TME_AGGREGATION");
                    default -> throw new IllegalArgumentException("Unknown route type: " + routeType);
                };

                results.add("Получен ответ для типа " + routeType + ": " + response);
                LOGGER.info("Получен ответ для типа " + routeType + ": " + response);

            } catch (Exception e) {
                results.add("Ошибка для типа " + routeType + ": " + e.getMessage());
                LOGGER.warning("Ошибка при запросе для типа " + routeType + ": " + e.getMessage());
            }
        }
    }

    @Description("Метод для создания маршрута на основании 2-х точек.")
    @Step("Создаю тестовый маршрут.")
    protected String tryToMakeRoute(int pageNumber, int pageSize) throws SQLException, InterruptedException {
        serialisation.makeRoute();
        // Распарсиваю имя маршрута для последующего перезапуска маршрута.
        // Получаю все существующие точки и сам маршрут.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        String response = ApiMethods.sendGetRequest(Swagger.CONTEXT_SEARCH, paramString);
        List<String> routeNames = extractRouteName(response);
        // С проверкой на пустоту.
        String routeName = !routeNames.isEmpty() ? routeNames.get(0) : "";
        // Распарсиваю 'id' маршрута для последующего удаления тестовых данных.
        var id = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        idRouteToDelete = String.valueOf(id);
        return routeName;
    }

    @Description("Метод для перезапуска существующего маршрута по его имени.")
    @Step("Перезепускаю существующий маршрут, осуществляю проверку по статус-коду, затем удаляю маршрут.")
    public void restartRoute() throws SQLException, InterruptedException {
        String json = tryToRestartRoute();
        ApiMethods.sendPostRequest(Swagger.RESTART_EXISTING_ROUTE, json);
    }

    @Description("Метод для сброса счётчика.")
    @Step("Сбрасываю счётчик по названию существующего маршрута.")
    public void resetCount() throws SQLException, InterruptedException {
        String json = tryToResetCount();
        ApiMethods.sendPostRequest(Swagger.RESET_ROUTE_COUNT, json);
    }

    @Description("Метод для сброса счётчиков маршрутов из списка.")
    @Step("Сбрасываю счётчики для маршрутов из списка.")
    public void resetCounts() throws SQLException, InterruptedException {
        String json = tryToResetCounts();
        ApiMethods.sendPostRequest(Swagger.RESET_ROUTE_COUNT, json);
    }

    @Description("Метод для удаления всех тестовых данных: сначала удаляется созданный и перезапущенный маршрут, " +
            "затем 2 точки - исходящая и входящая.")
    @Step("Удаляю тестовые данные посредством API-запроса: сначала удаляю перезапущенный маршрут, затем точки.")
    public void deleteTestData() throws Exception {
        // Удаляю созданный тестовый маршрут.
        serialisation.deleteRoute();
        // Последовательно удаляю все существующие тестовые точки.
        serialisation.cleanAllPointsInDmBus();
    }

    @Description("Метод парсит и достаёт название маршрута.")
    @Feature("Данный метод не вынесен в общий класс парсеров по причине специфичного одноразового использования.")
    private List<String> extractRouteName(String jsonInput) {
        List<String> names = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonInput);
            JSONArray endpoints = jsonObject.getJSONObject("data").getJSONArray("list");

            for (int i = 0; i < endpoints.length(); i++) {
                JSONObject endpoint = endpoints.getJSONObject(i);

                // Проверяем, есть ли routes_to_incoming_endpoint и не пустой ли он.
                if (endpoint.has("routes_to_incoming_endpoint")) {
                    JSONArray incomingRoutes = endpoint.getJSONArray("routes_to_incoming_endpoint");

                    // Если массив не пустой, берем name из каждого элемента.
                    if (incomingRoutes.length() > 0) {
                        for (int j = 0; j < incomingRoutes.length(); j++) {
                            String routeName = incomingRoutes.getJSONObject(j).getString("name");
                            names.add(routeName);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Ошибка при парсинге JSON", e);
        }
        return names;
    }

    @Description("Вынос полей для формирования 'JSON' для перезапуска существующего маршрута.")
    private String tryToRestartRoute() throws SQLException, InterruptedException {
        RestartRoute restartRoute = new RestartRoute();
        restartRoute.setElementName(tryToMakeRoute(1, 50));

        Gson gson = new Gson();
        return gson.toJson(restartRoute);
    }

    @Description("Вынос полей для формирования 'JSON' для сброса счётчика существующего маршрута.")
    private String tryToResetCount() throws SQLException, InterruptedException {
        ResetCount resetCount = new ResetCount();
        resetCount.setElementName(tryToMakeRoute(1, 50));

        Gson gson = new Gson();
        return gson.toJson(resetCount);
    }

    @Description("Вынос полей для формирования 'JSON' для сброса счётчиков для маршрутов из списка.")
    private String tryToResetCounts() throws SQLException, InterruptedException {
        RouteNames routeNames = new RouteNames();
        String exampleRouteName = tryToMakeRoute(1, 50);
        routeNames.setRoute_names(List.of(exampleRouteName));

        Gson gson = new Gson();
        return gson.toJson(routeNames);
    }
}