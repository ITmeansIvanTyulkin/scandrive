package rootlockscontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import json.assertjson.AssertJson;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rootlockscontroller.pojo.BlockingRoute;
import rootlockscontroller.pojo.UpdateTypeAndReason;
import serialisation.Serialisation;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RootLocksController extends Serialisation {
    public static String idToDelete;
    Serialisation serialisation = new Serialisation();
    private static final Logger LOGGER = Logger.getLogger(RootLocksController.class.getName());

    // Методы.
    @Description("Метод для создания маршрута на основании 2-х точек.")
    @Step("Создаю тестовый маршрут.")
    protected String tryToMakeRoute(int pageNumber, int pageSize) throws SQLException, InterruptedException {
        serialisation.makeRoute();
        // Распарсиваю имя маршрута для последующей сериализации для блокировки маршрута.
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
        return routeName;
    }

    @Description("Метод, который создаёт 2 точки и на основании их создаёт маршрут, а затем его блокирует, проверяет, " +
            "что блокировка произошла успешно.")
    @Step("Создаю маршрут, блокирую его, проверяю.")
    public void blockingRoute() throws Exception {
        // 1 часть метода с проверками.
        String json = tryToBlockRoute();
        String response = ApiMethods.sendPostRequest(Swagger.ROUTE_BLOCKING, json);
        // Проверка, что в JSON, пришедшем в ответе от сервера, 'key' имеет статус 'true'.
        // 'Key' - может иметь любое название для универсальности, а в данном случае имеет название 'is_locked'.
        AssertJson.assertBooleanFieldValue(response, "data.root_id", "is_locked", true);

        // 2 часть метода с проверками.
        // Распарсиваю 'id' для последующего удаления.
        Optional<Object> idFromResponse = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);

        if (idFromResponse.isPresent()) {
            // Безопасное приведение типа.
            int id = (Integer) idFromResponse.get();
            idToDelete = Integer.toString(id);
        } else {
            LOGGER.warning(Constants.RED + "ID не найден в JSON" + Constants.RESET);
        }
    }

    @Description("Метод снятия (удаления) блокировки с заблокированного ранее тестового маршрута.")
    @Step("Снимаю (удаляю) блокировку с маршрута.")
    public void tryToUnblockRoute() {
        String response = ApiMethods.sendDeleteRequest(Swagger.ROUTE_UNBLOCKING, idToDelete);
        // Проверка, что в JSON, пришедшем в ответе от сервера, 'key' имеет статус 'false'.
        //AssertJson.assertBooleanFieldValue(response, "data.root_id", "is_locked", false);
    }

    @Description("Метод для удаления тестовых данных: удаляет созданный и разблокированный маршрут, а также все точки.")
    @Step("Удаляю разблокированный маршрут и все точки.")
    public void deleteTestData() throws Exception {
        serialisation.deleteRoute();
        serialisation.cleanAllPointsInDmBus();
    }

    @Description("Метод делает запрос на ручку для получения информации от сервера обо всех заблокированных маршрутах. " +
            "Метод имеет проверку по коду 200 внутри себя и состоит после проверки из 2 частей: 1 часть для вывода " +
            "значения обо всех существующих на данный момент заблокированных маршрутах, 2 часть для вывода значения " +
            "поля 'total_records' для вывода в отчёт общей цифры о заблокированных маршрутах.")
    @Step("Получаю список установленных блокировок на маршруты.")
    public String getAllRouteBlocksList(int pageNumber, int pageSize) throws Exception {
        // Устанавливаю параметризацию в соответствии с тест-кейсом.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString для параметризированного GET-запроса.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // Делаю запрос.
        String response = ApiMethods.sendGetRequest(Swagger.GET_BLOCKS_LIST, paramString);
        List<Integer> ids = ExtractingFromJson.extractingAnyFieldFromJson(response);

        // 1 часть.
        // Возвращаю список всех id из элементов list.
        LOGGER.info(Constants.GREEN + "Блокировка(-и) маршрута(-ов) осуществлена(-ы) под следующим(-ими) ID: " + Constants.RESET + Constants.BLUE + ids + Constants.RESET);
        for (int id : ids) {
            LOGGER.info(Constants.GREEN + "Все ID: " + Constants.RESET + Constants.BLUE + id + Constants.RESET);
        }

        // 2 часть.
        // Произвожу парсинг значений поля 'total_records' для вывода общего количества записей обо всех заблокированных маршрутах.
        Optional<Integer> totalRecords = getTotalRecords(response);
        if (totalRecords.isPresent()) {
            LOGGER.info(Constants.GREEN + "Общее количество записей обо всех заблокированных маршрутах составляет: " + Constants.RESET + Constants.BLUE + totalRecords.get() + Constants.RESET);
        } else {
            LOGGER.warning(Constants.RED + "total_records не найден!".toUpperCase() + Constants.RESET);
        }
        return response;
    }

    @Description("Метод обновляет тип и причину блокировки маршрута.")
    @Step("Обновляю тип и причину блокировки маршрута.")
    public void tryToUpdateRoute() throws Exception {
        // 1. Создаем и блокируем маршрут.
        blockingRoute();
        // 2. Получаем список заблокированных маршрутов.
        String response = getAllRouteBlocksList(1, 50);
        // 3. Парсим ID маршрута.
        List<Integer> ids = ExtractingFromJson.extractingAnyFieldFromJson(response);
        if (ids.isEmpty()) {
            // Если список пуст - критическая ошибка!
            LOGGER.severe(Constants.RED + "ОШИБКА: Не найдено ни одного заблокированного маршрута!" + Constants.RESET);
            throw new IllegalStateException("Нет заблокированных маршрутов для обновления");
        }
        // 4. Берем первый ID из списка.
        int firstId = ids.get(0);
        idToDelete = Integer.toString(firstId);
        // 5. Логируем найденный ID (для отладки).
        LOGGER.info(Constants.BLUE + "Найден ID маршрута для обновления: " + firstId + Constants.RESET);
        // 6. Обновляем маршрут.
        String json = tryToUpdateTypeAndReasonOfBlockingRoute();
        String serverResponse = ApiMethods.sendPutRequest(
                Swagger.UPDATE_TYPE_AND_REASON_OF_BLOCKING_ROUTE + idToDelete,
                json
        );
        // 7. Проверяем ответ сервера.
        LOGGER.info("Результат обновления: " + serverResponse);
    }

    @Description("Метод для парсинга общего количества заблокированных маршрутов: парсим значение поля 'total_records'. ")
    private static Optional<Integer> getTotalRecords(String jsonInput) {
        JSONObject json = new JSONObject(jsonInput);
        try {
            JSONObject data = json.getJSONObject("data");
            JSONObject pagination = data.getJSONObject("pagination");
            return Optional.of(pagination.getInt("total_records"));
        } catch (JSONException e) {
            return Optional.empty();
        }
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

    @Description("Вынос полей для формирования 'JSON' для блокировки существующего маршрута.")
    private String tryToBlockRoute() throws SQLException, InterruptedException {
        BlockingRoute blockingRoute = new BlockingRoute();
        blockingRoute.setEntity_lock("EDIT_LOCK");
        blockingRoute.setReason("0000-0000-0000");
        blockingRoute.setRoot_name(tryToMakeRoute(1, 30));

        Gson gson = new Gson();
        return gson.toJson(blockingRoute);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления типа и причины блокировки существующего маршрута.")
    private String tryToUpdateTypeAndReasonOfBlockingRoute() {
        UpdateTypeAndReason updateTypeAndReason = new UpdateTypeAndReason();
        updateTypeAndReason.setEntity_lock("DELETE_LOCK");
        updateTypeAndReason.setReason("0000-0000-0000");

        Gson gson = new Gson();
        return gson.toJson(updateTypeAndReason);
    }
}