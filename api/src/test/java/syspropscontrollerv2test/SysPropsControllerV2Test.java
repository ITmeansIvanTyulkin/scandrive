package syspropscontrollerv2test;

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
import syspropscontrollerv2.SysPropsControllerV2;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 01.07.2025;
 * <p>
 * @Suite: 'Scanner controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1124
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests: </p>
 * 1. Тест на получение списка системных реквизитов посредством 'API'.
 * <p>
 * 2. Тест на создание системного реквизита посредством 'API'.
 * <p>
 * 3. Тест на обновление системного реквизита по его 'id' посредством 'API'.
 * <p>
 * 4. Тест на удаление системного реквизита по его 'id' посредством 'API'.
 * <p>
 * 5. Тест на получение списка доступных параметров посредством 'API'.
 * <p>
 * <p>
 * @FYI: В сьюте в блоке @After реализовано удаление тестовых данных из базы данных: сначала очищаются данные по рабочим станциям и затем по существующим сканерам.
 * </p>
 */

public class SysPropsControllerV2Test {
    private SysPropsControllerV2 step;

    @Description("Инициализация экземпляра класса в шаг.")
    @Before
    public void setup() {
        step = new SysPropsControllerV2();
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(6)
    @Suite("Свойства агрегации")
    @Story("sys-props-controller-v-2")
    @Feature("Core-aggregator")
    @DisplayName("Тест на получение списка системных реквизитов посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение списка системных реквизитов.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1124")
    public void shouldGetSystemProperties() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getSystemPropertiesList(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(6)
    @Suite("Свойства агрегации")
    @Story("sys-props-controller-v-2")
    @Feature("Core-aggregator")
    @DisplayName("Тест на создание системного реквизита посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить создание системного реквизита.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1124")
    public void shouldCreateSystemProperties() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.createSysProps();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(6)
    @Suite("Свойства агрегации")
    @Story("sys-props-controller-v-2")
    @Feature("Core-aggregator")
    @DisplayName("Тест на обновление системного реквизита по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление системного реквизита по его 'id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1124")
    public void shouldUpdateSystemProperties() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateSysProps();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(6)
    @Suite("Свойства агрегации")
    @Story("sys-props-controller-v-2")
    @Feature("Core-aggregator")
    @DisplayName("Тест на удаление системного реквизита по его 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление системного реквизита по его 'id'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1124")
    public void shouldDeleteSystemProperties() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteSysProps();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(6)
    @Suite("Свойства агрегации")
    @Story("sys-props-controller-v-2")
    @Feature("Core-aggregator")
    @DisplayName("Тест на получение списка доступных параметров посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получения списка доступных параметров.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1124")
    public void shouldGetSystemPropertiesList() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getParametersList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Description("Удаление тестовых данных из базы данных.")
    @After
    public void teardown() throws SQLException {
        step.deleteTestData();
    }
}