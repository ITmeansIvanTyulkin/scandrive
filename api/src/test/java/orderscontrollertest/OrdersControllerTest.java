package orderscontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import orderscontroller.OrdersController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 17.06.2025;
 * <p>
 * @Suite: 'Orders controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1078
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на успешный контекстный поиск заказов контроллера посредством 'API'.
 * <p>
 * 2. Тест на успешное добавление заказа контроллера посредством 'API'.
 * <p>
 * 3. Тест на успешное получение информации о заказае контроллера посредством 'API'.
 * <p>
 * 4. Тест на успешное обновление заказа контроллера посредством 'API'.
 * <p>
 * 5. Тест на успешное удаление заказа контроллера по его 'id' посредством 'API'.
 * <p>
 */

public class OrdersControllerTest {
    private OrdersController step;

    @Before
    public void setup() throws SQLException {
        step = new OrdersController();
        OrdersController.deleteTestData();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешный контекстный поиск заказов контроллера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск заказов контроллера посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1078")
    public void shouldSearchControllerOrders() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForControllers(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное добавление заказа контроллера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление заказа контроллера посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1078")
    public void shouldAddControllerOrder() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addControllerOrder();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное получение информации о заказе контроллера по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о заказе контроллера по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1078")
    public void shouldGetControllerOrderInfo() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getOrderControllerInfo();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное обновление заказа контроллера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление заказа посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1078")
    public void shouldUpdateControllerOrder() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateOrderController();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное удаление заказа контроллера по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление заказа по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1078")
    public void shouldDeleteControllerOrder() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteOrderController();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void tearDown() throws SQLException {
        OrdersController.deleteTestData();
    }

    @Description("Удаление тестовых данных из базы данных.")
    @AfterClass
    public static void teardown() throws SQLException {
        OrdersController
                .deleteTestData();
    }
}