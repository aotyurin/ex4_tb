package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.dto.PairSettingDto;
import ru.ex4.apibt.dto.UserInfoDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitBaseDao {
    private JdbcTemplate jdbcTemplate;

    ExFactory exFactory = ExFactory.exFactoryInstance();

    public InitBaseDao() {
        this.jdbcTemplate = new JdbcTemplate();
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

        String sqlCreate__Pair_Setting = "CREATE TABLE IF NOT EXISTS Pair_Setting(pair TEXT, maxAmount REAL, maxPrice REAL, maxQuantity REAL, minAmount REAL, minPrice REAL, minQuantity REAL);";
        jdbcTemplate.executeUpdate(sqlCreate__Pair_Setting);
        String sqlClean__Pair_Setting = "DELETE FROM Pair_Setting;";
        jdbcTemplate.executeUpdate(sqlClean__Pair_Setting);
    }

    public void fillUserInfo() throws IOException {
        UserInfoDto userInfo = exFactory.getUserInfo();

        String uid = userInfo.getUid();

        ArrayList<UserInfoDto.Balance> balances = userInfo.getBalances();
        for (UserInfoDto.Balance balance : balances) {
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("uid", uid);
            prs.setValues("currency", balance.getCurrency());
            prs.setValues("amount", balance.getAmount());
            jdbcTemplate.executeUpdate("insert INTO User_Info_Balance VALUES (:uid, :currency, :amount);", prs);
        }

        ArrayList<UserInfoDto.Balance> reserved = userInfo.getReserved();
        for (UserInfoDto.Balance balance : reserved) {
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("uid", uid);
            prs.setValues("currency", balance.getCurrency());
            prs.setValues("amount", balance.getAmount());
            jdbcTemplate.executeUpdate("insert INTO User_Info_Reserved VALUES (:uid, :currency, :amount);", prs);
        }
    }

    public void fillPairSetting() throws IOException {
        List<PairSettingDto> pairSettings = exFactory.getPairSettings();

        for (PairSettingDto pairSetting : pairSettings) {
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("pair", pairSetting.getPair());
            prs.setValues("maxAmount", pairSetting.getMaxAmount());
            prs.setValues("maxPrice", pairSetting.getMaxPrice());
            prs.setValues("maxQuantity", pairSetting.getMaxQuantity());
            prs.setValues("minAmount", pairSetting.getMinAmount());
            prs.setValues("minPrice", pairSetting.getMinPrice());
            prs.setValues("minQuantity", pairSetting.getMinQuantity());

            jdbcTemplate.executeUpdate("insert INTO Pair_Setting VALUES (:pair, :maxAmount, :maxPrice, :maxQuantity, :minAmount, :minPrice, :minQuantity);", prs);
        }

    }
}
