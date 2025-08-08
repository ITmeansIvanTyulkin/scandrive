package monitorcontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import monitorcontroller.MonitorController;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 11.06.2025;
 * <p>
 * @Suite: 'Monitor controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1071
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на успешный контекстный поиск по мониторам посредством 'API'.
 * <p>
 * 2. Тест на успешное добавление монитора посредством 'API'.
 * <p>
 * 3. Тест на успешное получение информации о монитор по его 'id' посредством 'API'.
 * <p>
 * 4. Тест на успешное обновление монитора по его 'id' посредством 'API'.
 * <p>
 * 5. Тест на успешное удаление монитора по его 'id' посредством 'API'.
 * <p>
 * 6. Тест на успешное получение всех уровней информации посредством 'API'.
 * <p>
 * 7. Тест на успешное получение всех протоколов монитора посредством 'API'.
 * <p>
 * 8. Тест на успешное получение параметров протоколов посредством 'API'.
 * <p>
 * 9. Тест на успешное получение всех статусов монитора посредством 'API'.
 */

public class MonitorControllerTest {
    private MonitorController step;

    @Before
    public void setup() {
        step = new MonitorController();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешный контекстный поиск по мониторам посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск по мониторам посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldSearchMonitors() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForMonitors(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное добавление монитора посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление нового монитора посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldAddMonitor() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addingMonitor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное получение информации о мониторе по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о мониторе по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldGetMonitorInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToGetMonitorInfoById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное обновление монитора по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление монитора по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldUpdateMonitorInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateMonitor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное удаление монитора по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление монитора по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldDeleteMonitorInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteMonitorViaApi();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное получение всех уровней информации посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение всех уровней информации посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldGetAllLevelsInfo() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllLevelsInfo();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное получение всех протоколов монитора посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение всех протоколов монитора посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldGetAllMonitorsProtocols() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllMonitorProtocols();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное получение параметров протоколов посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение параметров протоколов посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldGetProtocolsParameters() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.getProtocolsParameters();
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1071")
    @DisplayName("Тест на успешное получение всех статусов монитора посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение всех существующих статусов монитора посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1071")
    public void shouldGetMonitorStatuses() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllMonitorStatuses();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Description("Удаление тестовых данных из базы данных.")
    @AfterClass
    public static void teardown() throws SQLException {
        MonitorController
                .deleteTestData();
    }
}