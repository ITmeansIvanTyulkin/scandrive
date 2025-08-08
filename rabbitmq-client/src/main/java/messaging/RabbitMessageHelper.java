package messaging;

import dto.TotalMarkEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RabbitMessageHelper {
    public static TotalMarkEvent createTotalMarkEvent(
            List<String> markingCodes,
            String bestBeforeDate,
            String dateOfManufacture) {

        TotalMarkEvent event = new TotalMarkEvent();

        // Обязательные поля.
        event.setAggregate_type("SSCC");
        event.setOperation_type("CREATED");
        event.setDevice_id("Line_1");
        event.setAggregate_level(1);
        event.setAggregate_code(generateAggregateCode());

        // Основные данные.
        event.setMarking_codes(markingCodes);
        event.setCreation_date("2025-06-30T17:17:35+03:00");

        // Дополнительная информация.
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("PackLevel", "1");
        additionalInfo.put("date_of_manufacture", dateOfManufacture);
        additionalInfo.put("best_before_date", bestBeforeDate);
        additionalInfo.put("OrganisationGuid", "");
        event.setAdditional_info(additionalInfo);

        // Мета-данные.
        event.setMessage_type("totalmark_event");
        event.setMessage_guid(UUID.randomUUID().toString());

        return event;
    }

    private static String generateAggregateCode() {
        // Генерация 20-значного кода. Либо можно заюзать конструктор из базового модуля.
        return "00104605" + System.currentTimeMillis() % 100000000;
    }
}