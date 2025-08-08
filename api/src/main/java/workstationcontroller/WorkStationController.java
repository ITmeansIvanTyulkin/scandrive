package workstationcontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import printingcontroller.PrintingController;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;
import workstationcontroller.pojo.PrintTemplate;
import workstationcontroller.pojo.Printer;
import workstationcontroller.pojo.Scanner;
import workstationcontroller.pojo.UpdateWorkstation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WorkStationController extends PrintingController {
    PrintingController printingController = new PrintingController();
    private static final Logger LOGGER = Logger.getLogger(WorkStationController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод добавления рабочей станции.")
    @Step("Создаю рабочую станцию.")
    public String addWorkStation() throws SQLException {
        return printingController.createWorkStation();
    }

    @Description("Метод для очистки тестовых данных в базе данных.")
    @Step("Произвожу очистку тестовых данных в базе данных.")
    public void deleteTestDataInDatabase() throws SQLException {
        printingController.deleteTestPrinterInDatabase();
    }

    @Description("Метод для осуществления контекстного поиска рабочих станций.")
    @Step("Выполняю поиск рабочих станций.")
    public int searchForWorkstations(int pageNumber, int pageSize) throws Exception {
        // Создаю рабочую станцию.
        addWorkStation();

        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String response = ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_WORKSTATIONS, paramString);

        // Распарсиваю 'id'.
        List<Integer> ids = ExtractingFromJson.extractingAnyFieldFromJson(response);

        if (!ids.isEmpty()) {
            int id = ids.get(0);

            // Проверки.
            // Проверка по сообщению от сервера.
            String expectedMessage = "Successfully";
            String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
            assertThat(actualMessage)
                    .as("Проверка сообщения об успехе.")
                    .isEqualTo(expectedMessage);

            return id;
        }
        throw new RuntimeException("Name не найден в ответе сервера");
    }

    @Description("Метод создаёт новую рабочую станцию, распарсивает её 'id', затем сериализует новые данные (название) " +
            "и обновляет рабочую станцию. При этом после создания рабочей станции из базы данных парсятся 'id' шаблона " +
            "печати, принтера и сканера и затем передаются на сериализацию.")
    @Step("Осуществляю обновление рабочей станции.")
    public void updateWorkstation() throws Exception {
        // Создаю рабочую станцию, нахожу её и получаю её 'id'.
        int id = searchForWorkstations(1, 50);
        // Обновляю рабочую станцию.
        String json = tryToUpdateWorkStation();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_WORKSTATION + id, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The workstation has been successfully updated";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка, что итоговое значение колонки 'name' таблицы содержит 'UPDATED' - то есть было обновлено.
        SqlMethods.isValueUpdated(
                SQLCode.UPDATED_NAME_EXISTS,
                "workstation",
                "name",
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }

    @Description("Метод для получения информации по рабочей станции по её 'id'")
    @Step("Получение информации рабочей станции по её 'id'.")
    public void getWorkstationInfoById() throws Exception {
        // Создаю рабочую станцию, нахожу её и получаю её 'id'.
        int id = searchForWorkstations(1, 50);
        String response = ApiMethods.sendGetRequest(Swagger.GET_WORKSTATION_INFO_BY_ID, String.valueOf((id)));
        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Удаление тестовых данных.")
    @Step("Удаляю созданные тестовые данные.")
    public void deleteWorkstationById() throws Exception {
        // Создаю рабочую станцию, нахожу её и получаю её 'id'.
        int id = searchForWorkstations(1, 50);
        String response = ApiMethods.sendGetRequest(Swagger.DELETE_WORKSTATION_INFO_BY_ID, String.valueOf((id)));
        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

// PRIVATE ZONE.
    @Description("Метод для получения одного ID из БД по указанному условию.")
    @Step("Получаю нужный 'id' из базы данных.")
    private int getIdOfPrintTemplate(String request) throws SQLException {
        String id = SqlMethods.getIdFromDatabase(
                request,
                "id",
                Credentials.DATABASE_DATAMATRIX_KEEPER,
                Credentials.DATABASE_DATAMATRIX_KEEPER_LOGIN,
                Credentials.DATABASE_DATAMATRIX_KEEPER_PASSWORD
        );
        return Integer.parseInt(id);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления рабочей станции.")
    @Step("Произвожу сериализацию для обновления рабочей станции.")
    private String tryToUpdateWorkStation() throws Exception {
        // Извлекаю из базы данных 'id' шаблона.
        PrintTemplate printTemplate = PrintTemplate.builder()
                .id(getIdOfPrintTemplate(SQLCode.GET_ID_FROM_PRINT_TEMPLATE))
                .build();

        // Извлекаю из базы данных 'id' принтера.
        Printer printer = Printer.builder()
                .id(getIdOfPrintTemplate(SQLCode.GET_ID_FROM_PRINTER))
                .build();

        // Извлекаю из базы данных 'id' сканера.
        Scanner scanner = Scanner.builder()
                .id(getIdOfPrintTemplate(SQLCode.GET_ID_FROM_SCANNER))
                .build();

        UpdateWorkstation updateWorkstation = UpdateWorkstation.builder()
                .autoprint_l2_aggregate(true)
                .is_dataset_aggregation_enabled(true)
                .name("UPDATED WORKSTATION")
                .print_template(printTemplate)
                .printer(printer)
                .scanner(scanner)
                .build();

        LOGGER.info(Constants.GREEN + "Обновление рабочей станции успешно совершено! Имя рабочей станции: " + Constants.RESET +
                Constants.BLUE + updateWorkstation.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updateWorkstation);
    }
}