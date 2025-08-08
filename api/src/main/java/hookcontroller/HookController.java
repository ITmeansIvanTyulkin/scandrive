package hookcontroller;

import com.google.gson.Gson;
import data.constants.Constants;
import data.endpoints.apiendpoints.Swagger;
import hookcontroller.pojo.CreateAndUpdateHook;
import hookcontroller.pojo.DeleteHook;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import printingadaptforkeeper.PrintingAdaptForKeeper;
import supportutils.ApiMethods;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HookController extends PrintingAdaptForKeeper {
    PrintingAdaptForKeeper printingAdaptForKeeper = new PrintingAdaptForKeeper();
    private static final Logger LOGGER = Logger.getLogger(HookController.class.getName());


    // Методы.
    @Description("Метод для создания и обновления хука. В сериализации используется создание принтера.")
    @Step("Создаю/обновляю хук.")
    public void createAndUpdateHook() throws SQLException {
        String json = tryToCreateAndUpdateHook();
        ApiMethods.sendPostRequest(Swagger.CREATE_AND_UPDATE_HOOK, json);
    }

    @Description("Метод для поиска всех существующих хуков.")
    @Step("Нахожу все существующие хуки.")
    public String tryToFindAllExistingHooks() throws SQLException {
        // Создаю/обновляю хук.
        createAndUpdateHook();
        // Получаю все хуки.
        return ApiMethods.sendGetRequest(Swagger.GET_ALL_EXISTING_HOOKS);
    }

    @Description("Метод удаляет хуки по их 'id'.")
    @Step("Удаляю хуки по их распарсенному 'id'")
    public void tryToDeleteHooks() throws SQLException {
        // 1. Получаю все хуки.
        String jsonResponse = tryToFindAllExistingHooks();

        // 2. Парсю JSON и извлекаем ID и deviceName.
        List<Map<String, Object>> hooks = parseAllHooks(jsonResponse);

        // 3. Удаляю каждый хук.
        for (Map<String, Object> hook : hooks) {
            Integer id = (Integer) hook.get("id");
            String deviceName = (String) hook.get("deviceName");

            LOGGER.info(
                    Constants.GREEN + "Удаляю хук ID: " + Constants.RESET +
                    Constants.BLUE + id + Constants.RESET +
                    Constants.GREEN + ", device: " + Constants.RESET +
                    Constants.BLUE + deviceName + Constants.RESET
            );

            // 4. Формирую и отправляю запрос на удаление.
            String deleteJson = tryToDeleteHook(id, deviceName);
            ApiMethods.sendPostRequest(Swagger.DELETE_HOOK, deleteJson);
        }
    }

    @Description("Получение списка всех хуков с их ID и deviceName")
    private List<Map<String, Object>> parseAllHooks(String jsonResponse) {
        List<Map<String, Object>> hooks = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject hook = dataArray.getJSONObject(i);
                Map<String, Object> hookData = new HashMap<>();
                hookData.put("id", hook.getInt("id"));
                hookData.put("deviceName", hook.getString("deviceName"));
                hooks.add(hookData);
            }
        } catch (JSONException e) {
            LOGGER.warning("Ошибка парсинга JSON: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return hooks;
    }

    @Description("Вынос полей для формирования 'JSON' для создания и обновления хука.")
    private String tryToCreateAndUpdateHook() throws SQLException {
        CreateAndUpdateHook createAndUpdateHook = new CreateAndUpdateHook();
        createAndUpdateHook.setActionType("CLEAR_BUFFER");
        createAndUpdateHook.setDeviceName(printingAdaptForKeeper.tryToAddPrinters());
        createAndUpdateHook.setHookType("POST");
        createAndUpdateHook.setHookUrl("localhost:9100");
        createAndUpdateHook.setId(0);

        Gson gson = new Gson();
        return gson.toJson(createAndUpdateHook);
    }

    @Description("Вынос полей для формирования 'JSON' для удаления хука.")
    private String tryToDeleteHook(Integer id, String deviceName) {
        DeleteHook deleteHook = new DeleteHook();
        deleteHook.setActionType("CLEAR_BUFFER");
        deleteHook.setDeviceName(deviceName);
        deleteHook.setHookType("POST");
        deleteHook.setHookUrl("localhost:9100");
        deleteHook.setId(id);

        Gson gson = new Gson();
        return gson.toJson(deleteHook);
    }
}