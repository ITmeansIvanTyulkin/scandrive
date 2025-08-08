package routecontrollerv2;

import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertjson.AssertJson;
import org.json.JSONArray;
import org.json.JSONObject;
import routecontrollerv2.pojo.AddInfo;
import routecontrollerv2.pojo.Params;
import routecontrollerv2.pojo.UpdateRoute;
import serialisation.Serialisation;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RouteControllerV2 extends Serialisation {
    Serialisation serialisation = new Serialisation();
    private String from_endpoint_name, to_endpoint_name;
    private static final Logger LOGGER = Logger.getLogger(RouteControllerV2.class.getName());

    // Методы.
    @Description("Метод добавляет новую тестовую точку 'TCP', проверяет успех создания по статус-коду 200.")
    @Step("Добавляю новую тестовую точку, осуществляю проверку успешности.")
    public String addNewTcpPoint() throws SQLException {
        return serialisation.addNewTcpPoint();
    }

    @Description("Метод добавляет новую тестовую точку 'RABBIT_MQ', проверяет успех создания по статус-коду 200.")
    @Step("Добавляю новую тестовую точку, осуществляю проверку успешности.")
    public String addNewRabbitPoint() {
        return serialisation.addNewRabbitPoint();
    }

    @Description("Метод создаёт маршрут на основании 2-х ранее созданных точек.")
    @Step("Создаю маршрут.")
    public void makeRoute() throws SQLException, InterruptedException {
        serialisation.makeRoute();
    }

    @Description("Метод для удаления созданного тестового маршрута.")
    @Step("Удаляю созданный тестовый маршрут.")
    public void deleteRoute() throws SQLException {
        serialisation.deleteRoute();
    }

    @Description("Метод для удаления всех существующих точек последовательно.")
    @Step("Последовательно удаляю все существующие тестовые точки.")
    public void cleanAllPointsInDmBus() throws Exception {
        serialisation.cleanAllPointsInDmBus();
    }

    @Description("Метод проверки факта, есть ли забытые/неудалённые тестовые данные и если они есть, удаляет перед " +
            "классом, чтобы запустить тесты с чистого листа.")
    @Step("Проверяю, есть ли старые тестовые данные и удаляю их перед запуском тестов.")
    public static void cleaningDatabaseBeforeTests() {
        cleaningDatabaseBeforeTests();
    }

    @Description("Метод для осуществления контекстного поиска с пагинацией.")
    public String getRoute(int pageNumber, int pageSize) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);
        params.put("type", "TCP_ASYNC_SERVER");

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        System.out.println(paramString.toUpperCase());

        return ApiMethods.sendGetRequest(Swagger.GET_ROUTE, paramString);
    }

    @Description("Метод для получения информации о маршруте по его 'id'.")
    @Step("Создаю маршрут, затем получаю информацию о нём по его 'id'.")
    public int tryToGetRouteById() throws Exception {
        // 1.
        int id;
        // Создаю маршрут.
        makeRoute();
        // Получаю JSON с маршрутами.
        String json = getRoute(1, 50);

        // 2.
        // Получаю имена точек.
        JSONObject jsonObject = new JSONObject(json);
        JSONArray list = jsonObject.getJSONObject("data").getJSONArray("list");
        if (list.length() > 0) {
            JSONObject route = list.getJSONObject(0);
            this.from_endpoint_name = route.getString("from_endpoint_name");
            this.to_endpoint_name = route.getString("to_endpoint_name");

            // 3.
            // Получаю список всех IDs.
            id = route.getInt("id");
            // Получаю информацию о существующем маршруте по его 'id'.
            ApiMethods.sendGetRequest(Swagger.GET_ROUTE_INFO_BY_ID, String.valueOf(id));
            LOGGER.info(Constants.GREEN + "Создан маршрут с ID: " + Constants.RESET + Constants.BLUE + id + Constants.RESET);
        } else {
            throw new RuntimeException("Не найдено ни одного маршрута");
        }
        return id;
    }

    @Description("Метод на обновление информации о маршруте посредством 'API' по его 'id'.")
    @Step("Обновляю информацию о маршруте по его 'id'.")
    public void tryToUpdateRouteById() throws Exception {
        int id = tryToGetRouteById();
        String json = tryToUpdateRouteInfo();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_ROUTE_INFO + id + "?is_forced=true", json);
        // Проверка, что 2 JSON отличаются по полю 'name' после обновления. Если отличаются (false - не равны), то обновление прошло успешно.
        AssertJson.assertJson(json, response, "name", false);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления информации по маршруту.")
    private String tryToUpdateRouteInfo() throws SQLException {
        AddInfo add_info = new AddInfo();
        Params params = new Params();

        UpdateRoute updateRoute = new UpdateRoute();
        updateRoute.setAdd_info(add_info);
        updateRoute.setFrom_endpoint_name(from_endpoint_name);
        updateRoute.setName("UPDATED");
        updateRoute.setParams(params);
        updateRoute.setTo_endpoint_name(to_endpoint_name);
        updateRoute.setType("TCP_ASYNC_SERVER");

        Gson gson = new Gson();
        return gson.toJson(updateRoute);
    }
}