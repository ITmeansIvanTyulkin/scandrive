package printingadaptforkeepertest;

import databaseconnections.DbConnection;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.*;
import printingadaptforkeeper.PrintingAdaptForKeeper;
import retryfailedtests.RetryFailedTests;

import java.sql.SQLException;

/**
 * @Author Ivan Tyulkin, QA Java Engineer;
 * @Date 11.04.2025;
 * <p>
 * <p>
 * Сьют 'Device Controller' по таску: https://algaid.atlassian.net/browse/COREDEV-822
 * </p>
 * <p>
 * Одиночные не нагрузочные тесты для проверки каждой ручки.
 * В сьюте реализована 100% инкапсуляция каждого теста.
 * </p>
 * <p>
 * <p>
 * Что происходит в классе:
 * </p>
 * 1. Перед запуском класса осуществляется чистка базы данных - всех связанных с принтерами и печатью таблиц. А именно:
 * <p>
 * а). 'datamatrix';
 * b). 'params';
 * c). 'device';
 * d). 'orders'.
 * </p>
 * <p>
 * 2. Запуск тестового класса.
 * </p>
 * <p>
 * 3. Удаление тестовых данных после каждого теста.
 * </p>
 * <p>
 * <p>
 * ТЕСТЫ:
 * </p>
 * 1. Тест на проверку успешного создания (добавления) принтера посредством 'API'.
 * <p>
 * 2. Тест на проверку успешного добавления кодов посредством 'API'.
 * <p>
 * 3. Тест на проверку успешного обновления устройства посредством 'API'.
 * <p>
 * 4. Тест на проверку успешного получения кодов по атрибуту посредством 'API'.
 * <p>
 * 5. Тест на проверку успешного получения кодов из пула для печати посредством 'API'.
 * <p>
 * 6. Тест на проверку успешного получения одного кода из пула для печати посредством 'API'.
 */

public class PrintingAdaptForKeeperSingleHandlersTest {
    private static PrintingAdaptForKeeper step;

    @BeforeClass
    @Step("Зачистка базы данных от тестовых и иных данных перед тестами для чистого регресса.")
    public static void setup() throws SQLException {
        databaseCleaning();
    }

    @Before
    @Step("Создаю экземпляр класса в шаг.")
    public void setUp() {
        step = new PrintingAdaptForKeeper();
    }

    @Test
    @Tag("API")
    @Story("COREDEV-822")
    @DisplayName("Тест на проверку успешного создания (добавления) принтера посредством 'API'.")
    @Description("Проверка, что можно успешно создать (добавить) принтер посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-822")
    public void shouldAddPrinter() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToAddPrinters();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-822")
    @DisplayName("Тест на проверку успешного добавления кодов посредством 'API'.")
    @Description("Проверка, что можно успешно добавить коды на принтер посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-822")
    public void shouldAddCodesForPrinter() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToAddCodes(1000); // Значение количества добавленных кодов.
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-822")
    @DisplayName("Тест на проверку успешного обновления устройства посредством 'API'.")
    @Description("Проверка, что можно успешно обновить устройство посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-822")
    public void shouldUpdateDevice() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToAddCodes(1000); // Значение количества добавленных кодов.
                step.updateDevice("PAUSE"); // Статусы бывают: "PAUSE" и "RUNNING".
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-822")
    @DisplayName("Тест на проверку успешного получения кодов по атрибуту посредством 'API'.")
    @Description("Проверка, что можно успешно получить коды по атрибуту посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-822")
    public void shouldGetCodesByAttribute() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToAddCodes(1000); // Значение количества добавленных кодов.
                step.updateDevice("PAUSE"); // Статусы бывают: "PAUSE" и "RUNNING".
                step.tryToGetCodesByAttribute(10); // Значение вывода кодов (лимит) в ответе.
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-822")
    @DisplayName("Тест на проверку успешного получения кодов из пула для печати посредством 'API'.")
    @Description("Проверка, что можно успешно получить коды из пула для печати посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-822")
    public void shouldGetCodesFromPrintingPool() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToAddCodes(1000); // Значение количества добавленных кодов.
                step.updateDevice("PAUSE"); // Статусы бывают: "PAUSE" и "RUNNING".
                step.tryToGetCodesFromPrintingPool(10); // Значение вывода кодов (лимит) в ответе.
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @Test
    @Tag("API")
    @Story("COREDEV-822")
    @DisplayName("Тест на проверку успешного получения одного кода из пула для печати посредством 'API'.")
    @Description("Проверка, что можно успешно получить один код из пула для печати посредством 'API'.")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("https://algaid.atlassian.net/browse/COREDEV-822")
    public void shouldGetACodeFromPrintingPool() throws SQLException {
        RetryFailedTests.runTestWithRetry(() -> {
            try {
                step.tryToAddCodes(1000); // Значение количества добавленных кодов.
                step.updateDevice("PAUSE"); // Статусы бывают: "PAUSE" и "RUNNING".
                step.tryToGetACodeFromPrintingPool();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
    }

    @After
    public void teardown() throws SQLException {
        databaseCleaning();
    }

    @Description("Вспомогательные методы: осуществление удаления тестовых данных из базы данных.")
    @Tag("Database")
    @Severity(SeverityLevel.CRITICAL)
    private static void databaseCleaning() throws SQLException {
        PrintingAdaptForKeeper
                .cleanDatabaseTotally("datamatrix");
        PrintingAdaptForKeeper
                .cleanDatabaseTotally("params");
        PrintingAdaptForKeeper
                .cleanDatabaseTotally("device");
        PrintingAdaptForKeeper
                .cleanDatabaseTotally("sub_orders");
    }
}