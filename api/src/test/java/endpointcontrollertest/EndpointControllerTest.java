package endpointcontrollertest;

import data.constants.Constants;
import data.waitings.Waitings;
import endpointcontroller.EndpointController;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 31.03.2025;
 * <p>
 * <p>
 *     Сьют 'Endpoint Controller' по таску: https://algaid.atlassian.net/browse/COREDEV-794
 * </p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на успешное получение всех точек контроллеров посредством 'API'.
 * <p>
 * 2. Тест на успешное получение всех типов существующих типов соединений посредством 'API'.
 * <p>
 * 3. Тест на успешное создание любого типа точки посредством 'API', затем её удаление.
 * <p>
 * 4. Тест на успешное получение информации о точке посредством 'API' по её 'id'.
 * <p>
 * 5. Тест на успешное обновление информации о точке посредством 'API'.
 */
public class EndpointControllerTest {
    private static EndpointController step;

    @Before
    public void setup() {
        step = new EndpointController();
        Waitings.awaitSeconds(Constants.EXPECTATION_5);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-794")
    @DisplayName("Тест на успешное получение всех точек контроллеров посредством 'API'.")
    @Description("Проверка, что можно успешно получить список всех точек контроллеров посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-794")
    public void shouldGetAllEndpointControllers() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.contextSearch(1, 10);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-794")
    @DisplayName("Тест на успешное получение всех типов существующих типов соединений посредством 'API'.")
    @Description("Проверка, что можно успешно получить массив всех существующих типов соединений посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-794")
    public void shouldGetAllConnectionTypes() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getTypes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-794")
    @DisplayName("Тест на успешное создание любого типа точки посредством 'API', затем её удаление.")
    @Description("Проверка, что можно успешно создать любой тип точки посредством 'API' и затем удалить тестовую точку.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-794")
    public void shouldAddAnyRandomType() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addNewPoint();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-794")
    @DisplayName("Тест на успешное получение информации о точке посредством 'API' по её 'id'.")
    @Description("Проверка, что можно успешно получить информацию о точке посредством 'API' по её 'id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-794")
    public void shouldGetPointInfoById() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.checkPointById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-794")
    @DisplayName("Тест на успешное обновление информации о точке посредством 'API'.")
    @Description("Проверка, что можно успешно обновить информацию о точке посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-794")
    public void shouldUpdatePointInfo() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updatePointInfo();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }
}