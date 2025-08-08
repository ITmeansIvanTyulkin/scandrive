package dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TotalMarkEvent {
    // Обязательные поля.
    private String aggregate_type;      // "SSCC"
    private String operation_type;      // "CREATED"
    private String device_id;           // "Line_1"
    private int aggregate_level;        // 1
    private String aggregate_code;      // Генерируемый код

    // Основные данные.
    private List<String> marking_codes;
    private String creation_date;       // Формат: "2025-06-30T17:17:35+03:00"

    // Дополнительная информация.
    private Map<String, Object> additional_info = new HashMap<>();

    // Метаданные.
    private String message_type;        // "totalmark_event"
    private String message_guid;        // UUID

    public TotalMarkEvent() {
        // Фиксированный формат без миллисекунд
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        this.creation_date = ZonedDateTime.now()
                .format(formatter);
    }
}