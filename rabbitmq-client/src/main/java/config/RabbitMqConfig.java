package config;

import com.rabbitmq.client.ConnectionFactory;
import data.constants.Constants;
import data.credentials.Credentials;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class RabbitMqConfig {
    private static final String QUEUE_EXTERNAL_DATAMATRIX_CODES = "external-datamatrix-codes";
    private static final String QUEUE_TO_TOTAL_MARK_AGGREGATION = "to-total-mark-aggregation";
    private static final String VHOST = "/";
    private static final boolean SSL = false;  // true, если требуется.


    // Поля для тестового стенда (удалённый RabbitMQ).
    private static final String HOST = "91.109.201.205";
    private static final int PORT = Credentials.PORT_FOR_REMOTE_RABBITMQ;
    private static final String USERNAME = Credentials.USERNAME_FOR_REMOTE_RABBITMQ;
    private static final String PASSWORD = Credentials.PASSWORD_FOR_REMOTE_RABBITMQ;

    public static ConnectionFactory getConnectionFactory() throws NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VHOST);

        if (SSL) {
            factory.useSslProtocol();
        }

        // Таймауты.
        factory.setConnectionTimeout(Constants.EXPECTATION_30000);
        factory.setHandshakeTimeout(Constants.EXPECTATION_30000);

        return factory;
    }

    public static String getQueueName(String queueName) {
        return queueName;
    }
}