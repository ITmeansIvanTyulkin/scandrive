package devicecontroller;

import data.endpoints.apiendpoints.Swagger;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONObject;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DeviceController {
    private static final Logger LOGGER = Logger.getLogger(DeviceController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод, осуществляющий поиск списков параметров принтера.")
    @Step("Произвожу поиск списков параметра принтера.")
    public void searchForPrinterParameters(String name) throws SQLException {
        String queryParam = "printer_type=" + name;
        String response = ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS + "?" + queryParam);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод, осуществляющий поиск списков параметров для всех существующих типов принтеров.")
    @Step("Произвожу поиск списков параметра для всех существующих типов принтеров.")
    public List<String> searchForPrinterParametersParameterised() throws SQLException {
        List<String> printerTypes = getPrinterTypes();

        return printerTypes.stream()
                .map(printerType -> {
                    try {
                        return switch (printerType) {
                            case "HITACHI_IP" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=HITACHI_IP");
                            case "GODEX" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=GODEX");
                            case "CORRECT_PACK" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=CORRECT_PACK");
                            case "TSC" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=TSC");
                            case "SOLMARK_V2" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=SOLMARK_V2");
                            case "FC32LC" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=FC32LC");
                            case "POKKELS" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=POKKELS");
                            case "DIKAI" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=DIKAI");
                            case "SAVEMA" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=SAVEMA");
                            case "COLOS" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=COLOS");
                            case "EVOLABEL" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=EVOLABEL");
                            case "CYCJET" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=CYCJET");
                            case "ADAPTER" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=ADAPTER");
                            case "HSA" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=HSA");
                            case "VIDEOJET" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=VIDEOJET");
                            case "RYNAN" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=RYNAN");
                            case "SOLMARK" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=SOLMARK");
                            case "KEEPER" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=KEEPER");
                            case "FC53" ->
                                    ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINTER_PARAMETERS, "?printer_type=FC53");
                            default -> throw new IllegalArgumentException("Неизвестный тип принтера: " + printerType);
                        };
                    } catch (Exception e) {
                        return "Ошибка: " + printerType + ": " + e.getMessage();
                    }
                })
                .collect(Collectors.toList());
    }

    @Description("Метод для поиска списка стутусов принтера.")
    @Step("Осуществляю поиск списка статусов принтера.")
    public void searchAndCheckPrinterStatus() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.SEARCH_AND_CHECK_PRINTER_STATUS);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод осуществляет поиск списка всех типов принтеров.")
    @Step("Осуществляю поиск всех типов принтеров.")
    public List<String> searchForAllPrintersTypes() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.SEARCH_ALL_PRINTERS_TYPES_LIST);

        // Извлечение массива принтеров из JSON.
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray printersArray = jsonResponse.getJSONArray("data");

        // Конвертация JSONArray в List<String>.
        List<String> printersList = new ArrayList<>();
        for (int i = 0; i < printersArray.length(); i++) {
            printersList.add(printersArray.getString(i));
        }

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return printersList;
    }

    @Description("Метод для получения информации о текущем времени сервера.")
    @Step("Получаю информацию о текущем времени сервера.")
    public void getInfoOfCurrentServerTime() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.GET_INFO_OF_CURRENT_SERVER_TIME);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

// PRIVATE ZONE.
    @Description("Осуществляю поиск всех типов принтеров.")
    private List<String> getPrinterTypes() throws SQLException {
        return searchForAllPrintersTypes();
    }
}