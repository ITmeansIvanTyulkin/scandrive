package routecontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;
import routecontroller.RouteController;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 26.05.2025;
 * <p>
 * <p>
 *     Сьют по 'Route Controller' по таску: https://algaid.atlassian.net/browse/COREDEV-815
 * </p>
 * <p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на получение всех типов маршрута посредством 'API'.
 * <p>
 * 2. Тест на получение всех активных маршрутов посредством 'API'.
 * <p>
 * 3. Тест на получение типов входящих точек посредством 'API'.
 * <p>
 * 4. Тест на получение типов исходящих точек посредством 'API'.
 * <p>
 * 5. Тест на успешный перезапуск маршрута посредством 'API'.
 * <p>
 * 6. Тест на успешный сброс счётчика для существующего маршрута посредством 'API'.
 * <p>
 * 7. Тест на успешный сброс счётчиков для существующих маршрутов из списка посредством 'API'.
 * </p>
 */

public class RouteControllerTest {
    private RouteController step;

    @Before
    public void setup() {
        step = new RouteController();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-815")
    @DisplayName("Тест на получение всех типов маршрута посредством 'API'.")
    @Description("Проверка, что можно успешно получить все типы маршрутов посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-815")
    public void shouldGetAllRouteTypes() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllRouteTypes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-815")
    @DisplayName("Тест на получение всех активных маршрутов посредством 'API'.")
    @Description("Проверка, что можно успешно получить все активные маршруты посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-815")
    public void shouldGetAllActiveRoutes() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllActiveRoutes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-815")
    @DisplayName("Тест на получение типов входящих точек посредством 'API'.")
    @Description("Проверка, что можно успешно получить типы входящих точек посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-815")
    public void shouldGetIncomingPoints() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getIncomingPoints();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-815")
    @DisplayName("Тест на получение типов исходящих точек посредством 'API'.")
    @Description("Проверка, что можно успешно получить типы исходящих точек посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-815")
    public void shouldGetOutcomingPoints() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.getOutcomingPoints();
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-815")
    @DisplayName("Тест на успешный перезапуск маршрута посредством 'API'.")
    @Description("Проверка, что можно успешно перезапустить маршрут посредством 'API'. " +
            "Использовать метод только как аварийный. При передаче данных по маршруту во время перезапуска возможны " +
            "потери данных.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-815")
    public void shouldRestartRoute() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.restartRoute();
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-815")
    @DisplayName("Тест на успешный сброс счётчика для существующего маршрута посредством 'API'.")
    @Description("Проверка, что можно успешно сбросить счётчик для существующего маршрута посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-815")
    public void shouldResetRouteCount() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.resetCount();
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-815")
    @DisplayName("Тест на успешный сброс счётчиков для существующих маршрутов из списка посредством 'API'.")
    @Description("Проверка, что можно успешно сбросить счётчики для существующих маршрутов из списка посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-815")
    public void shouldResetRoutesCounts() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.resetCounts();
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void teardown() throws Exception {
        step.deleteTestData();
    }
}