package printerlockscontrollertest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import printerlockscontroller.PrinterLocksController;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 16.07.2025;
 * <p>
 * @Suite: 'Printer locks controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1208
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на получение списка установленных блокировок посредством 'API'.
 * <p>
 * 2. Тест на добавление блокировки для принтера посредством 'API'.
 * <p>
 * 3. Тест на обновление типа и причины блокировки для принтера посредством 'API'.
 * <p>
 * 4. Тест на удаление блокировки принтера посредством 'API'.
 * </p>
 */

public class PrinterLocksControllerTest {
    private PrinterLocksController step;

    @Before()
    public void setup() {
        step = new PrinterLocksController();
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("printer-locks-controller")
    @Feature("core-printer")
    @DisplayName("Тест на получение списка установленных блокировок посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение списка установленных блокировок.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1208")
    public void shouldGetSetBlocks() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getBlocksSet(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("printer-locks-controller")
    @Feature("core-printer")
    @DisplayName("Тест на добавление блокировки для принтера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление блокировки для принтера.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1208")
    public void shouldMakePrinterBlocked() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addingBlockForPrinter();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("printer-locks-controller")
    @Feature("core-printer")
    @DisplayName("Тест на обновление типа и причины блокировки для принтера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление типа и причины блокировки для принтера.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1208")
    public void shouldUpdateTypeAndReasonPrinterBlocked() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateTypeAndReasonPrinterBlocked();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("printer-locks-controller")
    @Feature("core-printer")
    @DisplayName("Тест на удаление блокировки принтера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление блокировки принтера.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1208")
    public void shouldDeletePrinterBlocked() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deletePrinterBlocked();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After()
    @Description("Удаление тестовых данных и полная очистка использованных таблиц.")
    public void teardown() throws SQLException {
        step.deleteTestData();
    }
}