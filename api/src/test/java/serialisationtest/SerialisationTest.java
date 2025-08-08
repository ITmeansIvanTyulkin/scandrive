package serialisationtest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.*;
import retryfailedtests.RetryFailedTests;
import serialisation.Serialisation;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 25.04.2025;
 * <p>
 * <p>
 *     Сьют по 'core-reader' 'Сериализация. Модуль считывания. Считывание кодов, создание маршрута' по таску: https://algaid.atlassian.net/browse/COREDEV-878
 * </p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на успешное создание точки посредством 'API'.
 * <p>
 * 2. Тест на успешное создание точки типа 'RABBIT_MQ' посредством 'API'.
 * <p>
 * 3. Тест на успешное создание маршрута на основании 2-х ранее созданных точек посредством 'API'.
 * <p>
 * 4. Тест на успешное обновление точки типа 'TCP' посредством 'API'.
 * <p>
 *
 */

public class SerialisationTest {
    private static Serialisation step;

    @Before
    public void setup() throws InterruptedException {
        step = new Serialisation();
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-878")
    @DisplayName("Тест на успешное создание точки типа 'TCP' посредством 'API'.")
    @Description("Проверка, что можно успешно создать точку посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-878")
    public void shouldAddNewTcpPoint() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addNewTcpPoint();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-878")
    @DisplayName("Тест на успешное создание точки типа 'RABBIT_MQ' посредством 'API'.")
    @Description("Проверка, что можно успешно создать точку посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-878")
    public void shouldAddNewRabbitPoint() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            step.addNewRabbitPoint();
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-878")
    @DisplayName("Тест на успешное создание маршрута на основании 2-х ранее созданных точек посредством 'API'.")
    @Description("Проверка, что можно успешно создать маршрут посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-878")
    public void shouldMakeRoute() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.makeRoute();
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-878")
    @DisplayName("Тест на успешное обновление точки типа 'TCP' посредством 'API'.")
    @Description("Проверка, что можно успешно обновить точку посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-878")
    public void shouldUpdateTcpPoint() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateNewPoint();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void tearDown() throws SQLException {
        step.deleteRoute();
    }

    @Description("Удаление тестовых данных (осуществляется последовательное удаление всех существующих точек).")
    @AfterClass
    public static void teardown() throws Exception {
        step.deleteRoute();
        step.cleanAllPointsInDmBus();
    }
}