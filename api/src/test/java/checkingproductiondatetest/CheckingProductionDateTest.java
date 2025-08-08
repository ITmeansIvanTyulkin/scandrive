package checkingproductiondatetest;

import checkingproductiondate.CheckingProductionDate;
import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 01.08.2025;
 * <p>
 * @Suite: 'Checking production date' по таску: https://algaid.atlassian.net/browse/COREDEV-1280
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на проверку даты производства посредством 'API'.
 * </p>
 */

public class CheckingProductionDateTest {
    private CheckingProductionDate step;

    @Before()
    @Description("Инициализация.")
    public void setup() {
        step = new CheckingProductionDate();
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(7)
    @Suite("проверка даты производства")
    @Story("Aggregation")
    @Feature("Функционал аггригации")
    @DisplayName("Тест на проверку даты производства посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить проверку даты производства.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1248")
    public void shouldCheckProductionDate() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.thread();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }
}