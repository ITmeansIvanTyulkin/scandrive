package supportutils;

import data.constants.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.sql.SQLException;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.CompletableFuture.anyOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public abstract class ApiMethods {
    private static final Logger LOGGER = Logger.getLogger(ApiMethods.class.getName());

    @Description("Общий метод для извлечения 'id' из базы данных с последующим запросом на ручки. Этот метод выполняет всю общую логику.")
    @Tag("Общая логика.")
    public static String sendGetRequest(String endPoint, String id) throws SQLException {
        Response response = given()
                .when()
                .get(endPoint + id)
                .then()
                .log().all()
                .statusCode(anyOf(equalTo(HttpStatus.SC_OK), equalTo(HttpStatus.SC_NO_CONTENT)))
                .extract().response();

        String responseBody = response.getBody().asString();
        LOGGER.info(Constants.GREEN + "Ответ: " + Constants.RESET + Constants.BLUE + responseBody + ", статус-код: " + response.getStatusCode() + Constants.RESET);

        if (response.getStatusCode() != HttpStatus.SC_OK && response.getStatusCode() != HttpStatus.SC_NO_CONTENT) {
            LOGGER.warning(Constants.RED + "Ошибка при получении ответа от сервера. Код ответа: " + Constants.RESET + Constants.BLUE + response.getStatusCode() + Constants.RESET);
        }
        return responseBody;
    }

    @Description("Общий метод для получения 'JSON'.")
    @Tag("Общая логика.")
    public static String sendGetRequest(String endPoint) throws SQLException {
        Response response = given()
                .when()
                .get(endPoint)
                .then()
                .log().all()
                .statusCode(anyOf(equalTo(HttpStatus.SC_OK), equalTo(HttpStatus.SC_NO_CONTENT)))
                .extract().response();

        String responseBody = response.getBody().asString();
        LOGGER.info(Constants.GREEN + "Ответ: " + Constants.RESET + Constants.BLUE + responseBody + ", статус-код: " + response.getStatusCode() + Constants.RESET);

        if (response.getStatusCode() != HttpStatus.SC_OK && response.getStatusCode() != HttpStatus.SC_NO_CONTENT) {
            LOGGER.warning(Constants.RED + "Ошибка при получении ответа от сервера. Код ответа: " + Constants.RESET + Constants.BLUE + response.getStatusCode() + Constants.RESET);
        }
        return responseBody;
    }

    @Description("Общий метод для отправки запроса 'POST'.")
    @Tag("Общая логика.")
    public static String sendPostRequest(String endPoint, String json) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(endPoint) // Здесь можно сделать параметр для метода, если нужно.
                .then()
                .log().all()
                .statusCode(anyOf(equalTo(HttpStatus.SC_OK), equalTo(HttpStatus.SC_CREATED)))
                .extract().response();

        String responseBody = response.getBody().asString();
        LOGGER.info(Constants.GREEN + "Ответ: " + Constants.RESET + Constants.BLUE + responseBody + ", статус-код: " + response.getStatusCode() + Constants.RESET);

        if (response.getStatusCode() != HttpStatus.SC_OK && response.getStatusCode() != HttpStatus.SC_CREATED) {
            LOGGER.warning(Constants.RED + "Ошибка при получении ответа от сервера. Код ответа: " + Constants.RESET + Constants.BLUE + response.getStatusCode() + Constants.RESET);
        }
        return responseBody;
    }

    @Description("Удаление созданных ранее тестовых данных посредством 'API'.")
    @Tag("Общая логика.")
    public static String sendDeleteRequest(String endPoint, String id) {
        Response response = given()
                .when()
                .delete(endPoint + id)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        String responseBody = response.getBody().asString();
        LOGGER.info(Constants.GREEN + "Ответ: " + Constants.RESET + Constants.BLUE + responseBody + ", статус-код: " + response.getStatusCode() + Constants.RESET);

        if (response.getStatusCode() != HttpStatus.SC_OK) {
            LOGGER.warning(Constants.RED + "Ошибка при получении ответа от сервера. Код ответа: " + Constants.RESET + Constants.BLUE + response.getStatusCode() + Constants.RESET);
        }
        return responseBody;
    }

    @Description("Метод частично обновляет информацию.")
    @Step("Частично обновляю информацию.")
    public static String sendPatchRequest(String endPoint, String json) throws SQLException {
        Response response = given()
                .body(json)
                .when()
                .patch(endPoint)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
        String responseBody = response.getBody().asString();
        LOGGER.info(Constants.GREEN + "Ответ: " + Constants.RESET + Constants.BLUE + responseBody + ", статус-код: " + response.getStatusCode() + Constants.RESET);

        if (response.getStatusCode() != HttpStatus.SC_OK) {
            LOGGER.warning(Constants.RED + "Ошибка при получении ответа от сервера. Код ответа: " + Constants.RESET + Constants.BLUE + response.getStatusCode() + Constants.RESET);
        }
        return responseBody;
    }

    @Description("Метод полностью обновляет информацию.")
    @Step("Полностью обновляет информацию.")
    public static String sendPutRequest(String endPoint, String json) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .put(endPoint)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
        String responseBody = response.getBody().asString();
        LOGGER.info(Constants.GREEN + "Ответ: " + Constants.RESET + Constants.BLUE + responseBody + ", статус-код: " + response.getStatusCode() + Constants.RESET);

        if (response.getStatusCode() != HttpStatus.SC_OK) {
            LOGGER.warning(Constants.RED + "Ошибка при получении ответа от сервера. Код ответа: " + Constants.RESET + Constants.BLUE + response.getStatusCode() + Constants.RESET);
        }
        return responseBody;
    }
}