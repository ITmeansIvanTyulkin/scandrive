package aggregation;

import aggregation.pojo.AddAggregationGtin;
import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AggregationGtinController {
    private static String gtin = "";
    private static final Logger LOGGER = Logger.getLogger(AggregationGtinController.class.getName());

    // Методы.
    @Description("Метод создаёт аггрегационный 'GTIN' в выносе полей, производит сериализацию и отправляет сгенерированный " +
            "код на ручку.")
    @Step("Пытаюсь добавить аггрегационный 'GTIN'.")
    public String addAggregationGtin() throws SQLException {
        String json = tryToAddAggregationGtin();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_AGGREGATION_GTIN, json);
        // Проверка по статус-коду осуществилась в методе выше, сейчас проверяю по сообщению.
        String expected = "Aggregation GTIN added";
        String actual = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actual)
                .as("Проверка сообщения об успешном добавлении GTIN")
                .isEqualTo(expected);
        // Проверка, что в базе данных появились тестовые данные.
        SqlMethods.isFilled(
                SQLCode.IS_EMPTY,
                "gtin",
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        return response;
    }

    @Description("Метод для получения списка всех существующих аггрегационых 'GTIN'.")
    @Step("Получаю список всех существующих аггрегационых 'GTIN'.")
    public static String getAggregationGtinList() throws SQLException {
        return ApiMethods.sendGetRequest(Swagger.GET_AGGREGATION_GTIN_LIST);
    }

    @Description("Метод для удаления всех существующих аггрегационых 'GTIN'.")
    @Step("Удаляю все существующие аггрегационые 'GTIN'.")
    public void deleteAggregationGtin() throws SQLException {
        String response = getAggregationGtinList();
        List<String> gtinList = ExtractingFromJson.extractingAnyFieldFromJsonToCollection(response, "data");
        for (String gtin : gtinList) {
            deleteGtin(gtin);
        }
        // Проверка, что база данных пуста после удаления тестовых данных.
        SqlMethods.isFilled(SQLCode.IS_EMPTY,
                "id",
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Вспомогательный метод для удаления 'GTIN'.")
    private static void deleteGtin(String gtin) {
        try {
            String json = tryToDeleteAggregationGtin();
            LOGGER.info(Constants.GREEN + "Отправка GTIN на удаление: " + Constants.RESET + Constants.BLUE + gtin + Constants.RESET);
            ApiMethods.sendPostRequest(Swagger.DELETE_ALL_GTINS, json);

        } catch (Exception e) {
            LOGGER.warning("Ошибка при удалении GTIN " + gtin + ": " + e.getMessage());
        }
    }

    @Description("Метод генерации кода 'GTIN' и вычленения из него 'GTIN' (01) и идентификатора (21), но без самого серийного номера.")
    private String gtinGenerator(int count) {
        List<String> generatedCodes = Gs1DataMatrixGenerator.generateMockGs1Codes(
                count, // количество кодов
                false, // includeExpiry
                false, // includeBatch
                false // includeCRC
        );
        return generatedCodes.stream()
                .findFirst()
                .map(code -> code.substring(code.indexOf("(01)") + 4, code.indexOf("(01)") + 4 + 14))
                .orElse("46000000000000");
    }

    @Description("Вынос полей для формирования 'JSON' для добавления аггрегационного 'GTIN'.")
    private String tryToAddAggregationGtin() {
        /*
        сгененировать код, например (01)40000257049687(21)SN0510ae09db(17)271206(10)B352,
        далее пропустить 0 и 14 символов после взять: 014000025704968721
         */
        gtin = gtinGenerator(1); // создаю код в количестве 1 и преобразую его, как в комменте выше

        AddAggregationGtin addAggregationGtin = new AddAggregationGtin();
        addAggregationGtin.setElementName(gtin);

        Gson gson = new Gson();
        return gson.toJson(addAggregationGtin);
    }

    @Description("Вынос полей для формирования 'JSON' для удаления аггрегационного 'GTIN'.")
    private static String tryToDeleteAggregationGtin() throws SQLException {
        String response = getAggregationGtinList();
        List<String> gtinList = ExtractingFromJson.extractingAnyFieldFromJsonToCollection(response, "data");
        for (String gtin : gtinList) {
            AddAggregationGtin addAggregationGtin = new AddAggregationGtin();
            addAggregationGtin.setElementName(gtin);

            Gson gson = new Gson();
            return gson.toJson(addAggregationGtin);
        }
        return response;
    }
}