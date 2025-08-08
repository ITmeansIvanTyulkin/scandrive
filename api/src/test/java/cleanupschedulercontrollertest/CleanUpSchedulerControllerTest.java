package cleanupschedulercontrollertest;

import cleanupschedulercontroller.CleanUpSchedulerController;
import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 03.07.2025;
 * <p>
 * @Suite: 'Workstation controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1136
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на получение всех типов расписаний посредством 'API'.
 * <p>
 * 2. Тест на получение всех расписаний по очисткам посредством 'API'.
 * <p>
 * 3. Тест на установку следующего времени срабатывания по расписанию посредством 'API'.
 * <p>
 * 4. Тест на перезапуск задания для отправки сообщений в очередь посредством 'API'.
 * <p>
 * 5. Тест на изменение расписания по очистке посредством 'API'.
 * <p>
 * 6. Тест на удаление расписания по очистке посредством 'API'.
 * </p>
 */

public class CleanUpSchedulerControllerTest {
    private CleanUpSchedulerController step;

    @Before
    public void setup() {
        step = new CleanUpSchedulerController();
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Расписание по очистке")
    @Story("clean-up-scheduler-controller")
    @Feature("Core-printer")
    @DisplayName("Тест на получение всех типов расписаний посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение всех типов расписаний.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1136")
    public void shouldGetAllTypesOfSchedulers() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllTypesOfScheduler();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Расписание по очистке")
    @Story("clean-up-scheduler-controller")
    @Feature("Core-printer")
    @DisplayName("Тест на получение всех расписаний по очисткам посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение всех расписаний по очисткам.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1136")
    public void shouldGetAllTypesOfCleanSchedulers() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllCleanSchedulers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Расписание по очистке")
    @Story("clean-up-scheduler-controller")
    @Feature("Core-printer")
    @DisplayName("Тест на установку следующего времени срабатывания по расписанию посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить установку следующего времени срабатывания по расписанию.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1136")
    public void shouldSetNextCleanScheduler() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.setNextCleanScheduler("* * * * * 1");
            } catch (SQLException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Расписание по очистке")
    @Story("clean-up-scheduler-controller")
    @Feature("Core-printer")
    @DisplayName("Тест на перезапуск задания для отправки сообщений в очередь посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить перезапуск задания для отправки сообщений в очередь.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1136")
    public void shouldRestartTaskToSendMessageToQueue() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.restartTaskToSendMessageToQueue();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Расписание по очистке")
    @Story("clean-up-scheduler-controller")
    @Feature("Core-printer")
    @DisplayName("Тест на изменение расписания по очистке посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить изменение расписания по очистке.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1136")
    public void shouldChangeCleanScheduler() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.changeCleanScheduler();
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Расписание по очистке")
    @Story("clean-up-scheduler-controller")
    @Feature("Core-printer")
    @DisplayName("Тест на удаление расписания по очистке посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление расписания по очистке.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1136")
    public void shouldDeleteCleanScheduler() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.deleteCleanScheduler();
        }, 2);
    }
}