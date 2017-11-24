package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.model.UserInfo;
import ru.ex4.apibt.util.DateUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
                    "  amountBalance\n" +
                    "FROM User_Info_Balance \n" +
                    "WHERE currency = :currency ;";
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("currency", currency);

            List<Float> balances = new ArrayList<>();
            ResultSet resultSet = jdbcTemplate.executeQuery(sql, prs);
            while (resultSet.next()) {
                balances.add(resultSet.getFloat("amountBalance"));
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

    public UserInfo getUserInfo() throws SQLException, ParseException {
        String sql = "SELECT \n" +
                "  uid, \n" +
                "  currency, \n" +
                "  amountBalance, \n" +
                "  amountReserved, \n" +
                "  serverData \n" +
                "FROM User_Info_Balance;";

        ArrayList<UserInfo.BalanceValue> balanceValues = new ArrayList<>();
        UserInfo balanceInfo = new UserInfo();
        String uid = null;
        Date serverData = null;
        ResultSet resultSet = jdbcTemplate.executeQuery(sql);
        while (resultSet.next()) {
            uid = resultSet.getString("uid");
            serverData = DateUtil.parse(resultSet.getString("serverData"));
            String currency = resultSet.getString("currency");
            BigDecimal amountBalance = resultSet.getBigDecimal("amountBalance");
            BigDecimal amountReserved = resultSet.getBigDecimal("amountReserved");

            balanceValues.add(balanceInfo.new BalanceValue(currency, amountBalance, amountReserved));
        }
        return new UserInfo(uid, serverData, balanceValues);
    }


    public void update(UserInfo userInfo) {
        String sql = "INSERT OR REPLACE INTO User_Info_Balance VALUES (:uid, :currency, :amountBalance, :amountReserved, :serverData);";

        String uid = userInfo.getUid();
        Date serverData = userInfo.getServerData();

        ArrayList<UserInfo.BalanceValue> balanceValues = userInfo.getBalanceValues();
        for (UserInfo.BalanceValue balanceValue : balanceValues) {
            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("uid", uid);
            prs.setValues("currency", balanceValue.getCurrency());
            prs.setValues("amountBalance", balanceValue.getAmountBalance());
            prs.setValues("amountReserved", balanceValue.getAmountReserved());
            prs.setValues("serverData", DateUtil.format(serverData));

            jdbcTemplate.executeUpdate(sql, prs);
        }
    }


}
