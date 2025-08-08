package workstationcontrollertest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;
import workstationcontroller.WorkStationController;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 02.07.2025;
 * <p>
 * @Suite: 'Workstation controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1132
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на добавление рабочей станции посредством 'API'.
 * <p>
 * 2. Тест на успешный контекстный поиск рабочей станции посредством 'API'.
 * <p>
 * 3. Тест на успешное обновление рабочей станции посредством 'API'.
 * <p>
 * 4. Тест на успешное получение информации рабочей станции по её 'id' посредством 'API'.
 * <p>
 * 5. Тест на успешное удаление информации рабочей станции по её 'id' посредством 'API'.
 */

public class WorkStationControllerTest {
    private WorkStationController step;

    @Description("Инициализация экземпляра класса в шаг.")
    @Before
    public void setup() throws SQLException {
        step = new WorkStationController();
        // Удаление тестовых данных из базы данных.
        step.deleteTestDataInDatabase();
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Рабочие станции")
    @Story("workstation-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на добавление рабочей станции посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление рабочей станции.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1132")
    public void shouldAddWorkStation() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addWorkStation();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Рабочие станции")
    @Story("workstation-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешный контекстный поиск рабочей станции посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск рабочей станции.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1132")
    public void shouldSearchForWorkStation() {
        RetryFailedTests.runTestWithRetry(() -> {
        try {
            step.searchForWorkstations(1, 50);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Рабочие станции")
    @Story("workstation-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное обновление рабочей станции посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление рабочей станции.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1132")
    public void shouldUpdateWorkStation() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateWorkstation();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Рабочие станции")
    @Story("workstation-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное получение информации рабочей станции по её 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации рабочей станции по её 'id'.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1132")
    public void shouldGetWorkStationInfo() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getWorkstationInfoById();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Рабочие станции")
    @Story("workstation-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное удаление информации рабочей станции по её 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление информации рабочей станции по её 'id'.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1132")
    public void shouldDeleteWorkStationInfo() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteWorkstationById();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Description("Удаление тестовых данных из базы данных.")
    @After
    public void teardown() throws SQLException {
        step.deleteTestDataInDatabase();
    }
}