package endpointcontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import data.waitings.Waitings;
import date.GetDate;
import endpointcontroller.pojo.AddingNewPoint;
import endpointcontroller.pojo.Parameters;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertjson.AssertJson;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONObject;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EndpointController {
    private static final Logger LOGGER = Logger.getLogger(EndpointController.class.getName());

    // Методы.
    @Description("Метод для осуществления контекстного поиска с пагинацией.")
    @Step("Осуществляю контекстный поиск.")
    public void contextSearch(int pageNumber, int pageSize) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        ApiMethods.sendGetRequest(Swagger.CONTEXT_SEARCH, paramString);
    }

    @Description("Метод для проверки, что возможно получить все типы данных соединений: 'TCP', 'RABBIT_MQ', 'FOLDER', 'DATAMATRIX_KEEPER', 'CONVEYOR'.")
    @Step("Получаю все типы данных соединений.")
    public void getTypes() throws SQLException {
        getAllTypesOfConnection();
    }

    @Description("Метод для проверки работоспособности параметров соединений: 'TCP', 'RABBIT_MQ', 'FOLDER', 'DATAMATRIX_KEEPER', 'CONVEYOR'.")
    @Step("Проверяю каждый параметр на работоспособность параметризированно (другой тестовый класс).")
    public String getParameters(String configuration, String endPoint) throws SQLException {
        return ApiMethods.sendGetRequest(endPoint, configuration);
    }

    @Description("Получение всех типов данных соединений в коллекцию.")
    public static List<String> getAllTypesOfConnection() throws SQLException {
        String json = ApiMethods.sendGetRequest(Swagger.GET_ALL_DATA_TYPES);
        return ExtractingFromJson.extractingAnyFieldFromJsonToCollection(json, "data");
    }

    @Description("Метод добавляет новую тестовую точку, проверяет успех создания по статус-коду 200 и затем удаляет её.")
    @Step("Добавляю новую тестовую точку, осуществляю проверку успешности, затем удаляю.")
    public void addNewPoint() throws SQLException {
        String json = tryToAddNewPoint();
        String idToDelete = deletePointById(json);
        ApiMethods.sendDeleteRequest(Swagger.DELETE_POINT, idToDelete);
    }

    @Description("Метод для получения информации о точке по её 'id'.")
    @Step("Получаю информацию о точке по её 'id'")
    public void checkPointById() throws SQLException {
        String json = tryToAddNewPoint();
        String id = tryToCheckPointById(json);
        ApiMethods.sendGetRequest(Swagger.GET_INFO_BY_ID, id);
    }

    @Description("Метод для обновления информации о созданной ранее точке.")
    @Step("Обновляю информацию о точке.")
    public void updatePointInfo() throws SQLException {
        String json = tryToAddNewPoint();
        String id = tryToCheckPointById(json);
        Waitings.awaitSeconds(Constants.EXPECTATION_5);
        String updatedJson = tryToUpdatePoint(json);
        ApiMethods.sendPutRequest(Swagger.UPDATE_POINT_INFO + id + "?is_forced=true", updatedJson);
        // Проверка на обновление: обновлённая точка не должна совпадать по полю со старой - то есть false.
        AssertJson.assertJson(json, updatedJson, "name", false);
    }

    @Description("Метод для обновления информации по любой точке.")
    private String getPointDataAndUpdate(String json) throws SQLException {
        String updated = GetDate.getTimeAndFormat() + " UPDATED";
        String name = (String) ExtractingFromJson.extractingAnyFieldFromJson(json, "name", String.class).get();
        assert name != null;
        name = updated;
        return updated;
    }

    @Description("Получаю информацию по 'id' точки.")
    private String tryToCheckPointById(String json) {
        return getAnyField(json);
    }

    @Description("Удаление точки по её 'id'.")
    private String deletePointById(String json) {
        return getAnyField(json);
    }

    @Description("Парсинг для получения 'id' точки.")
    private String getAnyField(String json) {
        Integer id = (Integer) ExtractingFromJson.extractingAnyFieldFromJson(json, "id", Integer.class).get();
        assert id != null;
        return Integer.toString(id);
    }

    @Description("Метод рандомно выбирает тип новой точки и добавляет её.")
    private String tryToAddNewPoint() throws SQLException {
        String response = "";
        List<String> randomType = getAllTypesOfConnection();
        String type = RandomMethods.getRandom(randomType);

        response = switch (type) {
            case "TCP" -> {
                String json = tryToCreateNewPointWithTcp();
                yield ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json);
            }
            case "RABBIT_MQ" -> {
                String json1 = tryToCreateNewPointWithRabbit();
                yield ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json1);
            }
            case "FOLDER" -> {
                String json2 = tryToCreateNewPointWithFolder();
                yield ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json2);
            }
            case "DATAMATRIX_KEEPER" -> {
                String json3 = tryToCreateNewPointWithDataMatrixKeeper();
                yield ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json3);
            }
            case "CONVEYOR" -> {
                String json4 = tryToCreateNewPointWithConveyor();
                yield ApiMethods.sendPostRequest(Swagger.ADD_NEW_POINT, json4);
            }
            default -> response;
        };
        return response;
    }

    @Description("Этот метод выполняет контекстный поиск всех созданных (существующих) точек, затем извлекает " +
            "в коллекцию все найденные значения поля 'QUEUE'.")
    private static List<String> contextSearchToParse(int pageNumber, int pageSize) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        String response = ApiMethods.sendGetRequest(Swagger.CONTEXT_SEARCH, paramString);
        List<String> queue = extractingQueueValuesFromJson(response);
        return queue;
    }

    @Description("Извлечение в коллекцию всех значений очередей 'QUEUE'. Если 'QUEUE' не имеет значения (то есть " +
            "если оно пустое), то ему присваивается значение '1' и оно будет соответствовать номеру очереди.")
    private static List<String> extractingQueueValuesFromJson(String jsonInput) {
        // Парсим входящий 'JSON' как 'JSONObject'.
        JSONObject jsonObject = new JSONObject(jsonInput);
        // Получаем объект 'data'.
        JSONObject dataObject = jsonObject.getJSONObject("data");
        // Получаем массив 'list' из объекта 'data'.
        JSONArray listArray = dataObject.getJSONArray("list");

        // Создаем список для хранения значений поля 'QUEUE'.
        List<String> queueValues = new ArrayList<>();

        // Проходим по каждому элементу массива 'list'.
        for (int i = 0; i < listArray.length(); i++) {
            // Получаем текущий элемент массива.
            JSONObject currentElement = listArray.getJSONObject(i);
            // Извлекаем объект 'params' из текущего элемента.
            JSONObject paramsObject = currentElement.optJSONObject("params");

            // Извлекаем значение поля 'QUEUE' из объекта 'params'.
            String queueValue = paramsObject.getString("QUEUE");

            // Проверяем, пустое ли значение поля 'QUEUE'.
            if (queueValue == null || queueValue.isEmpty()) {
                // Если значение пустое, добавляем '1' в список.
                queueValues.add("1");
            } else {
                // Иначе добавляем само значение поля 'QUEUE'.
                queueValues.add(queueValue);
            }
        }
        return queueValues;
    }

    @Description("Метод для нахождения макисмального целочисленного значения в коллекции, прибавление к нему '1' для того, чтобы избежать дублирования " +
            "и кастомизация значения в строку.")
    private static String findMaxInteger(List<String> strings) {
        // Преобразуем каждую строку в целое число и находим максимум.
        int maxValue = strings.stream()
                .mapToInt(Integer::parseInt)
                .max()
                .getAsInt() + 1; // Прибавляем 1 к найденному максимальному значению.

        // Преобразуем итоговое значение в строку и возвращаем.
        return Integer.toString(maxValue);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления новой тестовой точки если тип 'RABBIT_MQ'.")
    private String tryToCreateNewPointWithRabbit() throws SQLException {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("Тест " + GetDate.getTimeAndFormat());
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        parameters.setLOGIN("quest");
        parameters.setPASSWORD("quest");
        parameters.setQUEUE(findMaxInteger(contextSearchToParse(1, 10)));
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType("RABBIT_MQ");
        addingNewPoint.setUrl("localhost:5672");

        Gson gson = new Gson();
        String json = gson.toJson(addingNewPoint);
        return json;
    }

    @Description("Вынос полей для формирования 'JSON' для добавления новой тестовой точки если тип 'TCP'.")
    private String tryToCreateNewPointWithTcp() throws SQLException {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("Тест " + GetDate.getTimeAndFormat());
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType("TCP");
        addingNewPoint.setUrl("0.0.0.0:9100");

        Gson gson = new Gson();
        String json = gson.toJson(addingNewPoint);
        return json;
    }

    @Description("Вынос полей для формирования 'JSON' для добавления новой тестовой точки если тип 'FOLDER'.")
    private String tryToCreateNewPointWithFolder() throws SQLException {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("Тест " + GetDate.getTimeAndFormat());
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType("FOLDER");
        addingNewPoint.setUrl("localhost:9101");

        Gson gson = new Gson();
        String json = gson.toJson(addingNewPoint);
        return json;
    }

    @Description("Вынос полей для формирования 'JSON' для добавления новой тестовой точки если тип 'DATAMATRIX_KEEPER'.")
    private String tryToCreateNewPointWithDataMatrixKeeper() throws SQLException {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("Тест " + GetDate.getTimeAndFormat());
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType("DATAMATRIX_KEEPER");
        addingNewPoint.setUrl("localhost:9102");

        Gson gson = new Gson();
        String json = gson.toJson(addingNewPoint);
        return json;
    }

    @Description("Вынос полей для формирования 'JSON' для добавления новой тестовой точки если тип 'CONVEYOR'.")
    private String tryToCreateNewPointWithConveyor() throws SQLException {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName("Тест " + GetDate.getTimeAndFormat());
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType("CONVEYOR");
        addingNewPoint.setUrl("localhost:9103");

        Gson gson = new Gson();
        String json = gson.toJson(addingNewPoint);
        return json;
    }

    @Description("Вынос полей для формирования 'JSON' для обновления тестовой точки.")
    private String tryToUpdatePoint(String json) throws SQLException {
        AddingNewPoint addingNewPoint = new AddingNewPoint();
        addingNewPoint.setName(getPointDataAndUpdate(json));
        // Вложение данных на передачу.
        Parameters parameters = new Parameters();
        // Установка параметров в объект AddingNewPoint.
        addingNewPoint.setParams(parameters);
        addingNewPoint.setType("CONVEYOR");
        addingNewPoint.setUrl("localhost:9103");

        Gson gson = new Gson();
        json = gson.toJson(addingNewPoint);
        return json;
    }
}