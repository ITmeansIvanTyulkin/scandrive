package devicecontrollertest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import devicecontroller.DeviceController;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 09.07.2025;
 * <p>
 * @Suite: 'Printing controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1170
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на поиск списка параметров принтера посредством 'API'. Осуществляется исключительно для 'GODEX'.
 * <p>
 * 2. Тест на поиск списка параметров всех существующих принтеров с параметризацией посредством 'API'.
 * <p>
 * 3. Тест на поиск списка статусов принтера посредством 'API'.
 * <p>
 * 4. Тест на поиск списка всех типов принтеров посредством 'API'.
 * <p>
 * 5. Тест на получении информации о текущем времени сервера посредством 'API'.
 * </p>
 */

public class DeviceControllerTest {
    private DeviceController step;

    @Before()
    public void setup() {
        step = new DeviceController();
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("device-controller")
    @Feature("core-printer")
    @DisplayName("Тест на поиск списка параметров принтера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить поиск списка параметров принтера. Осуществляется " +
            "исключительно для 'GODEX'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1170")
    public void shouldSearchForPrinterParameters() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForPrinterParameters("GODEX");
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
    @Story("device-controller")
    @Feature("core-printer")
    @DisplayName("Тест на поиск списка параметров всех существующих принтеров с параметризацией посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить поиск списка параметров принтера. Осуществляется " +
            "для вообще всех принтеров через параметризацию.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1170")
    public void shouldSearchForPrinterParametersParameterised() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForPrinterParametersParameterised();
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
    @Story("device-controller")
    @Feature("core-printer")
    @DisplayName("Тест на поиск списка статусов принтера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить поиск списка статусов принтера.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1170")
    public void shouldSearchAndCheckPrinterStatus() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchAndCheckPrinterStatus();
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
    @Story("device-controller")
    @Feature("core-printer")
    @DisplayName("Тест на поиск списка всех типов принтеров посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить поиск списка всех типов принтеров.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1170")
    public void shouldSearchForAllPrintersTypes() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForAllPrintersTypes();
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
    @Story("device-controller")
    @Feature("core-printer")
    @DisplayName("Тест на получении информации о текущем времени сервера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о текущем времени сервера.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1170")
    public void shouldGetInfoOfCurrentServerTime() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getInfoOfCurrentServerTime();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }
}