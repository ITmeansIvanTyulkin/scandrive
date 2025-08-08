package printingadaptforkeeper;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.generators.Gs1DataMatrixGenerator;
import data.randommethods.RandomMethods;
import date.GetDate;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import pojo.addingcodes.AddingCodes;
import pojo.addingprinter.AddingPrinter;
import pojo.addingprinter.Attributes;
import pojo.addingprinter.Params;
import pojo.updatedevice.Order;
import pojo.updatedevice.UpdateDevice;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static org.assertj.core.api.Fail.fail;

public class PrintingAdaptForKeeper {
    private static final Logger LOGGER = Logger.getLogger(PrintingAdaptForKeeper.class.getName());

    // Методы.
    @Description("Метод добавляет принтер и проверяет, что он успешно добавлен.")
    @Step("Добавляю принтер и выполняю проверку успешного добавления.")
    public String tryToAddPrinters() throws SQLException {
        String expectedMessage = "Device added successfully";
        String json = tryToAddPrinter();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_PRINTER, json);
        // Проверка на успешное добавление принтера.
        String message = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        if (expectedMessage.equals(message)) {
            LOGGER.info(Constants.GREEN + "Принтер успешно добавлен: " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
        }
        else {
            fail("Ожидалось сообщение: '" + expectedMessage + "', но получено: '" + message + "'");
        }
        // Распарсиваю имя принтера.
        return ExtractingFromJson.extractingAnyFieldFromJson(response, "data", "name");
    }

    @Description("Метод загружает коды для печати на принтер.")
    @Step("Загружаю коды на печать.")
    public void tryToAddCodes(int count) throws SQLException {
        String expectedMessage = "Removal successful";
        String json = tryToAddDifferentCodes(count); // Установить количество генерируемых кодов.
        String response = ApiMethods.sendPostRequest(Swagger.LOAD_CODES, json);
        // Проверка на успешное добавление кодов.
        String message = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        if (expectedMessage.equals(message)) {
            LOGGER.info(Constants.GREEN + "Коды успешно добавлены: " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
        }
        else {
            fail("Ожидалось сообщение: '" + expectedMessage + "', но получено: '" + message + "'");
        }
    }

    @Description("Метод удаляет все значения из базы данных из таблицы 'datamatrix', включая связанные таблицы.")
    @Step("Делаю зачистку базы данных от тестовых данных.")
    public static void cleanDatabaseTotally(String tableName) throws SQLException {
        try {
            SqlMethods.truncateTableWithCascade(
                    tableName,
                    Credentials.DATABASE_DATAMATRIX_KEEPER,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                    Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Ошибка: " + tableName + ": " + e.getMessage());
        }
    }

    @Description("Метод обновляет устройство.")
    @Step("Обновляю устройство.")
    public void updateDevice(String status) throws SQLException {
        String expectedMessage = "Device updated successfully";
        int id = getIdFromDatabaseDevice();
        String json = tryToUpdateDevice(status);
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_DEVICE + id, json);
        // Проверка на успешное обновление принтера.
        String message = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        if (expectedMessage.equals(message)) {
            LOGGER.info(Constants.GREEN + "Принтер успешно обновлён: " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
        }
        else {
            fail("Ожидалось сообщение: '" + expectedMessage + "', но получено: '" + message + "'");
        }
    }

    @Description("Метод задаёт лимит количества кодов на вывод в ответе, отправляет на ручку запрос на поиск и " +
            "получение существующих кодов по атрибутам, осуществляет проверку успешного запроса по статус-коду, " +
            "имеет дополнительную проверку на ожидаемое сообщение в ответе. Если запрос не успешен - создаётся падение.")
    @Step("Осуществляю поиск и получение кодов по атрибутам.")
    public void tryToGetCodesByAttribute(int limit) throws SQLException {
        String expectedMessage = "Successfully";
        String response = ApiMethods.sendGetRequest(
                Swagger.GET_CODES_BY_ATTRIBUTE + "?searching_attribute=",
                getFieldNameFromDatabaseDevice("name") + "&limit=" + limit
        );
        // Проверка.
        String message = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        if (expectedMessage.equals(message)) {
            LOGGER.info(Constants.GREEN + "Коды по атрибуту успешно получены в количестве, указанном в лимите. " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
        }
        else {
            fail("Ожидалось сообщение: '" + expectedMessage + "', но получено: '" + message + "'");
        }
    }

    @Description("Метод для получения кодов из пула печати задаёт лимит и отправляет запрос на ручку для получения " +
            "существующих кодов, осуществляет проверку успешного запроса по статус-коду, имеет дополнительную проверку " +
            "на ожидаемое сообщение в ответе. Если запрос не успешен - создаётся падение.")
    @Step("Осуществляю получения кодов из пула для печати.")
    public void tryToGetCodesFromPrintingPool(int limit) throws SQLException {
        String expectedMessage = "Successfully";
        String response = ApiMethods.sendGetRequest(
                Swagger.GET_CODES_FROM_PRINTING_POOL + "?device_name=",
                getFieldNameFromDatabaseDevice("name") + "&limit=" + limit
        );
        // Проверка.
        String message = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        if (expectedMessage.equals(message)) {
            LOGGER.info(Constants.GREEN + "Коды успешно получены в количестве, указанном в лимите. " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
        }
        else {
            fail("Ожидалось сообщение: '" + expectedMessage + "', но получено: '" + message + "'");
        }
    }

    @Description("Метод для получения одного кода из пула печати отправляет запрос на ручку для получения " +
            "существующего кода, осуществляет проверку успешного запроса по статус-коду, имеет дополнительную проверку " +
            "на ожидаемое сообщение в ответе. Если запрос не успешен - создаётся падение.")
    @Step("Осуществляю получение одного кода из пула для печати.")
    public void tryToGetACodeFromPrintingPool() throws SQLException {
        String expectedMessage = "Successfully";
        String response = ApiMethods.sendGetRequest(
                Swagger.GET_A_CODE_FROM_PRINTING_POOL + "?device_name=",
                getFieldNameFromDatabaseDevice("name")
        );
        // Проверка.
        String message = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        if (expectedMessage.equals(message)) {
            LOGGER.info(Constants.GREEN + "Коды успешно получены в количестве, указанном в лимите. " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
        }
        else {
            fail("Ожидалось сообщение: '" + expectedMessage + "', но получено: '" + message + "'");
        }
    }

    @Description("Метод получения ID из базы данных из таблицы 'sub_orders'.")
    @Step("Получаю ID из базы данных из таблицы 'sub_orders'.")
    private static int getIdFromDatabaseSubOrders() throws SQLException {
        return getAnyIdFromDatabase(SQLCode.GET_FROM_ORDERS);
    }

    @Description("Метод получения ID из базы данных из таблицы 'device'.")
    @Step("Получаю ID из базы данных из таблицы 'device'.")
    private static int getIdFromDatabaseDevice() throws SQLException {
        return getAnyIdFromDatabase(SQLCode.GET_FROM_DEVICE);
    }

    @Description("Метод получения имени принтера из базы данных из таблицы 'device'.")
    @Step("Получаю имя принтера из базы данных из таблицы 'device'.")
    private static String getFieldNameFromDatabaseDevice(String name) throws SQLException {
        return getAnyStringFieldFromDatabase(SQLCode.GET_ANY_FIELD_NAME, name);
    }

    @Description("Запрос в базу данных для получения строкового значения из любой таблицы.")
    private static String getAnyStringFieldFromDatabase(String request, String name) throws SQLException {
        return SqlMethods.getIdFromDatabase(
                request,
                name,
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
    }

    @Description("Запрос в базу данных для получения ID из любой таблицы.")
    private static int getAnyIdFromDatabase(String request) throws SQLException {
        String id = SqlMethods.getIdFromDatabase(
                request,
                "id",
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
        return Integer.parseInt(id);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления принтера.")
    private String tryToAddPrinter() throws SQLException {
        String someText = RandomMethods.createRandomName();

        AddingPrinter addingPrinter = new AddingPrinter();
        Attributes attributes = new Attributes();

        addingPrinter.setAttributes(attributes);
        addingPrinter.setBatch_dm(someText);
        addingPrinter.setCounter(0);
        addingPrinter.setName("TEST PRINTER " + someText);

        Params params = new Params();
        params.setTEMPLATE("TEMPLATE " + someText);
        params.setBUFFER_SIZE("100");
        addingPrinter.setParams(params);

        addingPrinter.setProduction_date(GetDate.getTimeAndFormat(true));
        addingPrinter.setSend_printed_codes_to_queue(true);
        addingPrinter.setState("PAUSE");
        addingPrinter.setType("GODEX");
        addingPrinter.setUrl("localhost:9100");

        Gson gson = new Gson();
        return gson.toJson(addingPrinter);
    }

    @Description("Вынос полей для формирования 'JSON' для загрузки кодов.")
    private String tryToAddDifferentCodes(int count) throws SQLException {
        AddingCodes addingCodes = new AddingCodes();
        addingCodes.setBestBeforeDate(GetDate.getTimeAndFormat(true));
        // Устанавливаем список кодов напрямую
        List<String> generatedCodes = Gs1DataMatrixGenerator.generateMockGs1Codes(
                count,  // количество кодов
                false,  // includeExpiry
                false,  // includeBatch
                false   // includeCRC
        );
        // Преобразуем формат (убираем скобки).
        List<String> formattedCodes = generatedCodes.stream()
                .map(code -> code.replaceAll("[()]", ""))
                .collect(Collectors.toList());
        // Устанавливаем коды.
        addingCodes.setCodes(formattedCodes);
        // Создаю принтер и прописываю его имя.
        addingCodes.setSearchingAttribute(tryToAddPrinters());

        Gson gson = new Gson();
        return gson.toJson(addingCodes);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления устройства.")
    private String tryToUpdateDevice(String status) throws SQLException {
        String someText = RandomMethods.createRandomName();

        UpdateDevice updateDevice = new UpdateDevice();
        Attributes attributes = new Attributes();

        updateDevice.setAttributes(attributes);

        updateDevice.setBatch_dm(someText);

        Order order = new Order();
        order.setId(getIdFromDatabaseSubOrders());
        updateDevice.setOrder(order);

        updateDevice.setCounter(0);
        updateDevice.setName(getFieldNameFromDatabaseDevice("name"));

        Params params = new Params();
        params.setTEMPLATE(someText);
        params.setBUFFER_SIZE("100");
        updateDevice.setParams(params);

        updateDevice.setProduction_date(GetDate.getTimeAndFormat(true));
        updateDevice.setSend_printed_codes_to_queue(true);
        updateDevice.setState(status);
        updateDevice.setType("GODEX");
        updateDevice.setUrl("localhost:8095");

        Gson gson = new Gson();
        return gson.toJson(updateDevice);
    }
}