package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;

import java.sql.SQLException;

public class InitBaseDao {
    private JdbcTemplate jdbcTemplate;

    public InitBaseDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        String sqlCreate__User_Info_Balance = "CREATE TABLE IF NOT EXISTS User_Info_Balance(uid TEXT, currency TEXT, amountBalance REAL, amountReserved REAL, serverData TEXT, PRIMARY KEY (uid, currency));";
        jdbcTemplate.executeUpdate(sqlCreate__User_Info_Balance);
        String sqlClean__User_Info_Balance = "DELETE FROM User_Info_Balance;";
        jdbcTemplate.executeUpdate(sqlClean__User_Info_Balance);

        String sqlCreate__Pair_Setting = "CREATE TABLE IF NOT EXISTS Pair_Setting(pair TEXT PRIMARY KEY, minQuantity REAL, maxQuantity REAL, minPrice REAL, maxPrice REAL, minAmount REAL, maxAmount REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__Pair_Setting);

        String sqlCreate__Open_Order = "CREATE TABLE IF NOT EXISTS User_Order(orderId TEXT PRIMARY KEY, pair TEXT, quantity REAL, price REAL, type TEXT, lastPrice REAL, date TEXT);";
        jdbcTemplate.executeUpdate(sqlCreate__Open_Order);

        String sqlCreate__Stack_Order = "CREATE TABLE IF NOT EXISTS Stack_Order(orderId TEXT PRIMARY KEY);";
        jdbcTemplate.executeUpdate(sqlCreate__Stack_Order);
//        String sqlClean__Stack_Orde = "DELETE FROM Stack_Order;";
//        jdbcTemplate.executeUpdate(sqlClean__Stack_Orde);

        String sqlCreate__History_Trades = "CREATE TABLE IF NOT EXISTS History_Trades(orderId TEXT, pair TEXT, tradeId TEXT, type TEXT, price REAL, quantity REAL, amount REAL, date TEXT, PRIMARY KEY (orderId, tradeId)   );";
        jdbcTemplate.executeUpdate(sqlCreate__History_Trades);

        String sqlCreate__Stop_Trailing = "CREATE TABLE IF NOT EXISTS Stop_Trailing(pair TEXT, trendType TEXT, price REAL, dateCreated TEXT, dateNotify TEXT, PRIMARY KEY (pair, dateCreated) );";
        jdbcTemplate.executeUpdate(sqlCreate__Stop_Trailing);

    }




}
