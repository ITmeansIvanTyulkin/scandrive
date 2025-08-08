package data.waitings;

import java.util.concurrent.TimeUnit;

public class Waitings {
    public static void awaitSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Ожидание прервано: " + e.getMessage());
        }
    }
}