package suborderscontrollertest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;
import suborderscontroller.SubOrdersController;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 05.08.2025;
 * <p>
 * @Suite: 'Checking production date' по таску: https://algaid.atlassian.net/browse/COREDEV-1293
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на добавление подзаказа посредством 'API'.
 * <p>
 * 2. Тест на контекстный поиск подзаказов посредством 'API'.
 * <p>
 * 3. Тест на получение информации о подзаказе по {id) посредством 'API'.
 * <p>
 * 4. Тест на обновление подзаказа по {id} посредством 'API'.
 * <p>
 * 5. Тест на получение информации о подзаказе по 'subOrderNumber' посредством 'API'.
 * <p>
 * 6. Тест на удаление подзаказа посредством 'API'.
 * </p>
 * <p>
 * @FYI: Реализовано удаление тестовых данных из всех связанных таблиц после прохождения каждого теста.
 * До начала регресса - также сперва проводится очистка базы данных от данных, оставленных коллегами.
 */

public class SubOrdersControllerTest {
    private SubOrdersController step;

    @BeforeClass()
    @Description("Осуществляю очистку базы данных (всех связанных с таском таблиц) от тестовых данных.")
    public static void cleanDatabase() throws SQLException {
        SubOrdersController.deleteTestDataFromDatabase();
    }

    @Before()
    @Description("Произвожу инициализацию шага в качестве экземпляра класса.")
    public void setup() {
        step = new SubOrdersController();
    }

    @Test
    @Tag("API")
    @Sprint(41)
    @Release(8)
    @Suite("Подзаказ")
    @Story("sub-orders-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на добавление подзаказа посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление подзаказа.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1293")
    public void shouldAddSubOrder() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.createSubOrder();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(41)
    @Release(8)
    @Suite("Подзаказ")
    @Story("sub-orders-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на контекстный поиск подзаказов посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск подзаказов.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1293")
    public void shouldSearchForSubOrders() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForSubOrders(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(41)
    @Release(8)
    @Suite("Подзаказ")
    @Story("sub-orders-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на получение информации о подзаказе по {id) посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о подзаказе по {id).")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1293")
    public void shouldGetSubOrderInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getSubOrderInfoById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(41)
    @Release(8)
    @Suite("Подзаказ")
    @Story("sub-orders-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на обновление подзаказа по {id} посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление подзаказа по {id}.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1293")
    public void shouldUpdateSubOrderInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateSubOrder();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(41)
    @Release(8)
    @Suite("Подзаказ")
    @Story("sub-orders-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на получение информации о подзаказе по 'subOrderNumber' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о подзаказе по 'subOrderNumber'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1293")
    public void shouldGetSubOrderInfoBySubOrderNumber() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getSubOrderInfoBySubOrderNumber();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(41)
    @Release(8)
    @Suite("Подзаказ")
    @Story("sub-orders-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на удаление подзаказа посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление подзаказа.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1293")
    public void shouldDeleteSubOrderById() {
        //RetryFailedTests.runTestWithRetry(() -> {
            step.deleteSubOrderById();
        //}, 2);
    }

    @After()
    @Description("Удаление тестовых данных после выполнения каждого теста.")
    public void teardown() throws SQLException {
        step.deleteTestDataFromDatabase();
    }
}