package printtemplatecontrollertest;

import data.annotations.*;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import printtemplatecontroller.PrintTemplateController;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 24.06.2025;
 * <p>
 * @Suite: 'Print template controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1106
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на добавления шаблона печати посредством 'API'.
 * <p>
 * 2. Тест на контекстный поиск шаблонов печати посредством 'API'.
 * <p>
 * 3. Тест на успешное получение информации о шаблоне печати по его 'id' посредством 'API'.
 * <p>
 * 4. Тест на успешное обновление шаблона печати по его 'id' посредством 'API'.
 * <p>
 * 5. Тест на успешное удаление шаблона печати по его 'id' посредством 'API'.
 * <p>
 * 6. Тест на успешное получение карты замены значений для печати на основании описаний посредством 'API'.
 * <p>
 * 7. Тест на успешное добавление или обновление шаблона, даты производства и партии у выбранных сущностей посредством 'API'.
 */

public class PrintTemplateControllerTest {
    private PrintTemplateController step;

    @Before
    public void setup() {
        step = new PrintTemplateController();
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Шаблон печати")
    @Story("print-template-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на добавления шаблона печати посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление шаблона печати посредством 'API'.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1106")
    public void shouldAddPrintTemplate() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addPrintTemplate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Шаблон печати")
    @Story("print-template-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на контекстный поиск шаблонов печати посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск существующих шаблонов печати посредством 'API'.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1106")
    public void shouldSearchPrintTemplate() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForPrintTemplates(
                        1,
                        50,
                        true,
                        true,
                        true
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Шаблон печати")
    @Story("print-template-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное получение информации о шаблоне печати по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о шаблоне печати по его 'id'.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1106")
    public void shouldGetPrintTemplateInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getPrintTemplateInfoById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Шаблон печати")
    @Story("print-template-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное обновление шаблона печати по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление информации о шаблоне печати по его 'id'.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1106")
    public void shouldUpdatePrintTemplateInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updatePrintTemplateInfoById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Шаблон печати")
    @Story("print-template-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное удаление шаблона печати по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление информации о шаблоне печати по его 'id'.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1106")
    public void shouldDeletePrintTemplateInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deletePrintTemplateById();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Шаблон печати")
    @Story("print-template-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное получение карты замены значений для печати на основании описаний посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение карты замены значений для печати на основании описаний.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1106")
    public void shouldGetValuesMapForPrinting() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getValuesMapForPrinting();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Шаблон печати")
    @Story("print-template-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на успешное добавление или обновление шаблона, даты производства и партии у выбранных сущностей посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление или обновление шаблона, даты производства и партии у выбранных сущностей.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1106")
    public void shouldAddOrUpdatePrintTemplate() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addOrUpdatePrintTemplateEntities();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void teardown() throws SQLException {
        step.deleteTestData();
    }
}