package loadcodesviarabbitmqtest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import loadcodesviarabbitmq.LoadCodesViaRabbitMQ;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 30.07.2025;
 * <p>
 * @Suite: 'Load codes via RabbitMQ' по таску: https://algaid.atlassian.net/browse/COREDEV-1264
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests: </p>
 * 1. Тест на загрузку внешних КМ через RabbitMQ.
 * </p>
 * <p>
 * @FYI:
 * В блоке '@After()' происходит очистка базы данных от тестовых данных.
 * </p>
 */

public class LoadCodesViaRabbitMQTest {
    private LoadCodesViaRabbitMQ step;

    @Description("Инициализация.")
    @Before()
    public void setup() {
        step = new LoadCodesViaRabbitMQ();
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(7)
    @Suite("Загрузка внешних КМ")
    @Story("Aggregation")
    @Feature("Функционал аггригации")
    @DisplayName("Тест на загрузку внешних КМ через RabbitMQ.")
    @Description("Проверка, что можно успешно осуществить загрузку внешних КМ через RabbitMQ.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1264")
    public void shouldLoadCodesViaRabbitMQ() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.sendMarkingMessage();
        }, 2);
    }

    @Description("Удаление тестовых данных.")
    @After()
    public void teardown() throws SQLException {
        step.deleteTestDataFromDatabase();
    }
}