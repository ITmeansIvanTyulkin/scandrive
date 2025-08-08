package endpointcontrollertest;

import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import endpointcontroller.EndpointController;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 01.04.2025;
 * <p>
 * <p>
 * Сьют 'Endpoint Controller' по таску: https://algaid.atlassian.net/browse/COREDEV-794
 * </p>
 * <p>
 * В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 * ТЕСТЫ:
 * </p>
 * 1. Параметризированные тесты на типы существующих соединений посредством 'API', проверки.
 * </p>
 * <p>
 * <p>
 * Проверяемые соединения: 'TCP', 'RABBIT_MQ', 'FOLDER', 'DATAMATRIX_KEEPER', 'CONVEYOR'.
 */

@RunWith(Parameterized.class)
public class ParameterisedEndpointControllerTest {
    private EndpointController step;
    private String configuration;
    private Boolean expected;

    public ParameterisedEndpointControllerTest(String configuration, Boolean expected) {
        this.configuration = configuration;
        this.expected = expected;
    }

    @Description("Параметризация проверок на существующие типы.")
    @Parameterized.Parameters
    public static Collection<Object[]> data() throws SQLException {
        List<String> types = EndpointController.getAllTypesOfConnection();

        // Проверка существующих конфигураций.
        List<Object[]> testData = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            testData.add(new Object[]{types.get(i), true});
        }
        return testData;
    }

    @Before
    public void setup() {
        step = new EndpointController();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-794")
    @DisplayName("Тест на успешное получение всех типов существующих типов соединений посредством 'API'.")
    @Description("Проверка, что можно успешно получить массив всех существующих типов соединений посредством 'API' и их поштучная проверка на работоспособность.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-794")
    public void shouldCheckAllParameters() {
        RetryFailedTests.runTestWithRetry(() -> {
            String response = null;
            try {
                // Проверка на статус-код '200' внутри метода.
                response = step.getParameters(
                        configuration,
                        Swagger.GET_ALL_PARAMETERS
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Проверки.
            // Проверяю наличие тела 'JSON'.
            if (response == null || response.isEmpty()) {
                throw new RuntimeException(Constants.RED + "Ответ пустой." + Constants.RESET);
            }
        }, 2);
    }
}