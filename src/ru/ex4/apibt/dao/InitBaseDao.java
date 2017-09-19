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
        String sqlCreate__User_Info_Balance = "CREATE TABLE IF NOT EXISTS User_Info_Balance(uid TEXT, currency TEXT, amount REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__User_Info_Balance);
        String sqlClean__User_Info_Balance = "DELETE FROM User_Info_Balance;";
        jdbcTemplate.executeUpdate(sqlClean__User_Info_Balance);

        String sqlCreate__User_Info_Reserved = "CREATE TABLE IF NOT EXISTS User_Info_Reserved(uid TEXT, currency TEXT, amount REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__User_Info_Reserved);
        String sqlClean__User_Info_Reserved = "DELETE FROM User_Info_Reserved;";
        jdbcTemplate.executeUpdate(sqlClean__User_Info_Reserved);

        String sqlCreate__Pair_Setting = "CREATE TABLE IF NOT EXISTS Pair_Setting(pair TEXT, minQuantity REAL, maxQuantity REAL, minPrice REAL, maxPrice REAL, minAmount REAL, maxAmount REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__Pair_Setting);
        String sqlClean__Pair_Setting = "DELETE FROM Pair_Setting;";
        jdbcTemplate.executeUpdate(sqlClean__Pair_Setting);

        String sqlCreate__User_Order = "CREATE TABLE IF NOT EXISTS User_Order(orderId TEXT, pair TEXT, quantity REAL, price REAL, type TEXT, currPrice REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__User_Order);
        String sqlClean__User_Order = "DELETE FROM User_Order;";
        jdbcTemplate.executeUpdate(sqlClean__User_Order);


    }

    public void fillUserInfo(UserInfoDto userInfo) {
        String uid = userInfo.getUid();

        ArrayList<UserInfoDto.Balance> balances = userInfo.getBalances();
        for (UserInfoDto.Balance balance : balances) {
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("uid", uid);
            prs.setValues("currency", balance.getCurrency());
            prs.setValues("amount", balance.getAmount());
            jdbcTemplate.executeUpdate("INSERT INTO User_Info_Balance VALUES (:uid, :currency, :amount);", prs);
        }

        ArrayList<UserInfoDto.Balance> reserved = userInfo.getReserved();
        for (UserInfoDto.Balance balance : reserved) {
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("uid", uid);
            prs.setValues("currency", balance.getCurrency());
            prs.setValues("amount", balance.getAmount());
            jdbcTemplate.executeUpdate("INSERT INTO User_Info_Reserved VALUES (:uid, :currency, :amount);", prs);
        }
    }

    public void fillPairSetting(List<PairSettingDto> pairSettings) {
        for (PairSettingDto pairSetting : pairSettings) {
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("pair", pairSetting.getPair());
            prs.setValues("minQuantity", pairSetting.getMinQuantity());
            prs.setValues("maxQuantity", pairSetting.getMaxQuantity());
            prs.setValues("minPrice", pairSetting.getMinPrice());
            prs.setValues("maxPrice", pairSetting.getMaxPrice());
            prs.setValues("minAmount", pairSetting.getMinAmount());
            prs.setValues("maxAmount", pairSetting.getMaxAmount());

            jdbcTemplate.executeUpdate("INSERT INTO Pair_Setting VALUES (:pair, :minQuantity, :maxQuantity, :minPrice, :maxPrice, :minAmount, :maxAmount);", prs);
        }

    }
}
