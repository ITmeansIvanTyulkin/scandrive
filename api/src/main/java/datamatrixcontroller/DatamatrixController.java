package datamatrixcontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import data.randommethods.RandomMethods;
import datamatrixcontroller.pojo.*;
import date.GetDate;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DatamatrixController {
    private static final Logger LOGGER = Logger.getLogger(DatamatrixController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для получения всех имеющихся сроков годности.")
    @Step("Получаю все имеющиеся сроки годности.")
    public void getAllExistingShelfLife() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.GET_ALL_EXISTING_SHELF_LIFE);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления всех просроченных кодов.")
    @Step("Удаляю все просроченные коды.")
    public void deleteAllExpiredCodes() {
        String json = "";
        String response = ApiMethods.sendPostRequest(Swagger.DELETE_ALL_EXPIRED_CODES, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления просроченных кодов, созданных в диапазоне дат.")
    @Step("Удаляю просроченные коды, созданные в диапазоне дат.")
    public void deleteExpiredCodes() {
        String json = tryToDeleteExpiredCodes();
        String response = ApiMethods.sendPostRequest(Swagger.DELETE_EXPIRED_CODES_DURING_DATES, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления кодов для поискового индекса.")
    @Step("Удаляю коды для поискового индекса.")
    public void deleteCodesForSearchingIndex() {
        String json = tryToDeleteCodesForSearchingIndex();
        String response = ApiMethods.sendPostRequest(Swagger.DELETE_CODES_FOR_SEARCHING_INDEX, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод, позволяющий удалить строки доступным количеством кодов равным нулю из таблицы статистики.")
    @Step("Удаляю строки доступным количеством кодов равным нулю из таблицы статистики.")
    public void deleteStrings() {
        String json = "";
        String response = ApiMethods.sendPostRequest(Swagger.DELETE_STRINGS, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод, позволяющий удалить отпечатанные коды, созданные в диапазоне дат.")
    @Step("Удаляю отпечатанные коды, созданные в диапазоне дат.")
    public void deletingPrintedCodes() {
        String json = tryToDeleteExpiredCodes();
        String response = ApiMethods.sendPostRequest(Swagger.DELETE_PRINTED_CODES_DURING_DATES, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод, позволяющий удалить все отпечатанные коды.")
    @Step("Удаляю все отпечатанные коды.")
    public void deletingAllPrintedCodes() {
        String json = "";
        String response = ApiMethods.sendPostRequest(Swagger.DELETE_ALL_PRINTED_CODES, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод проверяет статус удаления задания.")
    @Step("Проверяю статус удаления задания.")
    public void checkStatusTaskIsDeleted() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.CHECK_STATUS_DELETED_TASK);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для осуществления поиска по индексу: вернул 'name' и по нему происходит поиск с параметрами.")
    @Step("Осуществляю поиск с параметрами.")
    public void checkCountOfAvailableCodesForPrinting() throws SQLException {
        // Создаю предварительное условие в соответствии с тест-кейсом.
        String name = createIndex();
        if (name == null) {
            throw new IllegalStateException("Не удалось создать индекс, name = " + null);
        }

        // Основной сценарий.
        String queryParam = "searching_attribute=" + name;
        String response = ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_INDEX, queryParam);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод получения кода из пула для печати.")
    @Step("Получаю код из пула для печати.")
    public void getCodeFromPrintingPool() throws SQLException {
        // Предварительное условие.
        String json = tryToGetCodeFromPrintingPool();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_DEVICE_FOR_PREAMBLE, json);

        Optional<Object> nameOpt = ExtractingFromJson.extractingAnyFieldFromJson(response, "name", String.class);
        String deviceName = nameOpt.map(Object::toString).orElse(null);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Device added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // Основной сценарий.
        String queryParam = "searching_attribute=" + deviceName;
        //String serverResponse = ApiMethods.sendGetRequest(Swagger.GET_CODE_FROM_PRINTING_POOL, queryParam);
        // Нужно поправить ручной кейс, так как на ручку данные передаются БЕЗ создания заказа и ошибка 500.
    }

    @Description("Метод для обновление времени окончания сроков годности.")
    @Step("Обновляю время окончания сроков годности.")
    public void updateCodesExpirationTime() {
        String json = tryToUpdateCodesExpirationTime();
        String response = ApiMethods.sendPostRequest(Swagger.UPDATE_CODES_EXPIRATION_TIME, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для загрузки кодов.")
    @Step("Загружаю коды.")
    public void codesUpload() {
        String json = tryToUploadCodes();
        String response = ApiMethods.sendPostRequest(Swagger.UPLOAD_CODES, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Removal successful";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления тестовых данных.")
    @Step("Удаляю все тестовые данные из всех связанных таблиц.")
    public static void cleanTestData() throws SQLException {
        // Удаление тестовых данных из таблицы 'hooks' ('datamatrix').
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_HOOKS,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        // Удаление тестовых данных из таблицы 'params' ('datamatrix').
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_PARAMS,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        // Удаление тестовых данных из таблицы 'device' ('datamatrix').
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_DEVICE,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

// PRIVATE ZONE.
    @Step("Выполняю предварительное условие кейса.")
    private String uploadingCodes() {
        String json = tryToCheckCountOfAvailableCodesForPrinting();
        String response = ApiMethods.sendPostRequest(Swagger.CHECK_AVAILABLE_CODES_COUNT, json);

        Optional<Object> nameOpt = ExtractingFromJson.extractingAnyFieldFromJson(response, "name", String.class);
        String name = nameOpt.map(Object::toString).orElse(null);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Data added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return name;
    }

    @Description("Метод для создания предварительного условия в соответствии с тест-кейсом.")
    @Step("Создаю предварительное условие.")
    private String createIndex() {
        String json = tryToCheckCountOfAvailableCodesForPrinting();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_INDEX, json);

        Optional<Object> nameOpt = ExtractingFromJson.extractingAnyFieldFromJson(response, "name", String.class);
        String name = nameOpt.map(Object::toString).orElse(null);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Data added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return name;
    }

    @Description("Вынос полей для формирования 'JSON' для удаления просроченных кодов, созданных в диапазоне дат.")
    @Step("Произвожу сериализацию для удаления просроченных кодов, созданных в диапазоне дат.")
    private String tryToDeleteExpiredCodes() {
        DeleteCodes deleteCodes = DeleteCodes.builder()
                .from_date(GetDate.getTimeAndFormat(true))
                .to_date(GetDate.getTimeAndFormat(true))
                .build();

        LOGGER.info(Constants.GREEN + "Удаление просроченных кодов произошло успешно! Диапазон дат от: " + Constants.RESET +
                Constants.BLUE + deleteCodes.getFrom_date() + Constants.RESET +
                Constants.GREEN + " до: " + Constants.RESET +
                Constants.BLUE + deleteCodes.getTo_date() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(deleteCodes);
    }

    @Description("Вынос полей для формирования 'JSON' для удаления кодов для поискового индекса.")
    @Step("Произвожу сериализацию для удаления кодов для поискового индекса.")
    private String tryToDeleteCodesForSearchingIndex() {
        DeleteCodesForSearchingIndex deleteCodesForSearchingIndex = DeleteCodesForSearchingIndex.builder()
                .codeType("USED")
                .searchingAttribute("TEST: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Удаление кодов для поискового индекса произошло успешно: " + Constants.RESET +
                Constants.BLUE + deleteCodesForSearchingIndex.getCodeType() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(deleteCodesForSearchingIndex);
    }

    @Description("Вынос полей для формирования 'JSON' для проверки счётчика доступных к печати кодов по индексу.")
    @Step("Произвожу сериализацию для проверки счётчика доступных к печати кодов по индексу.")
    private String tryToCheckCountOfAvailableCodesForPrinting() {
        AvailableCodesForPrinting availableCodesForPrinting = AvailableCodesForPrinting.builder()
                .gtin("")
                .name(RandomMethods.createRandomName())
                .nomenclature("UPDATED")
                .sku("TEST: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Проверка счётчика доступных к печати кодов произошла успешно: " + Constants.RESET +
                Constants.BLUE + availableCodesForPrinting.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(availableCodesForPrinting);
    }

    @Description("Вынос полей для формирования 'JSON' для получения кода из пула печати.")
    @Step("Произвожу сериализацию для получения кода из пула печати.")
    private String tryToGetCodeFromPrintingPool() {
        Attributes attributes = Attributes.builder().build();

        Parameters params = Parameters.builder()
                .ADAPTER_TYPE("Test")
                .build();

        CodeFromPrintingPool codeFromPrintingPool = CodeFromPrintingPool.builder()
                .attributes(attributes)
                .batch_dm(RandomMethods.createRandomName())
                .counter(0)
                .name("PRINTER: " + RandomMethods.createRandomName())
                .params(params)
                .production_date(GetDate.getTimeAndFormat(true))
                .send_printed_codes_to_queue(true)
                .state("PAUSE")
                .type("ADAPTER")
                .url("192.168.25.36:8500")
                .build();

        LOGGER.info(Constants.GREEN + "Получение кодов из пула печати произошло успешно: " + Constants.RESET +
                Constants.BLUE + codeFromPrintingPool.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(codeFromPrintingPool);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления времени окончания сроков годности.")
    @Step("Произвожу сериализацию для обновления времени окончания сроков годности.")
    private String tryToUpdateCodesExpirationTime() {
        UpdateExpirationTime updateExpirationTime = UpdateExpirationTime.builder()
                .bbdTime("12:15:44.999")
                .build();

        LOGGER.info(Constants.GREEN + "Обновление времени произошло успешно: " + Constants.RESET +
                Constants.BLUE + updateExpirationTime.getBbdTime() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updateExpirationTime);
    }

    @Description("Метод генерации кода 'GTIN' и вычленения из него 'GTIN' (01) и идентификатора (21), но без самого серийного номера.")
    private List<String> gtinGenerator(int count) {

        return Gs1DataMatrixGenerator.generateMockGs1Codes(
                        count,
                        false,
                        false,
                        false
                ).stream()
                .map(code -> {
                    // Извлекаем GTIN (14 цифр после "(01)")
                    int startPos = code.indexOf("(01)") + 4;
                    return code.substring(startPos, startPos + 14);
                })
                .collect(Collectors.toList());
    }

    @Description("Вынос полей для формирования 'JSON' для загрузки кодов.")
    @Step("Произвожу сериализацию для загрузки кодов.")
    private String tryToUploadCodes() {
        UploadCodes uploadCodes = UploadCodes.builder()
                .bestBeforeDate(GetDate.getTimeAndFormat(true))
                .codes(gtinGenerator(2))
                .searchingAttribute(uploadingCodes())
                .build();

        LOGGER.info(Constants.GREEN + "Загрузка кодов произошла успешно: " + Constants.RESET +
                Constants.BLUE + uploadCodes.getSearchingAttribute() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(uploadCodes);
    }
}