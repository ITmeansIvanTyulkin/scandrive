package cleanupschedulercontroller;

import cleanupschedulercontroller.json.ChangeCleanScheduler;
import cleanupschedulercontroller.json.DeleteCleanScheduler;
import cleanupschedulercontroller.json.RestartTask;
import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import data.generators.CronGenerator;
import date.GetDate;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import json.extractfromjson.ExtractingFromJson;
import supportutils.ApiMethods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CleanUpSchedulerController {
    private static final Logger LOGGER = Logger.getLogger(CleanUpSchedulerController.class.getName());

// PUBLIC ZONE.
    // Методы.
    @Description("Метод для получения всех типов расписаний.")
    @Step("Получаю все существующие типы расписаний.")
    public void getAllTypesOfScheduler() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.GET_ALL_TYPES_OF_SCHEDULERS);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "All message types schedule successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для получения всех расписаний по очисткам.")
    @Step("Получаю имеющиеся расписания по очисткам.")
    public String getAllCleanSchedulers() throws SQLException {
        String response = ApiMethods.sendGetRequest(Swagger.GET_ALL_CLEAN_SCHEDULERS);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);

        return response;
    }

    @Description("Метод на установку следующего времени срабатывания по расписанию. " +
            "С добавлением заголовков, как в 'Postman' (иначе, запрос не проходит).")
    @Step("Устанавливаю следующее времени срабатывания по расписани.")
    public void setNextCleanScheduler(String cronString) throws SQLException, UnsupportedEncodingException {
        try {
            String url = Swagger.SET_NEXT_CLEAN_SCHEDULER + "cronString=" + cronString.replace(" ", "%20");

            LOGGER.info(Constants.DARK_YELLOW + "Sending request to: " + Constants.RESET + url);

            // Создаю HTTP соединение вручную для полного контроля.
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            // Добавляю заголовки как в 'Postman'.
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            String response;
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
            } else {
                response = new BufferedReader(new InputStreamReader(connection.getErrorStream()))
                        .lines().collect(Collectors.joining("\n"));
            }

            LOGGER.info(Constants.GREEN + "Response code: " + Constants.RESET + Constants.BLUE + responseCode + Constants.RESET);
            LOGGER.info(Constants.GREEN + "Response body: " + Constants.RESET + Constants.BLUE + response + Constants.RESET);

            String expectedMessage = "Successfully";
            String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
            assertThat(actualMessage)
                    .as("Проверка сообщения об успехе.")
                    .isEqualTo(expectedMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute request", e);
        }
    }

    @Description("Метод на перезапуск задания для отправки сообщений в очередь посредством 'API'.")
    @Step("Осуществляю перезапуск задания для отправки сообщений в очередь посредством 'API'.")
    public void restartTaskToSendMessageToQueue() throws SQLException {
        String json = tryToRestartTaskToSendMessageToQueue();
        String response = ApiMethods.sendPostRequest(Swagger.RESTART_TASK_TO_SEND_MASSAGE_TO_QUEUE, json);

        // Проверки.
        // 1. Проверка по сообщению от сервера.
        String expectedMessage = "Jobs are resent to the queue";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для изменения расписания по очистке.")
    @Step("Осуществляю изменения расписания по очистке.")
    public void changeCleanScheduler() {
        String json = tryToChangeCleanScheduler();
        String response = ApiMethods.sendPostRequest(Swagger.CHANGE_CLEAN_SCHEDULER, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "Successfully";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

    @Description("Метод для удаления расписания по очистке.")
    @Step("Удаляю расписание по очистке.")
    public void deleteCleanScheduler() {
        String json = tryToDeleteCleanScheduler();
        String response = ApiMethods.sendPostRequest(Swagger.DELETE_CLEAN_SCHEDULER, json);

        // Проверки.
        // Проверка по сообщению от сервера.
        String expectedMessage = "The schedule was successfully deleted";
        String actualMessage = ExtractingFromJson.extractingAnyFieldFromJson(response, "error");
        assertThat(actualMessage)
                .as("Проверка сообщения об успехе.")
                .isEqualTo(expectedMessage);
    }

// PRIVATE ZONE.
    @Description("Вынос полей для формирования 'JSON' для перезапуска задания и отправки сообщения в очередь.")
    @Step("Произвожу сериализацию для перезапуска задания и отправке сообщения в очередь.")
    private String tryToRestartTaskToSendMessageToQueue() {
        // Генерируем cron.
        String cronExpression = CronGenerator.generateCronExpression(
                "0",
                "0",
                "0",
                "1",
                "*/8"
        );
        RestartTask restartTask = RestartTask.builder()
                .cron(cronExpression)
                .days_to_save(0)
                .next_time_run(GetDate.getTimeAndFormat(true))
                .type("CLEANUP_BBD_OLD_CODES")
                .build();

        LOGGER.info(Constants.GREEN + "Перезапуск задания произошёл успешно: " + Constants.RESET +
                Constants.BLUE + restartTask.getCron() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(restartTask);
    }

    @Description("Вынос полей для формирования 'JSON' для изменения расписания по очистке.")
    @Step("Произвожу сериализацию для изменения расписания по очистке.")
    private String tryToChangeCleanScheduler() {
        // Генерируем cron.
        String cronExpression = CronGenerator.generateCronExpression(
                "0",
                "0",
                "0",
                "2",
                "*/4 *"
        );
        ChangeCleanScheduler changeCleanScheduler = ChangeCleanScheduler.builder()
                .cron(cronExpression)
                .days_to_save(1)
                .next_time_run(GetDate.getTimeAndFormat(true))
                .type("CLEANUP_BBD_OLD_CODES")
                .build();

        LOGGER.info(Constants.GREEN + "Изменение расписания по очистке произошло успешно: " + Constants.RESET +
                Constants.BLUE + changeCleanScheduler.getCron() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(changeCleanScheduler);
    }

    @Description("Вынос полей для формирования 'JSON' для удаления расписания по очистке.")
    @Step("Произвожу сериализацию для удаления расписания по очистке.")
    private String tryToDeleteCleanScheduler() {
        // Генерируем cron.
        String cronExpression = CronGenerator.generateCronExpression(
                "0",
                "0",
                "0",
                "2",
                "/4 *"
        );
        DeleteCleanScheduler deleteCleanScheduler = DeleteCleanScheduler.builder()
                .cron(cronExpression)
                .days_to_save(1)
                .next_time_run(GetDate.getTimeAndFormat(true))
                .type("CLEANUP_BBD_OLD_CODES")
                .build();

        LOGGER.info(Constants.GREEN + "Удаление расписания по очистке произошло успешно: " + Constants.RESET +
                Constants.BLUE + deleteCleanScheduler.getCron() + Constants.RESET);

        // Сериализую.
        Gson gson = new Gson();
        return gson.toJson(deleteCleanScheduler);
    }
}