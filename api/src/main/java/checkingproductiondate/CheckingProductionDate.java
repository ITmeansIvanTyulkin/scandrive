package checkingproductiondate;

import aggregation.Aggregation;
import checkingproductiondate.pojo.AggregationMessage;
import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import data.randommethods.RandomMethods;
import exception.RabbitMqException;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import serialisation.Serialisation;
import service.RabbitMqConsumer;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CheckingProductionDate {
    private final Serialisation serialisation = new Serialisation();
    private final Aggregation aggregation = new Aggregation();
    private static final Logger LOGGER = Logger.getLogger(CheckingProductionDate.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Step("Создаю маршрут.")
    public void makingRoute() throws SQLException, InterruptedException {
        serialisation.makeRoute();
    }

    @Step("Создаю тестовую линию агрегации, учитывая правила фильтрации, правила агрегации.")
    public void makingAggregation() throws SQLException {
        String response = aggregation.addNewAggregationLine();
        System.out.println(response);
    }

    @Description("Метод удаления тестовых данных из всех связанных таблиц в базе данных по агрегации.")
    @Step("Удаляю тестовые данные по агрегации из базы данных.")
    public void deleteAggregationLine() throws SQLException {
        aggregation.cleanTestDataInDatabase();
    }

    @Description("Метод удаления созданного тестового маршрута.")
    @Step("Удаляю тестовый маршрут.")
    public void deleteTestRoute() throws SQLException {
        serialisation.cleaningDatabaseBeforeTests();
    }

    @Description("Метод удаления созданных точек.")
    @Step("Удаляю тестовые точки.")
    public void deletePoints() throws Exception {
        serialisation.cleanAllPointsInDmBus();
    }

    @Description("Метод создаёт точки и маршрут, затем линию агрегации с правилами, затем удаляет все тестовые данные. " +
            "По базе данных проверить нельзя.")
    @Step("Создаю точки, маршрут и линию агрегации с правилами.")
    public void thread() throws Exception {
        makingRoute();
        makingAggregation();
        deleteAggregationLine();
        deleteTestRoute();
        deletePoints();
        listeningToQueueAndGetMassageToCheck();
    }

    @Description("Метод создания агрегационной линии с правилами на ручку: альтернативный вариант.")
    @Step("Использую альтернативный вариант: создаю агрегационную линию с правилами.")
    public void makingAggregationLine() {
        String json = tryToMakeAggregationLine();
        String response = ApiMethods.sendPostRequest(Swagger.MAKE_AGGREGATION_LINE, json);
        listeningToQueueAndGetMassageToCheck();
    }

// PRIVATE ZONE.
    @Description("Метод для прослушивания очереди и проверки сообщения, которое было в неё отправлено на предмет " +
            "корректной даты.")
    @Step("Слушаю очередь, получаю сообщение и проверяю формат даты.")
    private void listeningToQueueAndGetMassageToCheck() {
        try {
            RabbitMqConsumer consumer = new RabbitMqConsumer();

            // Подписываюсь на очередь и логирую сообщение.
            consumer.consumeMessages(message -> {
                LOGGER.info(Constants.GREEN + "Получено сообщение: " + Constants.RESET + Constants.BLUE + message + Constants.RESET);
            });

            // Инициализация долгого прослушивания.
            Thread.sleep(10_000);
            consumer.close();
        } catch (RabbitMqException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Description("Вынос полей для формирования 'JSON' для линии агрегации.")
    private String tryToMakeAggregationLine() {
        List<String> datamatrixList = Arrays.asList(
                "0140000624647424215ECRhQBQgWE>w\u001D91FFD0\u001D92dGVzdC2LUTJcDD2va939BLFISfjl0aA06DXLJlOgsmA=",
                "0140000624647424215kcMhe;YfLs4a\u001D91FFD0\u001D92dGVzdBiQ88bQaLbAMxUkbt8oN/vifseujYYW8ToCeB8="
        );

        AggregationMessage aggregationMessage = AggregationMessage.builder()
                .aggregateType("SSCC")
                .aggregationLevel(1)
                .aggregationLineId("TEST: SSKbSbW")
                .batchDm("TEST: " + RandomMethods.createRandomName())
                .creationDate("2025-08-04T11:49:22.877Z")
                .customText(RandomMethods.createRandomName())
                .datamatrix(datamatrixList)
                .expirationDate("2025-08-04T11:49:22.877Z")
                .organisationGuid(RandomMethods.createRandomName())
                .productionDate("2025-08-04T11:49:22.877Z")
                .build();

        LOGGER.info(Constants.GREEN + "Линия агрегации успешно создана: " + Constants.RESET +
                Constants.BLUE + aggregationMessage.getBatchDm() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(aggregationMessage);
    }
}