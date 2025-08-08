package datamatrixcontrollertest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import datamatrixcontroller.DatamatrixController;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 07.07.2025;
 * <p>
 * @Suite: 'Datamatrix controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1147
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на получение всех сроков годности посредством 'API'.
 * <p>
 * 2. Тест на удаление всех просроченных кодов посредством 'API'.
 * <p>
 * 3. Тест на удаление просроченных кодов, созданные в диапазоне дат, посредством 'API'.
 * <p>
 * 4. Тест на удаление кодов для поискового индекса посредством 'API'.
 * <p>
 * 5. Тест на удаление строк доступным количеством кодов равным нулю из таблицы статистики.
 * <p>
 * 6. Тест на удаление отпечатанных кодов, созданные в диапазоне дат
 * <p>
 * 7. Тест на удаление всех отпечатанных кодов.
 * <p>
 * 8. Тест на статус удаления задания.
 * <p>
 * 9. Тест на счётчик доступных для печати кодов по индексу.
 * <p>
 * 10. Тест на получение кода из пула для печати.
 * <p>
 * 11. Тест на обновление времени окончания сроков годности.
 * <p>
 * 12. Тест на загрузку кодов.
 * </p>
 */

public class DatamatrixControllerTest {
    private DatamatrixController step;

    @Before()
    public void setup() {
        step = new DatamatrixController();
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на получение всех сроков годности посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение всех сроков годности.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldGetAllShelfLife() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllExistingShelfLife();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на удаление всех просроченных кодов посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление всех просроченных кодов.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldDeleteAllExpiredCodes() {
        RetryFailedTests.runTestWithRetry(() -> step.deleteAllExpiredCodes(), 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на удаление просроченных кодов, созданные в диапазоне дат, посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление просроченных кодов, созданные в диапазоне дат.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldDeleteExpiredCodesDuringDates() {
        RetryFailedTests.runTestWithRetry(() -> step.deleteExpiredCodes(), 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на удаление кодов для поискового индекса посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление кодов для поискового индекса.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldDeleteCodesForSearchingIndex() {
        RetryFailedTests.runTestWithRetry(() -> step.deleteCodesForSearchingIndex(), 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на удаление строк доступным количеством кодов равным нулю из таблицы статистики.")
    @Description("Проверка, что можно успешно осуществить удаление строк доступным количеством кодов равным нулю из таблицы статистики.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldDeleteStrings() {
        RetryFailedTests.runTestWithRetry(() -> step.deleteStrings(), 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на удаление отпечатанных кодов, созданные в диапазоне дат.")
    @Description("Проверка, что можно успешно осуществить удаление отпечатанных кодов, созданные в диапазоне дат.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldDeletePrintedCodes() {
        RetryFailedTests.runTestWithRetry(() -> step.deletingPrintedCodes(), 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на удаление всех отпечатанных кодов.")
    @Description("Проверка, что можно успешно осуществить удаление все отпечатанные коды.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldDeleteAllPrintedCodes() {
        RetryFailedTests.runTestWithRetry(() -> step.deletingAllPrintedCodes(), 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на статус удаления задания.")
    @Description("Проверка, что можно успешно осуществить проверку на удаление задания.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldCheckStatusTaskIsDeleted() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.checkStatusTaskIsDeleted();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на счётчик доступных для печати кодов по индексу.")
    @Description("Проверка, что можно успешно осуществить проверку доступных для печати кодов по индексу.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldCheckCodesForPrintingByIndex() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.checkCountOfAvailableCodesForPrinting();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на получение кода из пула для печати.")
    @Description("Проверка, что можно успешно осуществить получение кода из пула для печати.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldGetCodeFromPrintingPool() {
        RetryFailedTests.runTestWithRetry(() -> {
        try {
            step.getCodeFromPrintingPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на обновление времени окончания сроков годности.")
    @Description("Проверка, что можно успешно осуществить обновление времени окончания сроков годности.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldUpdateExpirationTime() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.updateCodesExpirationTime();
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(4)
    @Suite("Коды")
    @Story("datamatrix-controller")
    @Feature("datamatrix")
    @DisplayName("Тест на загрузку кодов.")
    @Description("Проверка, что можно успешно осуществить загрузку кодов.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1147")
    public void shouldLoadCodes() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.codesUpload();
        }, 2);
    }

    @Description("Удаляю тестовые данные из всех связанных таблиц.")
    @AfterClass()
    public static void teardown() throws SQLException {
        DatamatrixController.cleanTestData();
    }
}