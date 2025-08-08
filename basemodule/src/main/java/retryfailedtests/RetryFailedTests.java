package retryfailedtests;

import data.constants.Constants;

import java.util.logging.Logger;

public class RetryFailedTests {
    static Logger LOGGER = Logger.getLogger(RetryFailedTests.class.getName());
    private static final int MAX_RETRY_COUNT = 3;

    public static void runTestWithRetry(Runnable test, int maxRetries) {
        int retryCount = 0;
        while (retryCount < MAX_RETRY_COUNT) {
            try {
                test.run();
                LOGGER.info(Constants.GREEN + "Тест успешно выполнен." + Constants.RESET);
                break;
            } catch (Exception exception) {
                LOGGER.warning(Constants.RED + "Тест провален после неудачных попыток. Всего произведено попыток: "
                        + Constants.RESET + Constants.BLUE + retryCount + Constants.RESET);
                error(exception);
                delay();
                retryCount++;
                if (retryCount == maxRetries) {
                    throw new RuntimeException(Constants.RED + "Тест не удалось выполнить после "
                            + Constants.RESET + Constants.BLUE + maxRetries + " попыток." + Constants.RESET);
                }
            }
        }
    }

    private static void error(Exception exception) {
        LOGGER.warning(Constants.RED + "Возникла ошибка при выполнении теста. Детали ошибки: "
                + Constants.RESET + Constants.BLUE + exception.getMessage() + Constants.RESET);
    }

    private static void delay() {
        try {
            Thread.sleep(Constants.EXPECTATION_3000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}