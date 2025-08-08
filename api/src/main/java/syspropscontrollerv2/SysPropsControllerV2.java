package syspropscontrollerv2;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.INNGenerator;
import data.randommethods.RandomMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;
import syspropscontrollerv2.pojo.CreateSysProps;
import syspropscontrollerv2.pojo.UpdateSysProps;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SysPropsControllerV2 {
    private static final Logger LOGGER = Logger.getLogger(SysPropsControllerV2.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для получения списка системных реквизитов.")
    @Step("Получаю системные реквизиты.")
    public String getSystemPropertiesList(int pageNumber, int pageSize) throws SQLException {
        // Осуществляю контекстный поиск.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String response =  ApiMethods.sendGetRequest(Swagger.GET_SYS_PROPS_LIST, paramString);
        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return response;
    }

    @Description("Метод для создания системного реквизита.")
    @Step("Создаю системный реквизит.")
    public int createSysProps() throws SQLException {
        String json = tryToCreateSysProps();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_SYS_PROPERTY, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "System prop was successfully created";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // Получение ID для проверки в БД и возврата в другой метод (если понадобится).
        int id = (int) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class)
                .orElseThrow(() ->
                        new RuntimeException("Не удалось извлечь ID из ответа"));

        // 2. Проверка существования системного реквизита в БД по ID.
        SqlMethods.isExist(
                SQLCode.SYS_PROP_EXISTS,
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD);

        return id;
    }

    @Description("Метод для обновления системного реквизита.")
    @Step("Обновляю системный реквизит.")
    public void updateSysProps() throws SQLException {
        // Преамбула. Создаю системный реквизит.
        int id = createSysProps();
        // Основной сценарий. Обновляю системный реквизит.
        String json = tryToUpdateSysProps();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_SYS_PROPERTY + id, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "System prop was successfully updated";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка существования системного реквизита в БД по ID.
        SqlMethods.isExist(
                SQLCode.SYS_PROP_EXISTS,
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD);

        // 3. Проверка, что итоговое значение колонки 'prop_value' таблицы содержит 'UPDATED' - то есть было обновлено.
        SqlMethods.isValueUpdated(
                SQLCode.UPDATED_EXISTS,
                "sys_prop",
                "prop_value",
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
                );
    }

    @Description("Метод для удаления системного реквизита.")
    @Step("Удаляю системный реквизит по его 'id'.")
    public void deleteSysProps() throws SQLException {
        // Преамбула. Создаю системный реквизит.
        int id = createSysProps();
        // Основной сценарий. Удаляю системный реквизит.
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_SYS_PROPERTY, String.valueOf(id));

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "System prop was successfully deleted";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка существования системного реквизита в БД по ID (ждём 'false' - например, ID: 21 существует в базе данных: false).
        SqlMethods.isExist(
                SQLCode.SYS_PROP_EXISTS,
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD);
    }

    @Description("Метод для получения списка доступных параметров.")
    @Step("Получаю список доступных параметров.")
    public String getParametersList() throws SQLException {
        String response =  ApiMethods.sendGetRequest(Swagger.GET_PARAMETERS_LIST);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return response;
    }

    @Description("Метод для удаления тестовых данных из базы данных.")
    @Step("Удаляю все тестовые данные из базы данных.")
    public void deleteTestData() throws SQLException {
        // Теперь удаляю данные из таблицы 'print_template'.
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_SYS_PROP,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

// PRIVATE ZONE.
    @Description("Вынос полей для формирования 'JSON' для создания системного реквизита.")
    @Step("Произвожу сериализацию для создания системного реквизита.")
    private String tryToCreateSysProps() {
        CreateSysProps createSysProps = CreateSysProps.builder()
                .inn(INNGenerator.generateInn(false)) // юридическое лицо.
                .key("GS1_PREFIX")
                .level(1)
                .sequence(1)
                .value("TEST " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Системный реквизит успешно создан: " + Constants.RESET +
                Constants.BLUE + createSysProps.getValue() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(createSysProps);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления системного реквизита.")
    @Step("Произвожу сериализацию для обновления системного реквизита.")
    private String tryToUpdateSysProps() {
        UpdateSysProps updateSysProps = UpdateSysProps.builder()
                .inn(INNGenerator.generateInn(true)) // физическое лицо.
                .key("SSCC_EXTENSION")
                .level(1)
                .sequence(1)
                .value("UPDATED TEST " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Системный реквизит успешно обновлён: " + Constants.RESET +
                Constants.BLUE + updateSysProps.getValue() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updateSysProps);
    }
}