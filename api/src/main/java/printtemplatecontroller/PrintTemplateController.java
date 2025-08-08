package printtemplatecontroller;

import aggregation.Aggregation;
import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import date.GetDate;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.assertjson.AssertJson;
import json.extractfromjson.ExtractingFromJson;
import printtemplatecontroller.pojo.AddOrUpdatePrintTemplateEntities;
import printtemplatecontroller.pojo.AddPrintTemplate;
import printtemplatecontroller.pojo.UpdatePrintTemplate;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrintTemplateController extends Aggregation {
    Aggregation aggregation = new Aggregation();
    private static final Logger LOGGER = Logger.getLogger(PrintTemplateController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для добавления шаблона печати.")
    @Step("Добавляю шаблон печати и проверяю его существование в БД.")
    public int addPrintTemplate() throws SQLException {
        String json = tryToAddPrintTemplate();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_PRINT_TEMPLATE, json);

        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The print template has been successfully added";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Получение ID для проверки в БД и возврата в другой метод (если понадобится).
        int id = (int) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class)
                .orElseThrow(() ->
                        new RuntimeException("Не удалось извлечь ID из ответа"));

        // 3. Проверка существования шаблона печати в БД по ID.
        SqlMethods.isExist(
                SQLCode.PRINT_TEMPLATE_EXISTS,
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD);

        return id;
    }

    @Description("Метод для осуществления контекстного поиска всех существующих шаблонов печати.")
    @Step("Осуществляю контекстный поиск существующих шаблонов печати.")
    public void searchForPrintTemplates(int pageNumber, int pageSize,
                                        boolean enabled_for_conveyor,
                                        boolean enabled_for_workstation,
                                        boolean error_enabled_for_conveyor) throws SQLException {
        // Добавляю шаблон печати.
        addPrintTemplate();
        // Осуществляю контекстный поиск существующих шаблонов печати.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);
        params.put("enabled_for_conveyor", enabled_for_conveyor);
        params.put("enabled_for_workstation", enabled_for_workstation);
        params.put("error_enabled_for_conveyor", error_enabled_for_conveyor);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_PRINT_TEMPLATES, paramString);
    }

    @Description("Получение информации о шаблоне печати по его 'id'. " +
            "При создании шаблона о печати осуществляется проверка на существование в БД.")
    @Step("Создаю тестовый шаблон печати, распарсиваю его 'id' и отправляю данные на целевую ручку для получения " +
            "информации о шаблоне печати по его 'id'.")
    public String getPrintTemplateInfoById() throws SQLException {
        int id = addPrintTemplate();
        String response = ApiMethods.sendGetRequest(Swagger.GET_PRINT_TEMPLATE_INFO_BY_ID + id);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
        return response;
    }

    @Description("Метод для обновления информации шаблона печати.")
    @Step("Обновляю информацию шаблона печати.")
    public void updatePrintTemplateInfoById() throws SQLException {
        int id = addPrintTemplate();                            // добавил новый шаблон
        String response1 = getPrintTemplateInfoById();          // получил то, что добавил

        String json = tryToUpdatePrintTemplate();               // сериализую для обновления
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_PRINT_TEMPLATE_INFO_BY_ID + id, json); // обновил
        String response2 = getPrintTemplateInfoById();          // получил обновлённое

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The print template has been successfully updated";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
        // 2. Сравниваю 2 JSON - который был изначально и новый по полю 'name'.
        AssertJson.assertJson(response1, response2, "data", "name", false);
    }

    @Description("Метод удаляет созданный шаблон печати по его 'id'.")
    @Step("Удаляю шаблон печати по его 'id'.")
    public void deletePrintTemplateById() throws SQLException {
        int id = addPrintTemplate();
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_PRINT_TEMPLATE_INFO_BY_ID, String.valueOf(id));
        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The print template has been deleted";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для получения карты замены значений для печати на основании описаний.")
    @Step("Получаю карты замены значений для печати на основании описаний.")
    public void getValuesMapForPrinting() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.GET_VALUES_MAP_FOR_PRINTING);
        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для добавления или обновления шаблона, даты производства и партии у выбранных сущностей.")
    @Step("Произвожу шаги для добавления или обновления шаблона, даты производства и партии у выбранных сущностей")
    public void addOrUpdatePrintTemplateEntities() throws SQLException {
        // Предварительные шаги кейса.
        // 1. Создаю линию агрегации.
        createAggregationLine();
        // 2. Добавляю шаблон печати.
        createPrintTemplate();
        // Основной сценарий. Формирую 'JSON' для добавления/обновления шаблона, даты производства и партии у выбранных сущностей.
        String json = tryToAddOrUpdatePrintTemplateEntities();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_OR_UPDATE_PRINT_TEMPLATE_ENTITIES, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The operation is completed";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления тестовых данных из базы данных.")
    @Step("Удаляю все тестовые данные из базы данных.")
    public void deleteTestData() throws SQLException {
        // Удаление тестовых данных из всех связанных таблиц в базе данных. Без этого нельзя очистить таблицу 'print_template'.
        aggregation.cleanTestDataInDatabase();
        // Теперь удаляю данные из таблицы 'print_template'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_PRINT_TEMPLATE,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

// PRIVATE ZONE.
    @Step("Выполняю предварительное условие: создаю линию агрегации.")
    private String createAggregationLine() throws SQLException {
        String response =  aggregation.addNewAggregationLine();
        return ExtractingFromJson.extractingAnyFieldFromJson(response, "data", "name");
    }

    @Step("Выполняю предварительное условие: добавляю шаблон печати.")
    private String createPrintTemplate() {
        String json = tryToAddPrintTemplate();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_PRINT_TEMPLATE, json);
        return ExtractingFromJson.extractingAnyFieldFromJson(response, "data", "name");
    }

    @Description("Вынос полей для формирования 'JSON' для добавления шаблона печати.")
    @Step("Произвожу сериализацию для добавления шаблона печати.")
    private String tryToAddPrintTemplate() {
        AddPrintTemplate addPrintTemplate = AddPrintTemplate.builder()
                .enabled_for_conveyor(true)
                .enabled_for_workstation(true)
                .encoding("UTF-8")
                .error_enabled_for_conveyor(true)
                .name("TEST: " + RandomMethods.createRandomName())
                .template("TEST: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Шаблон печати успешно создан: " + Constants.RESET +
                Constants.BLUE + addPrintTemplate.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addPrintTemplate);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления шаблона печати.")
    @Step("Произвожу сериализацию для обновления шаблона печати.")
    private String tryToUpdatePrintTemplate() {
        UpdatePrintTemplate updatePrintTemplate = UpdatePrintTemplate.builder()
                .enabled_for_conveyor(true)
                .enabled_for_workstation(true)
                .encoding("UTF-8")
                .error_enabled_for_conveyor(true)
                .name("TEST: " + RandomMethods.createRandomName())
                .template("TEST: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Шаблон печати успешно создан: " + Constants.RESET +
                Constants.BLUE + updatePrintTemplate.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updatePrintTemplate);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления/обновления шаблона, даты производства и партии у " +
            "выбранных сущностей.")
    @Step("Произвожу сериализацию для добавления/обновления шаблона, даты производства и партии у выбранных сущностей.")
    private String tryToAddOrUpdatePrintTemplateEntities() throws SQLException {
        AddOrUpdatePrintTemplateEntities addOrUpdatePrintTemplateEntities = AddOrUpdatePrintTemplateEntities.builder()
                .aggregation_line_external_id(createAggregationLine())
                .batch("123456")
                .custom_text("Something")
                .expiration_date(GetDate.getTimeAndFormat(true))
                .is_change_error_for_conveyor(true)
                .is_change_for_conveyor(true)
                .is_change_for_workstation(true)
                .name(createPrintTemplate())
                .production_date(GetDate.getTimeAndFormat(true))
                .build();

        LOGGER.info(Constants.GREEN + "Шаблон печати успешно создан: " + Constants.RESET +
                Constants.BLUE + addOrUpdatePrintTemplateEntities.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addOrUpdatePrintTemplateEntities);
    }
}