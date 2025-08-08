package sqlqueries.sqlutils;

import data.constants.Constants;
import databaseconnections.DbConnection;
import io.qameta.allure.Description;
import sqlqueries.SQLQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class SqlMethods {
    private static final Logger LOGGER = Logger.getLogger(SqlMethods.class.getName());
    @Description("Универсальный метод парсинга из БД имеющихся 'id'.")
    public static List<String> getIdsFromDatabase(String code, String columnLabel, String urlToBD, String userBD, String userPassword) throws SQLException {
        // Проверка на 'null' при соединении.
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);

        List<String> stringResult = new ArrayList<>();

        try {
            SQLQuery sqlQuery = new SQLQuery(conn);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT " + columnLabel)
                    .append(code);
            List<String> result = sqlQuery.executeQuery(sql.toString(), columnLabel);

            for (int i = 0; i < result.size(); i++) {
                String value = result.get(i);
                stringResult.add(value);
            }

            if (columnLabel.isEmpty()) {
                LOGGER.info(Constants.GREEN + "Ни один " + Constants.RESET + Constants.BLUE + columnLabel + Constants.RESET + Constants.GREEN + " не найден в базе данных." + Constants.RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Ошибка при выполнении запроса: " + e.getMessage());

        } finally {
            // Закрываем соединение и освобождаем ресурсы.
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return stringResult;
    }

    @Description("Метод для получения одного ID из БД по указанному условию.")
    public static String getIdFromDatabase(String code, String columnLabel, String urlToBD, String userBD, String userPassword) throws SQLException {
        // Проверка соединения с БД
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);

        String resultId = null;  // Результат (один ID)

        try {
            SQLQuery sqlQuery = new SQLQuery(conn);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(columnLabel)
                    .append(code);

            List<String> queryResult = sqlQuery.executeQuery(sql.toString(), columnLabel);

            if (!queryResult.isEmpty()) {
                resultId = queryResult.get(0);
            } else {
                LOGGER.info(Constants.GREEN + "ID не найден в базе данных по условию: " + code + Constants.RESET);
            }
        } catch (SQLException e) {
            LOGGER.severe("Ошибка при выполнении запроса: " + e.getMessage());
            throw new SQLException("Ошибка при получении ID: " + e.getMessage());
        } finally {
            // Закрываем соединение и освобождаем ресурсы.
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return resultId;
    }

    @Description("Метод парсинга из БД целых чисел.")
    public static List<Integer> getIntDataFromDatabase(String code, String columnLabel, String urlToBD, String userBD, String userPassword) throws SQLException {
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);

        List<Integer> intResult = new ArrayList<>();

        try {
            SQLQuery sqlQuery = new SQLQuery(conn);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT " + columnLabel)
                    .append(code);
            List<String> result = sqlQuery.executeQuery(sql.toString(), columnLabel);

            for (String value : result) {
                try {
                    Integer intValue = Integer.valueOf(value);
                    intResult.add(intValue);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Не удалось преобразовать значение '" + value + "' в Integer.");
                }
            }

            if (columnLabel.isEmpty()) {
                LOGGER.info(Constants.GREEN + "Ни один " + Constants.RESET + Constants.BLUE + columnLabel + Constants.RESET + Constants.GREEN + " не найден в базе данных." + Constants.RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Ошибка при выполнении запроса: " + e.getMessage());
        }
        return intResult;
    }

    @Description("Проверка, что конкретный, переданный 'id' в базе данных имеет/не имеет флага со значением 'Удалён'.")
    public static boolean isRemoved(String code, String id, String urlToBD, String userBD, String userPassword) throws SQLException {
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);

        boolean isRemoved = false;

        // Проверка на пустой 'id'.
        if (id.isBlank()) {
            return false; // Возвращаем 'false', если 'id' пустой.
        }

        try {
            SQLQuery sqlQuery = new SQLQuery(conn);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT is_removed")
                    .append(code)
                    .append("'" + id + "'");
            List<String> result = sqlQuery.executeQuery(sql.toString(), "is_removed");

            List<String> stringResult = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                String value = result.get(i);
                stringResult.add(value);
            }

            if (!"is_removed".isEmpty()) {
                isRemoved = true;
                LOGGER.info(Constants.GREEN + "ID: " + Constants.RESET + Constants.BLUE + id + Constants.RESET + Constants.GREEN + " имеет статус в базе данных: " + Constants.RESET + Constants.DARK_YELLOW + isRemoved + Constants.RESET + Constants.GREEN + " и является " + (isRemoved ? "удалённым" : "действующим") + ".");
            } else {
                // Если 'id' не найден, устанавливаем значение по умолчанию.
                isRemoved = false;
                LOGGER.info(Constants.GREEN + "ID: " + Constants.RESET + Constants.BLUE + id + Constants.RESET + Constants.GREEN + " не найден в базе данных и считается недействующим." + Constants.RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Ошибка при выполнении запроса: " + e.getMessage());

        } finally {
            // Закрываем соединение и освобождаем ресурсы.
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return isRemoved;
    }

    @Description("Проверка, что конкретный, переданный 'id' в базе данных существует/несуществует.")
    public static boolean isExist(String code, String id, String urlToBD, String userBD, String userPassword) throws SQLException {
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);

        if (id == null || id.isBlank()) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(code + "?")) {
            stmt.setLong(1, Long.parseLong(id));

            try (ResultSet rs = stmt.executeQuery()) {
                boolean exists = rs.next();

                LOGGER.info(Constants.GREEN + "ID: " + Constants.RESET + Constants.BLUE + id + Constants.RESET +
                        Constants.GREEN + " существует в базе данных: " + Constants.RESET +
                        Constants.DARK_YELLOW + exists + Constants.RESET);

                return exists;
            }
        } catch (NumberFormatException e) {
            throw new SQLException("ID должен быть числовым значением", e);
        } catch (SQLException e) {
            LOGGER.severe("Ошибка при выполнении запроса: " + e.getMessage());
            throw new SQLException("Ошибка при проверке существования ID в БД: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    /**
     * Проверяет, что значение в указанной колонке содержит "нужное значение".
     * @param tableName Имя таблицы для проверки
     * @param columnName Имя колонки, где ожидается "нужное значение"
     * @param condition Условие WHERE для выборки (например, "id = 123")
     * @param urlToBD URL базы данных
     * @param userBD Логин БД
     * @param userPassword Пароль БД
     * @return true - если найдено значение с "UPDATED", false - если нет
     * @throws SQLException При ошибках SQL
     */
    public static boolean isValueUpdated(String code,
                                         String tableName,
                                         String columnName,
                                         String condition,
                                         String urlToBD,
                                         String userBD,
                                         String userPassword) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnectionOrThrow(urlToBD, userBD, userPassword);
            String sql = String.format(
                    code,
                    tableName,
                    condition,
                    columnName
            );

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            LOGGER.severe("Ошибка при проверке искомого значения в БД: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }

    @Description("Удаление данных из базы данных.")
    public static void cleanDatabase(String code, String urlToBD, String userBD, String userPassword) throws SQLException {
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE ")
                    .append(code);

            PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info(Constants.GREEN + "Удаление тестовых данных прошло успешно: " + Constants.RESET + Constants.GREEN + " Удалено строк: " + Constants.RESET + Constants.BLUE + rowsAffected + Constants.RESET);
            } else {
                LOGGER.warning(Constants.RED + "Не найдено строк для удаления. База данных чистая." + Constants.RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Ошибка при выполнении запроса: " + e.getMessage());
        } finally {
            // Закрываем соединение и освобождаем ресурсы.
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    @Description("Проверка заполненности/незаполненности любой таблицы в базе данных.")
    public static boolean isFilled(String code, String columnName, String urlToBD, String userBD, String userPassword) throws SQLException {
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);
        boolean isFilled = false;

        try {
            SQLQuery sqlQuery = new SQLQuery(conn);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ")
                    .append(code);

            List<String> result = sqlQuery.executeQuery(sql.toString(), columnName);

            // Проверяем реальные результаты запроса
            if (result != null && !result.isEmpty()) {
                // Проверяем, есть ли хотя бы одно непустое значение
                for (String value : result) {
                    if (value != null && !value.trim().isEmpty()) {
                        isFilled = true;
                        break;
                    }
                }
            }

            LOGGER.info(Constants.GREEN + "В базе данных в колонке " + Constants.BLUE + columnName + Constants.RESET
                    + Constants.GREEN + " найдены значения: " + Constants.RESET + Constants.BLUE + isFilled + Constants.RESET);

        } catch (SQLException e) {
            LOGGER.severe("Ошибка при выполнении запроса: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return isFilled;
    }

    @Description("Зачистка базы данных: конкретной таблицы и связанных с нею таблиц.")
    public static void truncateTableWithCascade(String tableName, String urlToBD, String userBD, String userPassword) throws SQLException {
        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);
        try {
            SQLQuery sqlQuery = new SQLQuery(conn);
            StringBuilder sql = new StringBuilder();
            sql.append("TRUNCATE TABLE ")
                    .append(tableName)
                    .append(" CASCADE");  // Добавляем CASCADE для удаления связанных данных.

            // Выполняем запрос на удаление.
            int affectedRows = sqlQuery.executeUpdate(sql.toString());

            LOGGER.info(Constants.GREEN + "Таблица " + Constants.BLUE + tableName
                    + Constants.RESET + Constants.GREEN + " и связанные таблицы были успешно очищены."
                    + Constants.RESET);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Ошибка при очистке таблицы " + tableName + ": " + e.getMessage());
        }
    }

    @Description("Универсальный метод для выполнения запроса и возврата результатов в виде List<Map<String, Object>>")
    public static List<Map<String, Object>> getAllDataFromDataBase(String code,
                                                                   String urlToBD,
                                                                   String userBD,
                                                                   String userPassword,
                                                                   Object... params) throws SQLException {

        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);
        List<Map<String, Object>> result = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(code)) {
            // Устанавливаем параметры, если они есть
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        Object value = rs.getObject(i);
                        row.put(columnName, value);
                    }
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Ошибка при выполнении запроса: " + e.getMessage());
            throw new SQLException("Ошибка выполнения SQL-запроса: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return result;
    }

    @Description("Универсальный метод для получения значения любого типа из указанной колонки таблицы.")
    public static <T> T getValueFromDatabase(String query,
                                             String columnLabel,
                                             String urlToBD,
                                             String userBD,
                                             String userPassword,
                                             Class<T> type) throws SQLException {

        Connection conn = getConnectionOrThrow(urlToBD, userBD, userPassword);

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                // Обработка разных типов данных.
                if (type == String.class) {
                    return type.cast(rs.getString(columnLabel));
                } else if (type == Integer.class) {
                    return type.cast(rs.getInt(columnLabel));
                } else if (type == Long.class) {
                    return type.cast(rs.getLong(columnLabel));
                } else if (type == Boolean.class) {
                    return type.cast(rs.getBoolean(columnLabel));
                } else if (type == Timestamp.class) {
                    return type.cast(rs.getTimestamp(columnLabel));
                } else if (type == Date.class) {
                    return type.cast(rs.getDate(columnLabel));
                } else if (type == Double.class) {
                    return type.cast(rs.getDouble(columnLabel));
                } else if (type == Float.class) {
                    return type.cast(rs.getFloat(columnLabel));
                } else {
                    // Для неподдерживаемых типов возвращаем как Object.
                    return type.cast(rs.getObject(columnLabel));
                }
            } else {
                LOGGER.info(Constants.GREEN + "Данные не найдены в колонке " + Constants.BLUE +
                        columnLabel + Constants.RESET);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.severe("Ошибка при выполнении запроса: " + e.getMessage());
            throw new SQLException("Ошибка при получении значения из БД: " + e.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    @Description("Подключаюсь к базе данных.")
    private static Connection connectDataBase(String urlToBD, String userBD, String userPassword) throws SQLException {
        try {
            return DbConnection.getConnection(urlToBD, userBD, userPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Description("Вынос соединения с БД и проверка на 'null' в отдельный метод.")
    private static Connection getConnectionOrThrow(String urlToBD, String userBD, String userPassword) throws SQLException {
        Connection conn = connectDataBase(urlToBD, userBD, userPassword);
        if (conn == null) {
            throw new RuntimeException(Constants.RED + "Не могу соединиться с базой данных!".toUpperCase() + Constants.RESET);
        }
        return conn;
    }
}