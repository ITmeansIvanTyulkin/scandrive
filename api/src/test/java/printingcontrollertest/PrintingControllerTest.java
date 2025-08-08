package printingcontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import printingcontroller.PrintingController;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 23.06.2025;
 * <p>
 * @Suite: 'Printing controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1093
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на успешную прямую печать посредством 'API'.
 * <p>
 * 2. Тест на успешную печать по шаблону посредством 'API'.
 * <p>
 * 3. Тест на успешное агрегирование по коду 'Data Matrix' посредством 'API'.
 * <p>
 */

public class PrintingControllerTest {
    private PrintingController step;

    @Before
    public void setup() throws SQLException {
        step = new PrintingController();
        step.deleteTestPrinterInDatabase();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1093")
    @DisplayName("Тест на успешную прямую печать посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить прямую печать посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1093")
    public void shouldDoDirectPrinting() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.directPrintingTest();
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1093")
    @DisplayName("Тест на успешную печать по шаблону посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить печать по шаблону посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1093")
    public void shouldDoPrintingByTemplate() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.templatePrinting();
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1093")
    @DisplayName("Тест на успешное агрегирование по коду 'Data Matrix' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить агрегирование по коду 'Data Matrix' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1093")
    public void shouldAggregateViaCode() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.aggregateViaDataMatrixCode();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void teardown() throws SQLException {
        step.deleteTestPrinterInDatabase();
    }
}