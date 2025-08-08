package printerlockscontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import devicecontrollerv2.DeviceControllerV2;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import printerlockscontroller.pojo.AddingBlockPrinter;
import printerlockscontroller.pojo.UpdateTypeAndReasonPrinterBlocked;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrinterLocksController extends DeviceControllerV2 {
    private static final Logger LOGGER = Logger.getLogger(PrinterLocksController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для получения списка установленных блокировок с пагинацией.")
    @Step("Получаю список установленных блокировок с пагинацией.")
    public String getBlocksSet(int pageNumber, int pageSize) throws SQLException {
        // Осуществляю контекстный поиск существующих сканеров.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String response = ApiMethods.sendGetRequest(Swagger.GET_BLOCKED_SET_LIST, paramString);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
        return response;
    }

    @Description("Метод для добавления блокировки для принтера. Есть преамбула (используется при сериализации) " +
            "для создания принтера. Возвращает имя созданного принтера и затем - основной сценарий, когда " +
            "возвращённое имя участвует в сериализации для блокировки принтера.")
    @Step("Создаю принтер и добавляю блокировку принтера.")
    public String addingBlockForPrinter() throws SQLException {
        String json = tryToBlockPrinter();
        String response = ApiMethods.sendPostRequest(Swagger.MAKE_PRINTER_BLOCKED, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Printer blocking successfully added";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
        return response;
    }

    @Description("Преамбула: создание принтер и добавление блокировки принтера.")
    @Step("Обновляю тип и причину блокировки для принтера.")
    public void updateTypeAndReasonPrinterBlocked() throws SQLException {
        int idToBring = 0;

        String serverResponse = addingBlockForPrinter();

        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToBring = id;
            LOGGER.info(Constants.GREEN + "Extracted ID: " + Constants.RESET + Constants.BLUE + idToBring + Constants.RESET);
        } else {
            LOGGER.info("ID not found in JSON");
        }

        String json = tryToUpdateTypeAndReasonPrinterBlocked();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_TYPE_AND_REASON_PRINTER_BLOCKED + idToBring, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The printer seal has been successfully updated";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Преамбула: создание принтер и добавление блокировки принтера.")
    @Step("Удаляю блокировку для принтера.")
    public void deletePrinterBlocked() throws SQLException {
        int idToBring = 0;

        String serverResponse = addingBlockForPrinter();

        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToBring = id;
            LOGGER.info(Constants.GREEN + "Extracted ID: " + Constants.RESET + Constants.BLUE + idToBring + Constants.RESET);
        } else {
            LOGGER.info("ID not found in JSON");
        }
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_PRINTER_BLOCKED, String.valueOf(idToBring));

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Printer blocking removed successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления тестовых данных.")
    @Step("Произвожу удаление тестовых данных из базы данных таблиц 'params' и 'device'.")
    public void deleteTestData() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_PARAMS,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_TEST_DATA_IN_PRINTER_LOCKS,
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
    @Step("Предварительное условие: создаю принтер и возвращаю его имя.")
    private String getDeviceName() throws SQLException {
        int id = DeviceControllerV2.addingPrinter();

        return SqlMethods.getValueFromDatabase(
                SQLCode.GET_DEVICE_NAME + id,
                "name",
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD,
                String.class
        );
    }

    @Description("Вынос полей для формирования 'JSON' для блокировки принтера.")
    @Step("Произвожу сериализацию для блокировки принтера.")
    private String tryToBlockPrinter() throws SQLException {
        AddingBlockPrinter addingBlockPrinter = AddingBlockPrinter.builder()
                .entity_lock("EDIT_LOCK")
                .printer_name(getDeviceName())
                .reason("0000-0000-0000")
                .build();

        LOGGER.info(Constants.GREEN + "Блокировка принтера произошла успешно: " + Constants.RESET +
                Constants.BLUE + addingBlockPrinter.getPrinter_name() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addingBlockPrinter);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления блокировки принтера.")
    @Step("Произвожу сериализацию для обновления блокировки принтера.")
    private String tryToUpdateTypeAndReasonPrinterBlocked() {
        UpdateTypeAndReasonPrinterBlocked update = UpdateTypeAndReasonPrinterBlocked.builder()
                .entity_lock("DELETE_LOCK")
                .reason("UPDATED" + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Обновление блокировки принтера произошло успешно: " + Constants.RESET +
                Constants.BLUE + update.getReason() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(update);
    }
}