package timezonecontrollertest;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;
import timezonecontroller.TimezoneController;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 15.05.2025;
 * <p>
 * <p>
 * Сьют по 'core-reader' 'Timezone-Controller' по таску: https://algaid.atlassian.net/browse/COREDEV-817
 * </p>
 * <p>
 * В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 * ТЕСТЫ:
 * </p>
 * 1. Тест на успешную проверку доступности всех часовых поясов посредством 'API'.
 * <p>
 * 2. Тест на успешную проверку получения текущего часового пояса посредством 'API'.
 * <p>
 * 3. Тест на успешное обновление часового пояса посредством 'API'.
 * <p>
 * 4. Тест на успешное использование системного часового пояса посредством 'API'.
 * </p>
 * <p>
 * <p>
 *     P.S. Удаления тестовых данных после регресса в данном случае не требуется.
 */

public class TimezoneControllerTest {
    private static TimezoneController step;

    @Before
    public void setup() {
        step = new TimezoneController();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-817")
    @DisplayName("Тест на успешную проверку доступности всех часовых поясов посредством 'API'.")
    @Description("Проверка, что можно успешно получить все часовые пояса посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-817")
    public void shouldGetAllTimezones() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAllTimezones();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-817")
    @DisplayName("Тест на успешную проверку получения текущего часового пояса посредством 'API'.")
    @Description("Проверка, что можно успешно получить текущий часовой пояс посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-817")
    public void shouldGetCurrentTimezone() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getCurrentTimezone();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-817")
    @DisplayName("Тест на успешное обновление часового пояса посредством 'API'.")
    @Description("Проверка, что можно успешно обновить текущий часовой пояс посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-817")
    public void shouldUpdateCurrentTimezone() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.timezoneRenewed();
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-817")
    @DisplayName("Тест на успешное использование системного часового пояса посредством 'API'.")
    @Description("Проверка, что можно успешно использовать системный часовой пояс посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-817")
    public void shouldUseSystemTimezone() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            step.useSystemTimezone();
        }, 2);
    }
}