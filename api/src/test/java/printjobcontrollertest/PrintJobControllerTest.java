package printjobcontrollertest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import printjobcontroller.PrintJobController;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 17.07.2025;
 * <p>
 * @Suite: 'Print job controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1222
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на добавление задания на печать посредством 'API'.
 * <p>
 * 2. Тест на поиск информации о задании на печать по 'id' посредством 'API'.
 * <p>
 * 3. Тест на действия над заданиями: cancel - отменить, finish - завершить посредством 'API'.
 * <p>
 * 4. Тест на контекстный поиск задания посредством 'API'.
 * <p>
 * 5. Тест на удаление задания на печать посредством 'API'.
 * </p>
 */

public class PrintJobControllerTest {
    private PrintJobController step;

    @Before()
    public void setup() {
        step = new PrintJobController();
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("print-job-controller")
    @Feature("core-printer")
    @DisplayName("Тест на добавление задания на печать посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление задания на печать.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1222")
    public void shouldAddPrintTask() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addingPrintTask();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("print-job-controller")
    @Feature("core-printer")
    @DisplayName("Тест на поиск информации о задании на печать по 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить поиск информации о задании на печать по 'id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1222")
    public void shouldSearchForPrintTask() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForPrintTask();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("print-job-controller")
    @Feature("core-printer")
    @DisplayName("Тест на действия над заданиями: cancel - отменить, finish - завершить посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить действия над заданиями: cancel - отменить, finish - завершить.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1222")
    public void shouldTestTasksActs() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tasksActing(false, "FINISH");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("print-job-controller")
    @Feature("core-printer")
    @DisplayName("Тест на контекстный поиск задания посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск задания.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1222")
    public void shouldSearchForTask() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForTasks(
                        "2025-05-28T00:00:00.755Z",
                        "2025-05-29T00:00:00.755Z",
                        1, 50, true
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("print-job-controller")
    @Feature("core-printer")
    @DisplayName("Тест на удаление задания на печать посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление задания на печать.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1222")
    public void shouldDeletePrintTask() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deletePrintTask();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After()
    @Description("Метод для удаления всех созданных в процессе регресса тестовых данных из базы данных - всех " +
            "связанных таблиц: 'print_job', 'datamatrix', 'sub_orders', 'device'.")
    public void teardown() throws SQLException {
        step.deleteTestDataFromBd();
    }
}