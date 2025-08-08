package hookcontrollertest;

import hookcontroller.HookController;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import printingadaptforkeeper.PrintingAdaptForKeeper;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 29.05.2025;
 * <p>
 * <p>
 *     Сьют по 'Hook Controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1002
 * </p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на успешное создание и обновление хука посредством 'API'.
 * <p>
 * 2. Тест на успешный поиск всех хуков посредством 'API'.
 * <p>
 * 3. Тест на успешное удаление всех хуков посредством 'API'.
 * <p>
 *
 */

public class HookControllerTest {
    private HookController step;

    @Before
    public void setup() throws SQLException {
        step = new HookController();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1002")
    @DisplayName("Тест на успешное создание и обновление хука посредством 'API'.")
    @Description("Проверка, что можно успешно созать и обновить хук посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1002")
    public void shouldCreateAndUpdateHook() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.createAndUpdateHook();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1002")
    @DisplayName("Тест на успешный поиск всех хуков посредством 'API'.")
    @Description("Проверка, что можно успешно найти все существующие хуки посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1002")
    public void shouldFindAllHooks() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToFindAllExistingHooks();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1002")
    @DisplayName("Тест на успешное удаление всех хуков посредством 'API'.")
    @Description("Проверка, что можно успешно удалить все существующие хуки посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1002")
    public void shouldDeleteAllHooks() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToDeleteHooks();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

//    @After
//    public void teardown() throws SQLException {
//        databaseCleaning();
//    }
//
//    @Description("Вспомогательные методы: осуществление удаления тестовых данных из базы данных.")
//    @Tag("Database")
//    @Severity(SeverityLevel.CRITICAL)
//    private static void databaseCleaning() throws SQLException {
//        PrintingAdaptForKeeper
//                .cleanDatabaseTotally("datamatrix");
//        PrintingAdaptForKeeper
//                .cleanDatabaseTotally("params");
//        PrintingAdaptForKeeper
//                .cleanDatabaseTotally("device");
//        PrintingAdaptForKeeper
//                .cleanDatabaseTotally("sub_orders");
//    }
}