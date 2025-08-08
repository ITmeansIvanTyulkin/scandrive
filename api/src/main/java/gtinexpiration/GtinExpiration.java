package gtinexpiration;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import gtinexpiration.pojo.AddOrUpdate;
import gtinexpiration.pojo.DeleteGtinExpirationData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONArray;
import org.json.JSONObject;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GtinExpiration {
    private static String gtin;
    private static final Logger LOGGER = Logger.getLogger(GtinExpiration.class.getName());

    // Методы.
    @Description("Метод добавляет или изменяет даты срока годности 'gtin'.")
    @Step("Добавляю или изменяю даты срока годности 'gtin'.")
    public void editOrUpdateExpirationGtin() throws SQLException {
        String json = tryToAddOrUpdate();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_OR_UPDATE_EXPIRATION_GTIN, json);
        // Проверки.
        // Проверка по статус-коду осуществилась в методе выше, сейчас проверяю по сообщению.
        String expected = "The operation is completed";
        String actual = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        LOGGER.info(Constants.GREEN + "Дата срока годности 'gtin' была успешно дабавлена/изменена." + Constants.RESET);
        assertThat(actual)
                .as("Проверка сообщения об успешном добавлении 'GTIN'")
                .isEqualTo(expected);
        // Проверка, что в базе данных появились тестовые данные.
        SqlMethods.isFilled(SQLCode.IS_EXISTED,
                "gtin",
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод для получения всех данных о датах срока годности.")
    @Step("Получаю все существующие на данный момент данные о датах срока годности.")
    public static String getAllGtinExpirationDateInfo() throws SQLException {
        return ApiMethods.sendGetRequest(Swagger.GET_ALL_EXPIRATION_GTIN_DATA);
    }

    @Description("Метод для получения списка дат сроков годности 'GTIN'.")
    @Step("Получаю список дат сроков годности 'GTIN'.")
    public void getAllGtinDataList() throws SQLException {
        String response = getAllGtinExpirationDateInfo();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray objectsArray = jsonObject.getJSONArray("objects");

        List<String> gtins = new ArrayList<>();
        for (int i = 0; i < objectsArray.length(); i++) {
            JSONObject obj = objectsArray.getJSONObject(i);
            gtins.add(obj.getString("gtin"));
        }

        for (String gtin : gtins) {
            Map<String, Object> params = new HashMap<>();
            params.put("gtin", gtin);

            // Конвертируем map в строку формата queryString.
            String paramString = params.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));

            String serverResponse = ApiMethods.sendGetRequest(Swagger.GET_EXPIRATION_GTIN_LIST, paramString);
            // Проверки.
            // Проверка по статус-коду осуществилась в методе выше, сейчас проверяю по сообщению.
            String expected = "Successfully";
            String actual = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "error");
            // Извлекаю количество дней.
            JSONObject jsonObject2 = new JSONObject(serverResponse);
            JSONObject dataObject = jsonObject2.getJSONObject("data");
            int days = dataObject.getInt("days");
            LOGGER.info(Constants.GREEN + "Дата срока годности 'gtin' была успешно получена: " + Constants.RESET + Constants.BLUE + days + Constants.RESET);
            assertThat(actual)
                    .as("Проверка сообщения о получении списка дат сроков годоности 'GTIN'")
                    .isEqualTo(expected);
        }
    }

    @Description("Метод для удаления тестовых данных из базы данных.")
    @Step("Удаляю тестовые данные из базы данных.")
    public static void deleteTestData() throws SQLException {
        String response = getAllGtinExpirationDateInfo();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray objectsArray = jsonObject.getJSONArray("objects");

        List<String> gtins = new ArrayList<>();
        for (int i = 0; i < objectsArray.length(); i++) {
            JSONObject obj = objectsArray.getJSONObject(i);
            gtins.add(obj.getString("gtin"));
        }

        for (String gtin : gtins) {
            deleteGtin(gtin);
        }

        // Проверка, что база данных пуста после удаления тестовых данных.
        SqlMethods.isFilled(SQLCode.IS_EXISTED,
                "id",
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Вспомогательный метод для удаления 'GTIN'.")
    private static void deleteGtin(String gtin) {
        try {
            String json = tryToDeleteGtinExpirationData(gtin);
            LOGGER.info(Constants.GREEN + "Отправка 'GTIN' на удаление: " + Constants.RESET + Constants.BLUE + gtin + Constants.RESET);
            ApiMethods.sendPostRequest(Swagger.DELETE_GTIN, json);
        } catch (Exception e) {
            LOGGER.warning(Constants.RED + "Ошибка при удалении 'GTIN' " + Constants.RESET + Constants.BLUE + gtin + Constants.RESET + ": " + e.getMessage());
        }
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

    @Description("Вынос полей для формирования 'JSON' для изменения или обновления тестовой даты срока годности.")
    private String tryToAddOrUpdate() {
        gtin = gtinGenerator(1);

        AddOrUpdate addOrUpdate = new AddOrUpdate();
        addOrUpdate.setDays(20);
        addOrUpdate.setGtin(gtin);

        Gson gson = new Gson();
        return gson.toJson(addOrUpdate);
    }

    @Description("Вынос полей для формирования 'JSON' для удаления даты срока годности 'GTIN'.")
    private static String tryToDeleteGtinExpirationData(String gtin) {
        DeleteGtinExpirationData deleteGtinExpirationData = new DeleteGtinExpirationData();
        deleteGtinExpirationData.setElementName(gtin);

        Gson gson = new Gson();
        return gson.toJson(deleteGtinExpirationData);
    }
}