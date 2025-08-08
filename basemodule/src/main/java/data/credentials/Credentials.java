package data.credentials;

public class Credentials {
// Креды для бэка (вход в базу данных).
    // База 'DATAMATRIX-KEEPER'.
    public static final String DATABASE_DATAMATRIX_KEEPER = System.getenv("DATABASE_DATAMATRIX_KEEPER");
    public static final String DATABASE_DATAMATRIX_KEEPER_LOGIN = System.getenv("DATABASE_DATAMATRIX_KEEPER_LOGIN");
    public static final String DATABASE_DATAMATRIX_KEEPER_PASSWORD = System.getenv("DATABASE_DATAMATRIX_KEEPER_PASSWORD");

    // База 'DATABASE_CONVEYOR_CORE'.
    public static final String DATABASE_CONVEYOR_CORE = System.getenv("DATABASE_CONVEYOR_CORE");
    public static final String DATABASE_CONVEYOR_CORE_LOGIN = System.getenv("DATABASE_CONVEYOR_CORE_LOGIN");
    public static final String DATABASE_CONVEYOR_CORE_PASSWORD = System.getenv("DATABASE_CONVEYOR_CORE_PASSWORD");

    // База 'CMS'.
    public static final String DATABASE_CMS = System.getenv("DATABASE_CMS");
    public static final String DATABASE_CMS_LOGIN = System.getenv("DATABASE_CMS_LOGIN");
    public static final String DATABASE_CMS_PASSWORD = System.getenv("DATABASE_CMS_PASSWORD");

// RabbitMQ.
    // Креды для локального RabbitMQ.
    public static final int PORT_FOR_LOCAL_RABBITMQ = getEnvAsInt("LOCAL_RABBITMQ_PORT", 5672);
    public static final String USERNAME_FOR_LOCAL_RABBITMQ = System.getenv("USERNAME_FOR_LOCAL_RABBITMQ");
    public static final String PASSWORD_FOR_LOCAL_RABBITMQ = System.getenv("PASSWORD_FOR_LOCAL_RABBITMQ");

    // Креды для удалённого RabbitMQ на тестовом стенде.
    public static final int PORT_FOR_REMOTE_RABBITMQ = getEnvAsInt("REMOTE_RABBITMQ_PORT", 5672);
    public static final String USERNAME_FOR_REMOTE_RABBITMQ = System.getenv("USERNAME_FOR_REMOTE_RABBITMQ");
    public static final String PASSWORD_FOR_REMOTE_RABBITMQ = System.getenv("PASSWORD_FOR_REMOTE_RABBITMQ");


// PRIVATE ZONE.
    // Вспомогательный метод с дефолтным значением.
    private static int getEnvAsInt(String varName, int defaultValue) {
        String envValue = System.getenv(varName);
        if (envValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(envValue);
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга переменной " + varName +
                    ". Используется значение по умолчанию: " + defaultValue);
            return defaultValue;
        }
    }
}