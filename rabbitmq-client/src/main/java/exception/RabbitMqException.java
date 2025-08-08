package exception;

public class RabbitMqException extends RuntimeException {
    public RabbitMqException(String message, Throwable cause) {
        super(message, cause);
    }
}