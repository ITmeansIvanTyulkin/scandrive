package routecontrollerv2test;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.*;
import retryfailedtests.RetryFailedTests;
import routecontrollerv2.RouteControllerV2;
import serialisation.Serialisation;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 02.06.2025;
 * <p>
 * <p>
 *     Сьют по 'Route Controller V2' по таску: https://algaid.atlassian.net/browse/COREDEV-816
 * </p>
 * <p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на добавление маршрута посредством 'API'.
 * <p>
 * 2. Тест на получение маршрута посредством 'API'.
 * <p>
 * 3. Тест на получение информации о маршруте посредством 'API' по его 'id'.
 * <p>
 * 4. Тест на обновление информации о маршруте посредством 'API' по его 'id'.
 * </p>
 */

public class RouteControllerV2Test {
    private static RouteControllerV2 step;

    @Description("Проверяю, есть ли старые тестовые данные и удаляю их перед запуском тестов.")
    @BeforeClass
    public static void cleanTestsDataBeforeClass() throws SQLException {
        Serialisation.cleaningDatabaseBeforeTests();
    }

    @Before
    public void setup() throws Exception {
        step = new RouteControllerV2();
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-816")
    @DisplayName("Тест на добавление маршрута посредством 'API'.")
    @Description("Проверка, что можно успешно добавить маршрут посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-816")
    public void shouldAddRoute() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                Serialisation.cleaningDatabaseBeforeTests();
                step.makeRoute();
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-816")
    @DisplayName("Тест на получение маршрута посредством 'API'.")
    @Description("Проверка, что можно успешно получить маршрут посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-816")
    public void shouldGetRoute() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getRoute(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-816")
    @DisplayName("Тест на получение информации о маршруте посредством 'API' по его 'id'.")
    @Description("Проверка, что можно успешно получить информацию о маршруте по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-816")
    public void shouldGetRouteById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToGetRouteById();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-816")
    @DisplayName("Тест на обновление информации о маршруте посредством 'API' по его 'id'.")
    @Description("Проверка, что можно успешно обновить информацию о маршруте по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-816")
    public void shouldUpdateRouteById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToUpdateRouteById();
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