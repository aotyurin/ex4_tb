package ru.ex4.apibt.bd;

import ru.ex4.apibt.log.Logs;

import java.sql.*;

public class JdbcTemplate {
    private Connection connection = null;

    public JdbcTemplate() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Logs.error(e.getMessage());
        }
    }

    public void executeUpdate(String sql) {
        executeUpdate(sql, null);
    }

    public void executeUpdate(String sql, PreparedParamsSetter preparedStatementSetter) {
        String sql2 = replaceParam(sql, preparedStatementSetter);
        run(sql2);
    }


    private void run(String sql2) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:ex4base.db");
            }
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate(sql2);
        } catch (SQLException e) {
            Logs.error(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logs.error(e.getMessage());
            }
        }
    }

    private String replaceParam(String sql, PreparedParamsSetter preparedParamsSetter) {
        if (preparedParamsSetter == null) {
            return sql;
        }

        for (PreparedParams preparedParams : preparedParamsSetter.getPreparedParams()) {
            sql = sql.replace(":" + preparedParams.getName(), preparedParams.getValue());
        }
        return sql;
    }

}
