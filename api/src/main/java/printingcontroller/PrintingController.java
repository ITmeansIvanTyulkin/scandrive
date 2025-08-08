package printingcontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import org.json.JSONObject;
import printercontroller.pojo.AddPrinter;
import printingcontroller.pojo.*;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrintingController {
    String idToBring;
    private static final Logger LOGGER = Logger.getLogger(PrintingController.class.getName());

    // Методы.
    @Description("Метод тестирует возможность прямой печати и проверяет, что всё удачно по сообщению от сервера. В БД проверить нельзя.")
    @Step("Тестирую возможность прямой печати.")
    public void directPrintingTest() {
        String json = tryToDoDirectPrinting();
        String response = ApiMethods.sendPostRequest(Swagger.DIRECT_PRINTING_TEST, json);
        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The operation is completed";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод тестирует возможность печати по шаблону.")
    @Step("Тестирую возможность печати по шаблону.")
    public void templatePrinting() {
        String json = tryToAddAggregationPrinter();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_AGGREGATION_PRINTER, json);

        // Распарсиваю 'id' созданного принтера.
        Optional<Object> idOptional = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        if (idOptional.isPresent()) {
            int id = (Integer) idOptional.get();
            idToBring = String.valueOf((id));

            // Отправляю на ручку данные (в том числе распарсенный 'id') для печати по шаблону.
            String jsonToRequest = tryToPrintByTemplate();
            String serverResponse = ApiMethods.sendPostRequest(Swagger.TEMPLATE_PRINTING_TEST, jsonToRequest);

            // Проверки.
            // Проверка по сообщению от сервера.
            String expectedMessage = "The operation is completed";
            String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "error");
            assertThat(actualMessage)
                    .as("Проверка сообщения об успехе.")
                    .isEqualTo(expectedMessage);
        }
    }

    @Description("Метод для агрегирования по коду 'Data Matrix'")
    @Step("Произвожу агрегирование по коду 'Data Matrix'.")
    public void aggregateViaDataMatrixCode() throws SQLException {
        String json = tryToAggregateViaDataMatrixCode();
        String serverResponse = ApiMethods.sendPostRequest(Swagger.AGGREGATION_DATAMATRIX_CODE, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The operation is completed";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(serverResponse, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления принтера агрегации (без удаления принтера, второй раз тест упадёт. " +
            "Всё из-за уникальности порта '9100' и при этом рандомизацию порта использовать нельзя, например, " +
            "если сделать порт '910' + 'рандомная цифра' - такой запрос вызывает ошибку.")
    @Step("Произвожу очистку тестовых данных в базе данных.")
    public void deleteTestPrinterInDatabase() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_WORK_STATION,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_PRINTER,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_SCANNER,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );

        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_PRINT_TEMPLATE,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод создания сканнера.")
    @Step("Создаю сканнер.")
    private Integer createScanner() {
        String json = tryToCreateScanner();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_SCANNER, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        Optional<Object> id = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        String expectedMessage = "The scanner has been added";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return (Integer) id.orElseThrow(() ->
                new RuntimeException("Не удалось извлечь ID из ответа"));
    }

    @Description("Метод создания шаблона.")
    @Step("Создаю шаблон.")
    private Integer createTemplate() {
        String json = tryToCreateTemplate();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_TEMPLATE, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        Optional<Object> id = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        String expectedMessage = "The print template has been successfully added";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return (Integer) id.orElseThrow(() ->
                new RuntimeException("Не удалось извлечь ID из ответа"));
    }

    @Description("Метод создания принтера.")
    @Step("Создаю принтер.")
    private Integer createPrinter() {
        String json = tryToAddAggregationPrinter();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_AGGREGATION_PRINTER, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        Optional<Object> id = ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class);
        String expectedMessage = "The printer has been added successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return (Integer) id.orElseThrow(() ->
                new RuntimeException("Не удалось извлечь ID из ответа"));
    }

    @Description("Метод создания рабочей станции.")
    @Step("Создаю рабочую станцию.")
    public String createWorkStation() {
        String json = tryToCreateWorkStation();
        String response = ApiMethods.sendPostRequest(Swagger.CREATE_WORK_STATION, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The workstation was successfully added";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // Парсинг 'name' сканера.
        return parseScannerName(response);
    }

    @Description("Сервисный метод для парсинга имени сканера.")
    private static String parseScannerName(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONObject data = root.getJSONObject("data");
            JSONObject scanner = data.getJSONObject("scanner");

            if (!scanner.has("name")) {
                System.err.println("Ошибка: в scanner нет поля 'name'");
                return null;
            }

            return scanner.getString("name");
        } catch (Exception e) {
            System.err.println("Ошибка парсинга JSON: " + e.getMessage());
            return null;
        }
    }

    @Description("Метод для получения и рандомного выбора кода 'datamatrix' из таблицы 'datamatrix' из 'conveyor-core'.")
    @Step("Извлекаю в коллекцию 10 кодов из таблицы 'datamatrix' из БД, рандомно выбираю любой, преобразую.")
    private String getDatamatrixCodesCollection() throws SQLException {
        List<String> collection =
                SqlMethods.getIdsFromDatabase(
                SQLCode.GET_10_CODES_FROM_DATAMATRIX,
                "full_code",
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );

        if (collection.isEmpty()) {
            throw new RuntimeException("Не удалось получить коды из базы данных или коллекция пуста");
        }

        Random random = new Random();
        return collection.get(random.nextInt(collection.size()));
    }

    @Description("Вынос полей для формирования 'JSON' для прямой печати.")
    @Step("Произвожу сериализацию для создания прямой печати.")
    private String tryToDoDirectPrinting() {
        DirectPrinting directPrinting = DirectPrinting.builder()
                .encoding("UTF-8")
                .printerIp(Swagger.BASE_TEST_PRINTER_IP) // к сожалению, нельзя исползовать рандомизацию, только хардкод.
                .printerPort(Swagger.BASE_TEST_PRINTER_PORT)    // то же самое.
                .sendEnterAtTheEnd(true)
                .template("TEST TEST TEST")
                .testCode("TEST TEST TEST")
                .testLine("TEST")
                .zplGsSeparatorReplacer("TEST")
                .build();

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(directPrinting);
    }

    @Description("Вынос полей для формирования 'JSON' для печати по шаблону.")
    @Step("Произвожу сериализацию для создания печати по шаблону.")
    private String tryToPrintByTemplate() {
        TemplatePrinting templatePrinting = TemplatePrinting.builder()
                .encoding("UTF-8")
                .printerId(Integer.valueOf(idToBring))
                .template("ONE MORE TEST")
                .build();

        LOGGER.info(Constants.GREEN + "Тестовый шаблон для печати успешно создан: " + Constants.RESET + Constants.BLUE + templatePrinting.getPrinterId() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(templatePrinting);
    }

    @Description("Вынос полей для формирования 'JSON' для добавления принтера агрегации.")
    @Step("Произвожу сериализацию для добавления принтера агрегации.")
    protected static String tryToAddAggregationPrinter() {
        AddPrinter addPrinter = AddPrinter.builder()
                .encoding("UTF-8")
                .ip(Swagger.BASE_TEST_PRINTER_IP)    // к сожалению, нельзя исползовать рандомизацию, только хардкод.
                .name("TEST PRINTER: " + RandomMethods.createRandomName())
                .port(Swagger.BASE_TEST_PRINTER_PORT)       // к сожалению, нельзя исползовать рандомизацию, только хардкод.
                .send_enter_at_end(true)
                .template("TEST: " + RandomMethods.createRandomName())
                .zpl_gs1_128_prefix(RandomMethods.createRandomName())
                .zpl_gs1_databar_expanded_prefix(RandomMethods.createRandomName())
                .zpl_gs_separator_replacer(RandomMethods.createRandomName())
                .zpl_sscc_prefix(RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Принтер успешно добавлен: " + Constants.RESET + Constants.BLUE + addPrinter.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addPrinter);
    }

    @Description("Вынос полей для формирования 'JSON' для создания сканера.")
    @Step("Произвожу сериализацию для создания сканера.")
    private String tryToCreateScanner() {
        Scanner scanner = Scanner.builder()
                .name("TEST: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Сканер успешно добавлен: " + Constants.RESET + Constants.BLUE + scanner.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(scanner);
    }

    @Description("Вынос полей для формирования 'JSON' для создания шаблона.")
    @Step("Произвожу сериализацию для создания шаблона.")
    private String tryToCreateTemplate() {
        Template template = Template.builder()
                .enabled_for_conveyor(true)
                .enabled_for_workstation(true)
                .encoding("UTF-8")
                .error_enabled_for_conveyor(true)
                .name("TEST: " + RandomMethods.createRandomName())
                .template("TEST: " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Шаблон успешно добавлен: " + Constants.RESET + Constants.BLUE + template.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(template);
    }

    @Description("Вынос полей для формирования 'JSON' для создания рабочей станции.")
    @Step("Произвожу сериализацию для создания рабочей станции.")
    private String tryToCreateWorkStation() {
        // Создаю сканнер.
        ScannerId scanner = ScannerId.builder()
                .id(createScanner())
                .build();

        // Создаю шаблон.
        PrintTemplate printTemplate = PrintTemplate.builder()
                .id(createTemplate())
                .build();

        // Создаю принтер.
        Printer printer = Printer.builder()
                .id(createPrinter())
                .build();

        WorkStation workStation = WorkStation.builder()
                .autoprint_l2_aggregate(true)
                .is_dataset_aggregation_enabled(true)
                .name("TEST: " + RandomMethods.createRandomName())
                .printTemplate(printTemplate)
                .printer(printer)
                .scanner(scanner)
                .build();

        LOGGER.info(Constants.GREEN + "Рабочая станция успешно создана: " + Constants.RESET + Constants.BLUE + workStation.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(workStation);
    }

    @Description("Вынос полей для формирования 'JSON' для создания агрегирования по коду 'Data matrix'.")
    @Step("Произвожу сериализацию для создания агрегирования по коду 'Data matrix'.")
    private String tryToAggregateViaDataMatrixCode() throws SQLException {
        String datamatrix = getDatamatrixCodesCollection();
        System.out.println(datamatrix);

        String id = createWorkStation();
        System.out.println(id);

        Aggregation aggregation = Aggregation.builder()
                .datamatrix(datamatrix)
                .scannerId(id)
                .build();

        LOGGER.info(Constants.GREEN + "Рабочая станция успешно создана: " + Constants.RESET + Constants.BLUE + aggregation.getScannerId() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(aggregation);
    }
}