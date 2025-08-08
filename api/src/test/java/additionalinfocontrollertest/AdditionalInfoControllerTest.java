package additionalinfocontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.*;
import retryfailedtests.RetryFailedTests;
import serialisation.Serialisation;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 13.05.2025;
 * <p>
 * <p>
 *     Сьют по 'additional-info-controller' по таску: https://algaid.atlassian.net/browse/COREDEV-812
 * </p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на успешное добавление дополнительной информации в маршрут посредством 'API'.
 * <p>
 * 2. Тест на успешное удаление дополнительной информации из маршрута посредством 'API'.
 * <p>
 *
 */

public class AdditionalInfoControllerTest {
    private static Serialisation step;

    @BeforeClass
    public static void cleanTestsDataBeforeClass() throws SQLException {
        // Проверяю, есть ли старые тестовые данные и удаляю их перед запуском тестов.
        Serialisation.cleaningDatabaseBeforeTests();
    }

    @Before
    public void setup() throws Exception {
        step = new Serialisation();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-812")
    @DisplayName("Тест на успешное добавление дополнительной информации в маршрут посредством 'API'.")
    @Description("Проверка, что можно успешно добавить дополнительную информацию в маршрут посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-812")
    public void shouldAddNewInfoToRoute() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addNewInfoToExistingRoute();
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-812")
    @DisplayName("Тест на успешное удаление дополнительной информации из маршрута посредством 'API'.")
    @Description("Проверка, что можно успешно добавить дополнительную информацию в маршрут посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-812")
    public void shouldAddAndDeleteAdditionalInfoFromRoute() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteAdditionalInfoFromExistingRoute();
            } catch (SQLException | InterruptedException e) {
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