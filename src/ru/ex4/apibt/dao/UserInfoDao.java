package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDao {
    private JdbcTemplate jdbcTemplate;

    public UserInfoDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public float getBalance(String currency) {
        try {
            String sql = "SELECT\n" +
                    "  uid,\n" +
                    "  currency,\n" +
                    "  amount\n" +
                    "FROM User_Info_Balance \n" +
                    "WHERE currency = :currency ;";
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("currency", currency);

            List<Float> balances = new ArrayList<>();
            ResultSet resultSet = jdbcTemplate.executeQuery(sql, prs);
            while (resultSet.next()) {
                balances.add(resultSet.getFloat("amount"));
            }
            if (balances.size() > 1 || balances.isEmpty()) {
                throw new RuntimeException("userInfo sql: записей не найдено или их более одной");
            }
            return balances.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public float getReserved(String currency) {
        try {
            String sql = "SELECT\n" +
                    "  uid,\n" +
                    "  currency,\n" +
                    "  amount\n" +
                    "FROM User_Info_Reserved \n" +
                    "WHERE currency = :currency ;";
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("currency", currency);

            List<Float> reserved = new ArrayList<>();
            ResultSet resultSet = jdbcTemplate.executeQuery(sql, prs);
            while (resultSet.next()) {
                reserved.add(resultSet.getFloat("amount"));
            }
            if (reserved.size() > 1 || reserved.isEmpty()) {
                throw new RuntimeException("userInfo sql: записей не найдено или их более одной");
            }
            return reserved.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
