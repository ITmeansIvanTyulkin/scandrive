package scannercontrollertest;

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
import scannercontroller.ScannerController;

import java.sql.SQLException;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 30.06.2025;
 * <p>
 * @Suite: 'Scanner controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1115
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests: </p>
 * 1. Тест на добавления сканера посредством 'API'.
 * <p>
 * 2. Тест на контекстный поиск сканеров посредством 'API'.
 * <p>
 * 3. Тест на получение информации о сканере по 'id' посредством 'API'.
 * <p>
 * 4. Тест на обновление информации о сканере по 'id' посредством 'API'.
 * <p>
 * 5. Тест на удаление информации о сканере по 'id' посредством 'API'.
 * <p>
 * <p>
 * @FYI: В сьюте в блоке @After реализовано удаление тестовых данных из базы данных: сначала очищаются данные по рабочим станциям и затем по существующим сканерам.
 * </p>
 */

public class ScannerControllerTest {
    private ScannerController step;

    @Description("Инициализация экземпляра класса в шаг.")
    @Before
    public void setup() {
        step = new ScannerController();
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Сканер агрегации")
    @Story("scanner-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на добавления сканера посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить добавление сканера посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1115")
    public void shouldAddScanner() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addScanner();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Сканер агрегации")
    @Story("scanner-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на контекстный поиск сканеров посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить контекстный поиск сканеров посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1115")
    public void shouldSearchForScanners() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.searchForScanners(1, 50);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Сканер агрегации")
    @Story("scanner-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на получение информации о сканере по 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить получение информации о сканере по 'id' посредством 'API'.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1115")
    public void shouldGetScannerInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getScannerInfoById();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Сканер агрегации")
    @Story("scanner-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на обновление информации о сканере по 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить обновление информации о сканере по 'id' посредством 'API'.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1115")
    public void shouldUpdateScannerInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.updateScanner();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Sprint(38)
    @Release(5)
    @Suite("Сканер агрегации")
    @Story("scanner-controller")
    @Feature("Core-aggregator")
    @DisplayName("Тест на удаление информации о сканере по 'id' посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить удаление информации о сканере по 'id' посредством 'API'.")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1115")
    public void shouldDeleteScannerInfoById() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteScanner();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Description("Удаление тестовых данных из базы данных.")
    @After
    public void teardown() throws SQLException {
        step.deleteTestPrinterInDatabase();
    }
}