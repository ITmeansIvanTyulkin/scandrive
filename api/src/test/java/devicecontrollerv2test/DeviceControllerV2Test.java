package devicecontrollerv2test;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import devicecontrollerv2.DeviceControllerV2;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 11.07.2025;
 * <p>
 * @Suite: 'Printing controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1172
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на нанесение: добавление принтера посредством 'API'.
 * <p>
 * 2. Тест на успешное получение списка принтеров посредством 'API'.
 * <p>
 * 3. Тест на получение устройства по 'device_id'.
 * <p>
 * 4. Тест на удаление устройства по 'id'.
 * <p>
 * 5. Тест на обновление устройства по 'id'.
 * </p>
 */

public class DeviceControllerV2Test {
    private DeviceControllerV2 step;

    @Before()
    public void setup() {
        step = new DeviceControllerV2();
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(4)
    @Suite("")
    @Story("device-controller-V-2")
    @Feature("core-printer")
    @DisplayName("Тест на нанесение: добавление принтера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить нанесение: добавление принтера.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1172")
    public void shouldAddPrinter() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                DeviceControllerV2.addingPrinter();
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
    @Story("device-controller-V-2")
    @Feature("core-printer")
    @DisplayName("Тест на успешное получение списка принтеров посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение списка принтеров.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1172")
    public void shouldGetPrintersList() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getPrintersList(1, 50);
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
    @Story("device-controller-V-2")
    @Feature("core-printer")
    @DisplayName("Тест на получение устройства по 'device_id'.")
    @Description("Проверка, что можно успешно осуществить получение устройства по 'device_id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1172")
    public void shouldGetDeviceById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getDeviceById();
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
    @Story("device-controller-V-2")
    @Feature("core-printer")
    @DisplayName("Тест на удаление устройства по 'id'.")
    @Description("Проверка, что можно успешно осуществить удаление устройства по 'id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1172")
    public void shouldDeleteDeviceById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteDeviceById();
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
    @Story("device-controller-V-2")
    @Feature("core-printer")
    @DisplayName("Тест на обновление устройства по 'id'.")
    @Description("Проверка, что можно успешно осуществить обновление устройства по 'id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1172")
    public void shouldUpdateDeviceById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateDevice();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After()
    @Description("Произвожу удаление всех тестовых данных.")
    public void teardown() throws SQLException {
        //step.deleteTestData();
    }
}