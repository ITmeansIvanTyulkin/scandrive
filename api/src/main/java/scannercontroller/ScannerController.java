package scannercontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import scannercontroller.pojo.AddScanner;
import scannercontroller.pojo.UpdateScanner;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ScannerController {
    private static final Logger LOGGER = Logger.getLogger(ScannerController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для добавления сканера.")
    @Step("Добавляю сканер.")
    public int addScanner() throws SQLException {
        String json = tryToAddScanner();
        String response = ApiMethods.sendPostRequest(Swagger.ADD_SCANNER, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The scanner has been added";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Получение ID для проверки в БД и возврата в другой метод (если понадобится).
        int id = (int) ExtractingFromJson.extractingAnyFieldFromJson(response, "id", Integer.class)
                .orElseThrow(() ->
                        new RuntimeException("Не удалось извлечь ID из ответа"));

        // 3. Проверка существования сканера в БД по ID.
        SqlMethods.isExist(
                SQLCode.SCANNER_EXISTS,
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD);

        return id;
    }

    @Description("Метод с пагинацией для осуществления контекстного поиска сканеров.")
    @Step("Осуществляю контекстный поиск сканеров с пагинацией.")
    public String searchForScanners(int pageNumber, int pageSize) throws SQLException {
        // Добавляю сканер.
        addScanner();
        // Осуществляю контекстный поиск существующих сканеров.
        Map<String, Object> params = new HashMap<>();
        params.put("page_number", pageNumber);
        params.put("page_size", pageSize);

        // Конвертируем map в строку формата queryString.
        String paramString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return ApiMethods.sendGetRequest(Swagger.SEARCH_FOR_SCANNERS, paramString);
    }

    @Description("Метод, направленный на получение информации о сканере по 'id'.")
    @Step("Получаю информацию о сканере по 'id'.")
    public void getScannerInfoById() throws Exception {
        // Осуществляю контекстный поиск сканеров с пагинацией.
        String scannersExistedIds = searchForScanners(1, 50);
        // Забираю все найденные 'ids' в коллекцию.
        List<Integer> ids = ExtractingFromJson.extractingAnyFieldFromJson(scannersExistedIds);
        // Выбираю из коллекции случайный 'id'.
        int id = getRandomId(ids);
        // Отправляю выбранный рандомный 'id' на ручку.
        String response = ApiMethods.sendGetRequest(Swagger.GET_SCANNER_INFO_BY_ID + id);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для обновления информации о сканере по его 'id'.")
    @Step("Обновляю информацию о сканере по его 'id'.")
    public void updateScanner() throws SQLException {
        int id = addScanner();
        String json = tryToUpdateScanner();
        String response = ApiMethods.sendPutRequest(Swagger.UPDATE_SCANNER + id, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The scanner has been updated";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления информации о сканере по его 'id'.")
    @Step("Удаляю информацию о сканере по его 'id'.")
    public void deleteScanner() throws SQLException {
        int id = addScanner();
        String response = ApiMethods.sendDeleteRequest(Swagger.DELETE_SCANNER, String.valueOf(id));

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "The scanner has been deleted";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        // 2. Проверка существования сканера в БД по ID (в консоли ждём 'false' - например, ID: 84 существует в базе данных: false).
        SqlMethods.isExist(
                SQLCode.SCANNER_EXISTS,
                String.valueOf(id),
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD);
    }

    @Description("Метод для удаления тестовых данных из базы данных.")
    @Step("Удаляю все тестовые данные из базы данных.")
    public void deleteTestPrinterInDatabase() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_WORK_STATION,
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
    }

// PRIVATE ZONE.
    @Description("Метод для выбора случайного 'id' из коллекции полученных.")
    @Step("Выбираю случайный 'id' из коллекции полученных.")
    private static Integer getRandomId(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Список ID не должен быть пустым");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(ids.size());
        Integer randomId = ids.get(randomIndex); // Получаем значение id по индексу

        LOGGER.info(Constants.GREEN + "Выбран случайный 'id': " + Constants.RESET
                + Constants.BLUE + randomId + Constants.RESET
                + " (индекс: " + randomIndex + ")");
        return ids.get(randomIndex);
    }

    @Description("Вынос полей для формирования 'JSON' для создания сканера.")
    @Step("Произвожу сериализацию для создания сканера.")
    private String tryToAddScanner() {
        AddScanner addScanner = AddScanner.builder()
                .name("TEST " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Шаблон печати успешно создан: " + Constants.RESET +
                Constants.BLUE + addScanner.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(addScanner);
    }

    @Description("Вынос полей для формирования 'JSON' для обновления сканера.")
    @Step("Произвожу сериализацию для обновления сканера.")
    private String tryToUpdateScanner() {
        UpdateScanner updateScanner = UpdateScanner.builder()
                .name("TEST " + RandomMethods.createRandomName())
                .build();

        LOGGER.info(Constants.GREEN + "Шаблон печати успешно создан: " + Constants.RESET +
                Constants.BLUE + updateScanner.getName() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(updateScanner);
    }
}