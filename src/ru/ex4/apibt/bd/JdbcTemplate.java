package ru.ex4.apibt.bd;

import org.sqlite.JDBC;
import ru.ex4.apibt.log.Logs;

import java.sql.*;

public class JdbcTemplate {
    private Connection connection = null;
    private static JdbcTemplate jdbcTemplate = null;


    public static synchronized JdbcTemplate getInstance() throws SQLException {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate();
        }
        return jdbcTemplate;
    }

    private JdbcTemplate() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        connection = DriverManager.getConnection("jdbc:sqlite:ex4base.db");
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            Logs.error(e.getMessage());
        }

        super.finalize();
    }

    public void executeUpdate(String sql) {
        executeUpdate(sql, null);
    }

    public void executeUpdate(String sql, PreparedParamsSetter preparedParamsSetter) {
        try {
            String sql2 = replaceParam(sql, preparedParamsSetter);
            run(sql2);
        } catch (SQLException e) {
            Logs.error(e.getMessage());
        }
    }

    public ResultSet executeQuery(String sql) {
        return executeQuery(sql, null);
    }

    public ResultSet executeQuery(String sql, PreparedParamsSetter preparedParamsSetter) {
        try {
            String sql2 = replaceParam(sql, preparedParamsSetter);
            return runResultSet(sql2);
        } catch (SQLException e) {
            Logs.error(e.getMessage());
        }
        return null;
    }


    private void run(String sql2) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        Logs.sql(sql2);
        statement.executeUpdate(sql2);
    }

    private ResultSet runResultSet(String sql2) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        Logs.sql(sql2);
        return statement.executeQuery(sql2);
    }

    private String replaceParam(String sql, PreparedParamsSetter preparedParamsSetter) {
        if (preparedParamsSetter == null) {
            return sql;
        }

        for (PreparedParams preparedParams : preparedParamsSetter.getPreparedParamsList()) {
            sql = sql.replace(":" + preparedParams.getName(), preparedParams.getValue());
        }
        return sql;
    }


}
