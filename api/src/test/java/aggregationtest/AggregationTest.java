package aggregationtest;

import aggregation.Aggregation;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 29.04.2025;
 * <p>
 * <p>
 *     Сьют по 'core-aggregator''Создание линий, правила фильтрации, правила агрегации.' по таску: https://algaid.atlassian.net/browse/COREDEV-879
 * </p>
 * <p>
 * <p>
 *     В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 *      ТЕСТЫ:
 * </p>
 * 1. Тест на успешное создание тестовой линии агрегации посредством 'API' (Создание линий, правила фильтрации, правила агрегации).
 * <p>
 * <p>
 * В блоке @After происходит очистка базы данных во всех связанных таблицах от тестовых данных.
 * </p>
 */

public class AggregationTest {
    private Aggregation step;

    @Before
    public void setup() {
        step = new Aggregation();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-879")
    @DisplayName("Тест на успешное создание тестовой линии агрегации посредством 'API'.")
    @Description("Проверка, что можно успешно создать новую тестовую линии агрегации посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-879")
    public void shouldAddNewTestAggregationLine() {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.addNewAggregationLine();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void teardown() throws SQLException {
        step.cleanTestDataInDatabase();
    }
}