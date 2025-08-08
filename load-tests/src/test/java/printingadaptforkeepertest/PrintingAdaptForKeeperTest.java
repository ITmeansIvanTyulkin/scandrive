package printingadaptforkeepertest;

import data.endpoints.apiendpoints.Swagger;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.apache.http.HttpStatus;
import java.sql.SQLException;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PrintingAdaptForKeeperTest extends Simulation {
    // Определяем конфигурацию HTTP
    HttpProtocolBuilder httpConf = http.baseUrl(Swagger.BASE_URI_TEST_STAGE_8088) // Базовый URL
            .header("Accept", "application/json")
            .header("Content-Type", "application/json");

    // Определяем сценарий
    ScenarioBuilder scn = scenario("Основной сценарий")
            .exec((http("Запрос на добавление принтеров.")
                    .get("/core-printer/v2/devices")
                    .check(status().is(HttpStatus.SC_OK)) // Проверка статуса ответа
    ));

    // Настройка теста
    {
        setUp(scn.injectOpen(atOnceUsers(10))) // Одновременный запуск 10 пользователей
                .protocols(httpConf);

//        setUp(scn.injectOpen(rampUsers(50).during(10))) // Постепенно добавляем 50 пользователей за 30 секунд
//                .protocols(httpConf);
    }
}