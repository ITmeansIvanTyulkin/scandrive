package json.extractfromjson;

import io.qameta.allure.Description;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExtractingFromJson {
    @Description("Метод для извлечения 'fieldName' из 'JSON'.")
    public static Optional<Object> extractingAnyFieldFromJson(String jsonInput, String fieldName, Class<?> returnType) {
        // Создаем JSONObject из входной строки JSON.
        JSONObject jsonObject = new JSONObject(jsonInput);

        try {
            JSONObject dataObject = jsonObject.getJSONObject("data");
            // Проверяем, есть ли поле 'fieldName' в корневом объекте 'JSON'.
            if (dataObject.has(fieldName)) {
                if (returnType == String.class) {
                    return Optional.of(dataObject.getString(fieldName));
                } else if (returnType == Integer.class) {
                    var result = Optional.of(dataObject.getInt(fieldName));
                    return Optional.of(dataObject.getInt(fieldName));
                } else {
                    throw new IllegalArgumentException("Неподдерживаемый тип: " + returnType);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Description("Метод для извлечения 'fieldName' из верхнего уровня (корня) 'JSON'.")
    public static String extractingAnyFieldFromJson(String jsonInput, String fieldName) {
        JSONObject jsonObject = new JSONObject(jsonInput);
        String value = null;
        try {
            // Проверяем, есть ли поле 'fieldName' в 'JSON'.
            if (jsonObject.has(fieldName)) {
                value = jsonObject.getString(fieldName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    @Description("Метод для извлечения 'fieldName' из вложенного в 'JSON' объекта.")
    public static String extractingAnyFieldFromJson(String jsonInput, String nestedName, String fieldName) {
        JSONObject jsonObject = new JSONObject(jsonInput);
        String value = null;
        try {
            // Проверяем, есть ли объект 'nestedName' в 'JSON'.
            if (jsonObject.has(nestedName)) {
                JSONObject warehouseObject = jsonObject.getJSONObject(nestedName);
                // Проверяем, есть ли поле 'fieldName' в объекте 'nestedName'.
                if (warehouseObject.has(fieldName)) {
                    value = warehouseObject.getString(fieldName);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    @Description("Метод для извлечения коллекции 'fieldName' из 'JSON'.")
    public static List<String> extractingAnyFieldFromJsonToCollection(String jsonInput, String fieldName) {
        // Парсим входящий 'JSON' как 'JSONObject'.
        JSONObject jsonObject = new JSONObject(jsonInput);
        // Получаем массив 'data' из объекта.
        JSONArray jsonArray = jsonObject.getJSONArray(fieldName);
        // Создаем список из массива 'data'.
        List<String> values = IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getString)
                .collect(Collectors.toList());
        // Выводим все распарсенные значения.
        values.forEach(System.out::println);
        return values;
    }

    @Description("Метод для извлечения коллекции 'fieldName' из СЛОЖНОГО 'JSON'.")
    public static List<Integer> extractingAnyFieldFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        List<Integer> ids = new ArrayList<>();
        JsonNode listNode = rootNode.path("data").path("list");

        for (JsonNode itemNode : listNode) {
            int id = itemNode.path("id").asInt();
            ids.add(id);
        }
        return ids;
    }

    @Description("Метод для извлечения всех ID из routes_to_incoming_endpoint и routes_to_outgoing_endpoint")
    public static List<Integer> extractRouteIdsFromJson(String jsonInput) {
        JSONObject jsonObject = new JSONObject(jsonInput);
        List<Integer> routeIds = new ArrayList<>();

        try {
            JSONArray endpoints = jsonObject.getJSONObject("data").getJSONArray("list");

            for (int i = 0; i < endpoints.length(); i++) {
                JSONObject endpoint = endpoints.getJSONObject(i);

                // Обрабатываем routes_to_incoming_endpoint
                if (endpoint.has("routes_to_incoming_endpoint")) {
                    JSONArray incomingRoutes = endpoint.getJSONArray("routes_to_incoming_endpoint");
                    for (int j = 0; j < incomingRoutes.length(); j++) {
                        routeIds.add(incomingRoutes.getJSONObject(j).getInt("id"));
                    }
                }

                // Обрабатываем routes_to_outgoing_endpoint
                if (endpoint.has("routes_to_outgoing_endpoint")) {
                    JSONArray outgoingRoutes = endpoint.getJSONArray("routes_to_outgoing_endpoint");
                    for (int j = 0; j < outgoingRoutes.length(); j++) {
                        routeIds.add(outgoingRoutes.getJSONObject(j).getInt("id"));
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return routeIds;
    }
}