package service;

import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import config.RabbitMqConfig;
import data.constants.Constants;
import exception.RabbitMqException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMqSender {
    private static final Logger LOGGER = Logger.getLogger(RabbitMqSender.class.getName());

    public static void sendMessage(Object message, String queueName) throws RabbitMqException, NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory factory = RabbitMqConfig.getConnectionFactory();

        LOGGER.log(Level.INFO, "Подключение к RabbitMQ:\nHost: {0}\nPort: {1}\nVHost: {2}",
                new Object[]{factory.getHost(), factory.getPort(), factory.getVirtualHost()});

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Параметры должны совпадать с существующей очередью!
            Map<String, Object> queueArgs = new HashMap<>();
            channel.queueDeclare(
                    RabbitMqConfig.getQueueName(queueName),
                    true,        // durable: должно быть true (как на сервере)
                    false,          // exclusive
                    false,          // autoDelete
                    queueArgs
            );

            String json = new com.google.gson.Gson().toJson(message);
            channel.basicPublish(
                    "",
                    RabbitMqConfig.getQueueName(queueName),
                    null,
                    json.getBytes(StandardCharsets.UTF_8)
            );

            LOGGER.info(Constants.GREEN + "Сообщение успешно отправлено в очередь '" + Constants.RESET +
                    RabbitMqConfig.getQueueName(queueName) + "'");

        } catch (AlreadyClosedException e) {
            String errorMsg = "Ошибка подключения. Проверьте:\n" +
                    "- Параметры очереди (durable/exclusive/autoDelete)\n" +
                    "- rabbitmqctl list_queues -p / name durable auto_delete";
            LOGGER.severe(errorMsg);
            throw new RabbitMqException(errorMsg, e);
        } catch (IOException e) {
            throw new RabbitMqException("Сетевая ошибка: " + e.getMessage(), e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}