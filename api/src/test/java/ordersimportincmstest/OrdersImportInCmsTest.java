package ordersimportincmstest;

import data.annotations.Release;
import data.annotations.Sprint;
import data.annotations.Suite;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import ordersimportincms.OrdersImportInCms;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

/**
 * @Author: Ivan Tyulkin, QA Java Engineer;
 * @Date: 23.07.2025;
 * <p>
 * @Suite: 'Orders import in CMS' по таску: https://algaid.atlassian.net/browse/COREDEV-1248
 * </p>
 * <p>
 * @Features: В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * @Tests:
 * </p>
 * 1. Тест на импортирование заказов в CMS посредством 'API'.
 * </p>
 */

public class OrdersImportInCmsTest {
    private OrdersImportInCms step;

    @Before()
    @Description("Инициализация.")
    public void setup() {
        step = new OrdersImportInCms();
    }

    @Test
    @Tag("API")
    @Sprint(39)
    @Release(7)
    @Suite("импортировать заказы в 'cms'")
    @Story("Aggregation")
    @Feature("Функционал аггригации")
    @DisplayName("Тест на импортирование заказов в CMS посредством 'API'.")
    @Description("Проверка, что можно успешно осуществить импортирование заказов в CMS.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1248")
    public void shouldImportOrdersInCms() {
        RetryFailedTests.runTestWithRetry(() -> {
            step.importToCms();
        }, 2);
    }

    @After()
    public void teardown() throws Exception {
        step.deleteTestData();
    }
}