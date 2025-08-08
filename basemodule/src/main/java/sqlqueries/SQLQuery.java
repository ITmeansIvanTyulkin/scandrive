package sqlqueries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Унифицированные методы и запросы к базе данных для использования в любых модулях, классах и методах, в том
числе в методах с перегрузкой.
 */
public class SQLQuery {
    private Connection conn;
    public SQLQuery(Connection conn) {
        this.conn = conn;
    }

    public List executeQuery(String sql, String columnLabel, List<String> params) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<String> resultArray = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString(columnLabel);
            resultArray.add(id);
        }

        // Закрываем ресурсы.
        rs.close();
        stmt.close();
        conn.close();
        return resultArray;
    }

    public List executeQuery(String sql, String columnLabel) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<String> resultArray = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString(columnLabel);
            resultArray.add(id);
        }

        // Закрываем ресурсы.
        rs.close();
        stmt.close();
        conn.close();
        return resultArray;
    }

    // Работа с Map.
    public List<Map<String, String>> executeQueryMap(String sql, List<String> columns) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Map<String, String>> resultArray = new ArrayList<>();

        while (rs.next()) {
            Map<String, String> row = new HashMap<>();
            for (String column : columns) {
                row.put(column, rs.getString(column));
            }
            resultArray.add(row);
        }

        // Закрываем ресурсы.
        rs.close();
        stmt.close();
        return resultArray;
    }

    public int executeUpdate(String sql) throws SQLException {
        Statement stmt = null;
        int affectedRows = 0;

        try {
            stmt = conn.createStatement();
            affectedRows = stmt.executeUpdate(sql);
        } finally {
            // Закрываем ресурсы.
            if (stmt != null) {
                stmt.close();
            }
            // Не закрываем conn здесь, так как он может использоваться повторно.
        }
        return affectedRows;
    }
}