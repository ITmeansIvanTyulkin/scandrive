package templatescontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;
import templatescontroller.pojo.AddPrinter;
import templatescontroller.pojo.UpdateTemplate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TemplatesController {
    static String idToCheck = "";
    private static final Logger LOGGER = Logger.getLogger(TemplatesController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Step("Преамбула: добавить шаблон.")
    public String addingTemplate() throws SQLException {
        String idToCheck = "";
        String json = tryToAddPrinter();
        String serverResponse = ApiMethods.sendPostRequest(Swagger.ADD_PRINTER_TEMPLATES_CONTROLLER, json);

        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "Template added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка, что принтер появился в базе данных.
        // Распарсиваю 'id' созданного принтера.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToCheck = String.valueOf(id);

            // Проверяю в базе данных на существование по распарсенному 'id'.
            SqlMethods.isExist(
                    SQLCode.TEMPLATE_EXISTS_IN_TEMPLATE_CONTROLLER,
                    idToCheck,
                    Credentials.DATABASE_DATAMATRIX_KEEPER,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
            );
        }
        return idToCheck;
    }

    @Description("Метод осуществляет запрос на ручку для получения списка имеющихся шаблонов.")
    @Step("Получаю список шаблонов.")
    public void getTemplatesList(int pageNumber, int pageSize) throws SQLException {
        // Преамбула: добавить шаблон.
        addingTemplate();

        // Основной сценарий.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String response = ApiMethods.sendGetRequest(Swagger.GET_TEMPLATES_LIST, paramString);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для получения шаблона по его 'id': сначала создаётся тестовый шаблон (преамбула) со всеми " +
            "проверками, в том числе, по базе данных, затем парсится его 'id' и по нему осуществляется поиск в " +
            "основном сценарии.")
    @Step("Получаю шаблон по его 'id'.")
    public void getTemplateByItsId() throws SQLException {
        // Преамбула: добавить шаблон.
        String idToCheck = addingTemplate();
        String response = ApiMethods.sendGetRequest(Swagger.GET_TEMPLATE_BY_ID, idToCheck);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для обновления шаблона: в приватном методе сериализации в 'name' создаётся новый тестовый " +
            "шаблон и в 'name' возвращается его имя, при этом 'id' шаблона выносится в глобальную переменную типа " +
            "int, так как метод уже возвращает String имени шаблона. Затем всё это после сериализации передаётся " +
            "на ручку и проверяется ответ сервера по коду и по сообщению. После чего осуществляется проверка по " +
            "базе данных на предмет того, что переданная при сериализации часть имени, содержащая 'UPDATED' " +
            "появилась в базе данных в таблице, что свидетельствует об успешном обновлении шаблона. ")
    @Step("Обновляю шаблон, созданный в преамбуле тест-кейса.")
    public void updateTemplate() throws SQLException {
        String json = tryToUpdateTemplate();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_TEMPLATE_BY_ID + idToCheck, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The template has been successfully updated";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка в базе данных, что в колонке 'template' появилось 'UPDATED'.
        SqlMethods.isValueUpdated(
                SQLCode.UPDATED_NAME_EXISTS_IN_TEMPLATE,
                "template",
                "template",
                idToCheck,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод осуществляет запрос на ручку для удаления шаблона по его 'id'.")
    @Step("Удаляю шаблон по его 'id'.")
    public void deleteTemplate() throws SQLException {
        // Преамбула: добавить шаблон.
        String idToCheck = addingTemplate();
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_TEMPLATE_TEMPLATES_CONTROLLER, idToCheck);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "Template deleted successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка, что шаблон удалён из базы данных.
        SqlMethods.isExist(
                SQLCode.TEMPLATE_EXISTS_IN_TEMPLATE_CONTROLLER,
                idToCheck,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

    @Description("Метод для удаления тестовых данных из базы данных - из всех связанных таблиц.")
    @Step("Удаляю тестовые данные из базы данных - из всех связанных таблиц: 'template', 'params', 'hooks', 'device'.")
    public void cleaningDatabase() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_TEMPLATE,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_PARAMS,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_HOOKS,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_DEVICE,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

// PRIVATE ZONE.
    @Step("Преамбула: создаю шаблон и получаю его имя.")
    private String getTemplateName() throws SQLException {
        // Преамбула: добавить шаблон.
        idToCheck = addingTemplate();
        // Получаю имя созданного шаблона.
        return SqlMethods.getValueFromDatabase(
                SQLCode.GET_DEVICE_NAME_FROM_TEMPLATE + idToCheck,
                "name",
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD,
                String.class
        );
    }

    @Description("Вынос полей для формирования 'JSON' для добавления шаблона.")
    @Step("Произвожу сериализацию для добавления шаблона.")
    private String tryToAddPrinter() {
        AddPrinter addPrinter = AddPrinter.builder()
                .name("TEST: " + RandomMethods.createRandomName())
                .printer_type("ADAPTER")
                .template(RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Добавление шаблона произошло успешно: " + Constants.RESET +
                Constants.BLUE + addPrinter.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addPrinter);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления шаблона.")
    @Step("Произвожу сериализацию для обновления шаблона.")
    private String tryToUpdateTemplate() throws SQLException {
        UpdateTemplate updateTemplate = UpdateTemplate.builder()
                .name(getTemplateName())
                .printer_type("ADAPTER")
                .template("UPDATED: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Обновление шаблона произошло успешно: " + Constants.RESET +
                Constants.BLUE + updateTemplate.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updateTemplate);
    }
}