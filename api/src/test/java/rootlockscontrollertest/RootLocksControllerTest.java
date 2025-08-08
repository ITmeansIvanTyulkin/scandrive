package rootlockscontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;
import rootlockscontroller.RootLocksController;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 21.05.2025;
 * <p>
 * <p>
 * Сьют 'Root Locks Controller' по таску: https://algaid.atlassian.net/browse/COREDEV-814
 * </p>
 * <p>
 * В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 * ТЕСТЫ:
 * </p>
 * 1. Тест на успешную постановку блокировки для маршрута посредством 'API', проведение надлежащих проверок.
 * <p>
 * 2. Тест на успешное получение списка установленных блокировок на маршруты посредством 'API'.
 * <p>
 * 3. Тест на успешное обновление типа и причины блокировки на маршрут посредством 'API'.
 * <p>
 * 4. Кейс на удаление блокировки маршрута не явный, а используется в блоке @After.
 */

public class RootLocksControllerTest {
    private RootLocksController step;

    @Before
    public void setup() {
        step = new RootLocksController();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-814")
    @DisplayName("Тест на успешную постановку блокировки для маршрута посредством 'API', проведение надлежащих проверок.")
    @Description("Проверка, что можно успешно поставить блокировку на маршрут посредством 'API', произвести " +
            "надлежащие проверки.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-814")
    public void shouldBlockCheckUnblockAndDeleteRoute() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.blockingRoute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-814")
    @DisplayName("Тест на успешное получение списка установленных блокировок на маршруты посредством 'API'.")
    @Description("Проверка, что можно успешно получить список всех установленных блокировок на имеющиеся маршруты " +
            "посредством 'API-запроса'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-814")
    public void shouldGetAllRouteBlocks() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllRouteBlocksList(1, 50);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-814")
    @DisplayName("Тест на успешное обновление типа и причины блокировки на маршрут посредством 'API'.")
    @Description("Проверка, что можно успешно обновить тип и причину блокирвки на имеющийся маршрут посредством 'API-запроса'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-814")
    public void shouldUpdateRoute() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToUpdateRoute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    @Description("Удаляю тестовые данные.")
    public void teardown() throws Exception {
        // Произвожу разблокировку тестового маршрута.
        step.tryToUnblockRoute();
        // Удаляю тестовые данные (разблокированный маршрут, а затем все точки).
        step.deleteTestData();
    }
}