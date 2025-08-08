package aggregation;

import aggregation.pojo.BufferRules;
import aggregation.pojo.CreateNewAggregationLine;
import aggregation.pojo.FiltrationRules;
import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import data.randommethods.RandomMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Fail.fail;

public class Aggregation {
    private static final Logger LOGGER = Logger.getLogger(Aggregation.class.getName());
    public static int id;

    // Методы.
    @Description("Метод добавляет новую тестовую линию агрегации, учитывая правила фильтрации, правила агрегации, проверяет успех создания по статус-коду 200.")
    @Step("Добавляю новую тестовую линию агрегации, осуществляю проверку успешности.")
    public String addNewAggregationLine() throws SQLException {
        String expected = "The aggregation line has been added";
        String json = tryToCreateNewTestAggregationLine();
        // Автоматическая проверка по статус-коду 200.
        String response = ApiMethods.sendPostRequest(Swagger.ADD_NEW_TEST_AGGREGATION_LINE, json);
        // Прямые проверки.
        // Вторая проверка: по сообщению в переменной 'error'.
        String message = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        id = (int) (Integer) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class).get();
        String idToCheckInDB = Integer.toString(id);
        assert message != null;
        if (message.equals(expected)) {
            LOGGER.info(Constants.GREEN + "Ожидаемое сообщение соответствует эталонному - линия агрегации успешно создана: " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
        }
        else {
            fail("Тест упал");
        }
        // Третья проверка по базе данных: проверяю, что переданный 'id' в базе данных существует/несуществует.
        SqlMethods.isExist(
                SQLCode.IF_ID_EXISTS,
                idToCheckInDB,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        return response;
    }

    @Description("Метод удаления тестовых данных из всех связанных таблиц в базе данных.")
    public void cleanTestDataInDatabase() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_AGGREGATION_LINE_BUFFERED_CODES,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Очистка таблицы 'aggregation_line_buffer_rule'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_AGGREGATION_LINE_BUFFER_RULE,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Очистка таблицы 'buffered_codes'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_BUFFERED_CODES,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Очистка таблицы 'gs1_128_rules_to_aggregation_lines'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_GS1_128_RULES_TO_AGGREGATION_LINES,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Очистка таблицы 'monitors_to_lines'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_MONITORS_TO_LINES,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Очистка таблицы 'filtration_rule'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_FILTRATION_RULE,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
        // Очистка таблицы 'aggregation_line'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_AGGREGATION_LINE,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
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

    @Description("Вынос полей для формирования 'JSON' для создания тестовой линии агрегации.")
    private String tryToCreateNewTestAggregationLine() {
        CreateNewAggregationLine createNewAggregationLine = new CreateNewAggregationLine();
        createNewAggregationLine.setAggregation_level(1); // тесты проводится на 1
        createNewAggregationLine.setAggregation_type("SSCC"); // оставить SSCC
        createNewAggregationLine.setBatch_dm("batch"); // оставить, как есть
        /*
        сгененировать код, например (01)40000257049687(21)SN0510ae09db(17)271206(10)B352,
        далее пропустить 0 и 14 символов после взять: 014000025704968721
         */
        String gtin = gtinGenerator(1); // создаю код в количестве 1 и преобразую его, как в комменте выше
        // Создаём список bufferRules (так как у нас массив).
        List<BufferRules> bufferRulesList = new ArrayList<>();
        BufferRules bufferRules = new BufferRules();
        bufferRules.setGtin(gtin);
        bufferRules.setSize(4);
        bufferRules.setTimer(0);
        bufferRulesList.add(bufferRules);
        createNewAggregationLine.setBuffer_rules(bufferRulesList);

        // Создаём список filtrationRules (так как у нас массив).
        List<FiltrationRules> filtrationRulesList = new ArrayList<>();
        FiltrationRules filtrationRules = new FiltrationRules();
        filtrationRules.setAggregation_size(0);
        filtrationRules.setGtin(gtin);
        filtrationRules.setNomenclature("string");
        filtrationRules.setWithout_aggregation(true);
        filtrationRulesList.add(filtrationRules);
        createNewAggregationLine.setFiltration_rules(filtrationRulesList);

        createNewAggregationLine.setName("TEST: " + RandomMethods.createRandomName()); // любое

        Gson gson = new Gson();
        return gson.toJson(createNewAggregationLine);
    }
}