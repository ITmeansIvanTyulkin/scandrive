package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import config.RabbitMqConfig;
import data.endpoints.apiendpoints.Swagger;
import exception.RabbitMqException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Slf4j
public class RabbitMqConsumer {
    private final Connection connection;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RabbitMqConsumer() throws RabbitMqException {
        try {
            this.connection = RabbitMqConfig.getConnectionFactory().newConnection();
        } catch (Exception e) {
            throw new RabbitMqException("Не удалось создать соединение с RabbitMQ!", e);
        }
    }

    /**
     * Подписывается на очередь и обрабатывает сообщения с помощью callback
     *
     * @param messageHandler Функция для обработки сообщений (бизнес-логика)
     */
    public void consumeMessages(Consumer<String> messageHandler) throws RabbitMqException {
        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare(Swagger.QUEUE_TO_TOTAL_MARK_AGGREGATION, true, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info("Получено сообщение: {}", message);
                messageHandler.accept(message);
            };

            channel.basicConsume(Swagger.QUEUE_TO_TOTAL_MARK_AGGREGATION, true, deliverCallback, consumerTag -> {
            });
            log.info("Ожидание сообщений в очереди: {}", Swagger.QUEUE_TO_TOTAL_MARK_AGGREGATION);

        } catch (IOException e) {
            throw new RabbitMqException("Не удалось получить сообщения!", e);
        }
    }

    /**
     * Подписывается на очередь и десериализует сообщения в указанный класс
     *
     * @param clazz Класс для десериализации (например, RabbitMessage.class)
     */
    public <T> void consumeMessages(Class<T> clazz, Consumer<T> messageHandler) throws RabbitMqException {
        consumeMessages(rawMessage -> {
            try {
                T message = objectMapper.readValue(rawMessage, clazz);
                messageHandler.accept(message);
            } catch (IOException e) {
                log.error("Не удалось десериализовать сообщения: {}", rawMessage, e);
            }
        });
    }

    public void close() {
        try {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (IOException e) {
            log.error("Ошибка корректного закрытия соединения RabbitMQ!", e);
        }
    }
}