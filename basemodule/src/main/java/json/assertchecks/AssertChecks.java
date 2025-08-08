package json.assertchecks;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
В ДАННОМ КЛАССЕ: второй уровень глубины проверок.
@FYI: Первый уровень - по статус-коду;
</p>
<p>
      Второй уровень - по сообщению от сервера;
</p>
<p>
      Третий уровень - по базе данных.
</p>
 */

public class AssertChecks {
    @Description("Метод для проверки через стрим ожидаемого сообщения от сервера.")
    @Step("Проверяю сообщение от сервера 'assertCheck'.")
    public static void assertCheck(String response, String expectedText) {
        String expectedMessage = expectedText;
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }
}