package gtinexpirationtest;

import gtinexpiration.GtinExpiration;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 09.06.2025;
 * <p>
 * <p>
 *     Сьют по 'Best before date controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1065
 * </p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на успешное добавление или изменение даты срока годности посредством 'API'.
 * <p>
 * 2. Тест на успешное получение всех данных о датах срока годности посредством 'API'.
 * <p>
 * 3. Тест на успешное получение списка дата срока годности по 'GTIN' посредством 'API'.
 * </p>
 * <p>
 * <p>
 * P.S. Удаление 'GTIN' реализовано отдельно в @AfterClass, чтобы заодно очищать базу данных от тестовых данных.
 */

public class GtinExpirationTest {
    private GtinExpiration step;

    @Description("Инициализация степа.")
    @Before
    public void setup() {
        step = new GtinExpiration();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1065")
    @DisplayName("Тест на успешное добавление или изменение даты срока годности посредством 'API'.")
    @Description("Проверка, что можно успешно добавить или изменить дату срока годности посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1065")
    public void shouldAddOrUpdateGtinExpiration() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.editOrUpdateExpirationGtin();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1065")
    @DisplayName("Тест на успешное получение всех данных о датах срока годности посредством 'API'.")
    @Description("Проверка, что можно успешно получить все данные о датах срока годности посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1065")
    public void shouldReceiveAllGtinExpirationsDates() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllGtinExpirationDateInfo();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1065")
    @DisplayName("Тест на успешное получение списка дата срока годности по 'GTIN' посредством 'API'.")
    @Description("Проверка, что можно успешно получить список дат срока годности по 'GTIN' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1065")
    public void shouldReceiveDataListGtinExpirations() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllGtinDataList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Description("Очистка базы данных от всех тестовых данных, созданных в процессе регрессионного тестирования.")
    @AfterClass
    public static void teardown() throws SQLException {
        GtinExpiration.deleteTestData();
    }
}