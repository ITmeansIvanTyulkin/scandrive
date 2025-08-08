package loadcodesviarabbitmq;

import data.constants.Constants;
import data.credentials.Credentials;
import data.endpoints.apiendpoints.Swagger;
import dto.TotalMarkEvent;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import messaging.RabbitMessageHelper;
import service.RabbitMqSender;
import sqlqueries.SQLCode;
import sqlqueries.sqlutils.SqlMethods;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class LoadCodesViaRabbitMQ {
    private static final Logger LOGGER = Logger.getLogger(LoadCodesViaRabbitMQ.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод, в котором происходит подготовка кодов для последующей загрузки и затем отправка сообщения.")
    @Step("Осуществляю подготовку данных, создаю детальный лог происходящего, отправляю сообщение в RabbitMQ.")
    public void sendMarkingMessage() {
        // 1. Осуществляю подготовку данных.
        List<String> markingCodes = List.of(
                "0104607072196678215VO<eU\u001D93qv5A",
                "01046070721966782156iEf>\u001D93P5EW",
                "0104607072196678215RJMr1\u001D93k?By",
                "0104607072196678215<IiIN\u001D93OMsF",
                "0104607072196678215jU3ZS\u001D936xHn",
                "0104607072196678215Bx4U8\u001D93t2O=",
                "0104607072196678215Z5de5\u001D93NJ2J",
                "01046070721966782158xX?Y\u001D93BZA3"
        );

        try {
            TotalMarkEvent event = RabbitMessageHelper.createTotalMarkEvent(
                    markingCodes,
                    "2025-010-18T00:00:00.000Z",      // best_before_date
                    "2025-010-18T00:00:00.000Z"                     // date_of_manufacture
            );

            // Логирование (полное) перед отправкой.
            String json = new com.google.gson.GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(event);

            LOGGER.info(Constants.GREEN + "Отправляемый JSON:\n" + Constants.RESET + Constants.BLUE + json + Constants.RESET);

            RabbitMqSender.sendMessage(event, Swagger.QUEUE_EXTERNAL_DATAMATRIX_CODES);
            LOGGER.info(Constants.GREEN + "✅ Сообщение успешно отправлено в очередь!" + Constants.RESET);

        } catch (Exception e) {
            LOGGER.severe("❌ Ошибка отправки: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Description("Метод удаления тестовых данных из 'conveyor' из таблицы 'external_datamatrix_codes'.")
    @Step("Удаляю тестовые данные из таблицы 'external_datamatrix_codes'.")
    public void deleteTestDataFromDatabase() throws SQLException {
        SqlMethods.cleanDatabase(
                SQLCode.CLEAN_DATA_BASE_EXTERNAL_DATAMATRIX_CODES,
                Credentials.DATABASE_CONVEYOR_CORE,
                Credentials.DATABASE_CONVEYOR_CORE_LOGIN,
                Credentials.DATABASE_CONVEYOR_CORE_PASSWORD
        );
    }




// PRIVATE ZONE.
}