package aggregationtest;

import aggregation.AggregationGtinController;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;


/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 05.06.2025;
 * <p>
 * <p>
 *     Сьют по 'Aggregation GTIN controller' по таску: https://algaid.atlassian.net/browse/COREDEV-1029
 * </p>
 * <p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на добавление аггрегационного 'GTIN' посредством 'API'.
 * <p>
 * 2. Тест на получение списка всех аггрегационых 'GTIN' посредством 'API'.
 * <p>
 * 3. Тест на удаление аггрегационного 'GTIN' посредством 'API'.
 * <p>
 * </p>
 */

public class AggregationGtinControllerTest {
    private AggregationGtinController step;

    @Description("Создаю предварительные условия для тестовой среды.")
    @Before
    public void setup() {
        step = new AggregationGtinController();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1029")
    @DisplayName("Тест на добавление аггрегационного 'GTIN' посредством 'API'.")
    @Description("Проверка, что можно успешно добавить аггрегационный 'GTIN' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1029")
    public void shouldAddAggregationGtin() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addAggregationGtin();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1029")
    @DisplayName("Тест на получение списка всех аггрегационых 'GTIN' посредством 'API'.")
    @Description("Проверка, что можно успешно получить список всех аггрегационных 'GTIN' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1029")
    public void shouldGetListOfAggregationGtin() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.getAggregationGtinList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-1029")
    @DisplayName("Тест на удаление аггрегационного 'GTIN' посредством 'API'.")
    @Description("Проверка, что можно успешно удалить аггрегационный 'GTIN' посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-1029")
    public void shouldDeleteAggregationGtin() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.deleteAggregationGtin();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }
}