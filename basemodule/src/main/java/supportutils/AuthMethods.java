package supportutils;

import data.constants.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.Tag;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;

public class AuthMethods {
    private static final Logger LOGGER = Logger.getLogger(AuthMethods.class.getName());

    @Description("Создание спецификации, общее для всех steps.")
    @Step("Создаю спецификацию.")
    public static RequestSpecification getSpec(String uri) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(uri);
    }

    @Description("Получение токена авторизации.")
    @Step("Получаю токен авторизации.")
    public static String getAuthToken(String login, String password, String endPoint) {
        RestAssured.useRelaxedHTTPSValidation();
        Map<String, String> credentials = support(login, password);
        return getToken(endPoint, credentials);
    }

    // Вспомогательные методы.
    @Description("Вспомогательный метод, который логин и пароль пользователя складывает в Map.")
    private static Map<String, String> support(String login, String password) {
        Map<String, String> creds = new HashMap<>();
        creds.put("login", login);
        creds.put("password", password);
        return creds;
    }

    @Description("Вспомогательный метод получения токена.")
    private static String getToken(String endPoint, Map<String, String> requestData) {
        RequestSpecification spec = getSpec(endPoint);
        Response response = spec
                .body(requestData)
                .when()
                .post();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException(Constants.RED + "Попытка получить токен провалилась: ".toUpperCase() + response.getStatusCode() + " " + response.getBody().asString() + Constants.RESET);
        }

        // Логирование ответа.
        LOGGER.info(Constants.GREEN + "Ответ: " + Constants.RESET + Constants.BLUE + response.getBody().asString() + Constants.RESET);

        String authToken = response.jsonPath().getString("access_token");
        return authToken;
    }
}