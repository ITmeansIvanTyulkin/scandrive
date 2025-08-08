package printercontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import printercontroller.PrinterController;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 18.06.2025;
 * <p>
 * @Suite: 'Orders controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1087
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на успешное добавление принтера агрегации посредством 'API'.
 * <p>
 * 2. Тест на успешный контекстный поиск по принтерам агрегации посредством 'API' + сравниваю с БД.
 * <p>
 * 3. Тест на успешное получение информации о принтере по его 'id' посредством 'API'.
 * <p>
 * 4. Тест на успешное обновление информации о принтере по его 'id' посредством 'API'.
 * <p>
 * 5. Тест на успешное удаление информации о принтере по его 'id' посредством 'API'.
 * <p>
 */

public class PrinterControllerTest {
    private PrinterController step;

    @Before
    public void setup() throws SQLException {
        step = new PrinterController();
        PrinterController.deleteTestData();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное добавление принтера агрегации посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление принтера агрегации посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1087")
    public void shouldAddAggregationPrinters() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addAggregationPrinter();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешный контекстный поиск по принтерам агрегации посредством 'API' + сравниваю с БД.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск принтеров агрегации посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1087")
    public void shouldSearchAggregationPrinters() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForPrintersViaApiAndCompareDataWithDatabase();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }


    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное получение информации о принтере по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о принтере по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1087")
    public void shouldGetAggregationPrintersInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getPrinterInfoById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное обновление информации о принтере по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление информации о принтере по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1087")
    public void shouldUpdateAggregationPrintersInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateAggregationPrinter();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1078")
    @DisplayName("Тест на успешное удаление информации о принтере по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление информации о принтере по его 'id' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1087")
    public void shouldDeleteAggregationPrintersInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteAggregationPrinter();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void tearDown() throws SQLException {
        PrinterController.deleteTestData();
    }

    @Description("Удаление тестовых данных из базы данных.")
    @AfterClass
    public static void teardown() throws SQLException {
        PrinterController
                .deleteTestData();
    }
}