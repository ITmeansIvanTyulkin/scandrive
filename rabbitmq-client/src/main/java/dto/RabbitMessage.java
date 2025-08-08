package dto;

import lombok.Data;
import java.util.Map;

@Data
public class RabbitMessage {
    private String messageType;
    private String messageGuid;
    private Map<String, Object> payload;  // Все поля хранятся как ключ-значение! Должно быть инициализировано!

    // Ручное управление.
    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}