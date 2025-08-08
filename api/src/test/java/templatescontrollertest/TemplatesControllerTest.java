package templatescontrollertest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;
import templatescontroller.TemplatesController;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 21.07.2025;
 * <p>
 * @Suite: 'Templates controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1235
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на получение списка шаблонов посредством 'API'.
 * <p>
 * 2. Тест на добавление шаблона посредством 'API'.
 * <p>
 * 3. Тест на получение шаблона по 'template_id' посредством 'API'.
 * <p>
 * 4. Тест на обновление шаблона посредством 'API'.
 * <p>
 * 5. Тест на удаление шаблона посредством 'API'.
 * </p>
 */

public class TemplatesControllerTest {
    private TemplatesController step;

    @Before()
    @Description("Инициализация.")
    public void setup() {
        step = new TemplatesController();
    }

    @Test
    @Tag("API")
    @Sprint(40)
    @Release(4)
    @Suite("")
    @Story("Templates Controller")
    @Feature("core-printer")
    @DisplayName("Тест на получение списка шаблонов посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение списка шаблонов.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1235")
    public void shouldGetTemplatesList() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getTemplatesList(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(40)
    @Release(4)
    @Suite("")
    @Story("Templates Controller")
    @Feature("core-printer")
    @DisplayName("Тест на добавление шаблона посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление шаблона.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1235")
    public void shouldAddTemplate() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addingTemplate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(40)
    @Release(4)
    @Suite("")
    @Story("Templates Controller")
    @Feature("core-printer")
    @DisplayName("Тест на получение шаблона по 'template_id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение шаблона по 'template_id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1235")
    public void shouldGetTemplateById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getTemplateByItsId();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(40)
    @Release(4)
    @Suite("")
    @Story("Templates Controller")
    @Feature("core-printer")
    @DisplayName("Тест на обновление шаблона посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление шаблона.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1235")
    public void shouldUpdateTemplate() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateTemplate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(40)
    @Release(4)
    @Suite("")
    @Story("Templates Controller")
    @Feature("core-printer")
    @DisplayName("Тест на удаление шаблона посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление шаблона.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1235")
    public void shouldDeleteTemplate() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteTemplate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After()
    @Description("Метод для удаления всех созданных в процессе регресса тестовых данных из базы данных.")
    public void teardown() throws SQLException {
        step.cleaningDatabase();
    }
}