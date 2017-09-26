package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.dto.PairSettingDto;
import ru.ex4.apibt.dto.UserInfoDto;
import ru.ex4.apibt.log.Logs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String sqlCreate__User_Info_Balance = "CREATE TABLE IF NOT EXISTS User_Info_Balance(uid TEXT, currency TEXT, amount REAL, PRIMARY KEY (uid, currency));";
        jdbcTemplate.executeUpdate(sqlCreate__User_Info_Balance);
        String sqlClean__User_Info_Balance = "DELETE FROM User_Info_Balance;";
        jdbcTemplate.executeUpdate(sqlClean__User_Info_Balance);

        String sqlCreate__User_Info_Reserved = "CREATE TABLE IF NOT EXISTS User_Info_Reserved(uid TEXT, currency TEXT, amount REAL, PRIMARY KEY (uid, currency));";
        jdbcTemplate.executeUpdate(sqlCreate__User_Info_Reserved);
        String sqlClean__User_Info_Reserved = "DELETE FROM User_Info_Reserved;";
        jdbcTemplate.executeUpdate(sqlClean__User_Info_Reserved);

        String sqlCreate__Pair_Setting = "CREATE TABLE IF NOT EXISTS Pair_Setting(pair TEXT PRIMARY KEY, minQuantity REAL, maxQuantity REAL, minPrice REAL, maxPrice REAL, minAmount REAL, maxAmount REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__Pair_Setting);

        String sqlCreate__User_Order = "CREATE TABLE IF NOT EXISTS User_Order(orderId TEXT PRIMARY KEY, pair TEXT, quantity REAL, price REAL, type TEXT, lastPrice REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__User_Order);

    }




}
