package databaseconnections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import data.constants.Constants;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbConnection {
    Logger LOGGER = Logger.getLogger(DbConnection.class.getName());
    private static DbConnection instance = null;
    private DataSource dataSource = null;

    private DbConnection() {}

    private void init(String urlToDB, String userBD, String userPasswordBD) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(urlToDB);
        config.setUsername(userBD);
        config.setPassword(userPasswordBD);
        config.setDriverClassName("org.postgresql.Driver");
        // Установка максимального размера пула на 100 соединений.
        config.setMaximumPoolSize(100);

        // Включение логирования.
        config.setConnectionTestQuery("SELECT 1");
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(15000); // Логировать утечки соединений через 15 секунд.

        dataSource = new HikariDataSource(config);
        printDataSourceDetails();
    }

    public static Connection getConnection(String urlToBD, String userBD, String userPasswordBD) throws SQLException {
        if (instance == null) {
            instance = new DbConnection();
            instance.init(urlToBD, userBD, userPasswordBD);
        }
        return instance.dataSource.getConnection();
    }

    public static void close() {
        if (instance != null && instance.dataSource instanceof HikariDataSource) {
            ((HikariDataSource) instance.dataSource).close();
        }
    }

    // Логирование состояния соединений.
    private void printDataSourceDetails() {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            LOGGER.info(Constants.DARK_YELLOW.concat("Максимальный размер пула: ").concat(Constants.RESET + hikariDataSource.getMaximumPoolSize()));
            LOGGER.info(Constants.DARK_YELLOW.concat("Текущие активные соединения: ").concat(Constants.RESET + hikariDataSource.getHikariPoolMXBean().getActiveConnections()));
            LOGGER.info(Constants.DARK_YELLOW.concat("Текущие свободные соединения: ").concat(Constants.RESET + hikariDataSource.getHikariPoolMXBean().getIdleConnections()));
            LOGGER.info(Constants.DARK_YELLOW.concat("Общее количество соединений: ").concat(Constants.RESET + hikariDataSource.getHikariPoolMXBean().getTotalConnections()));
            LOGGER.info(Constants.DARK_YELLOW.concat("Количество соединений, ожидающих выдачи: ").concat(Constants.RESET + hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()));
        } else {
            LOGGER.info(Constants.DARK_YELLOW.concat("DataSource не является экземпляром HikariDataSource.").concat(Constants.RESET));
        }
    }
}