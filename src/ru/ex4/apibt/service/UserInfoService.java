package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserInfoDao;
import ru.ex4.apibt.dto.UserBalanceDto;
import ru.ex4.apibt.model.UserInfo;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoService {
    static ExFactory exFactory = ExFactory.exFactoryInstance();
    private static UserInfoDao userInfoDao = new UserInfoDao();


    public static float getBalanceByCurrency(String currency) {
        return userInfoDao.getBalance(currency);
    }

    public static List<UserInfo.BalanceValue> getBalance() throws SQLException, ParseException {
        return userInfoDao.getUserInfo().getBalanceValues();
    }

    public static BigDecimal getBalanceOnLine(String currency) throws IOException {
        UserInfo userInfo = exFactory.getUserInfo();

        ArrayList<UserInfo.BalanceValue> balanceValues = userInfo.getBalanceValues();
        for (UserInfo.BalanceValue balance : balanceValues) {
            if (balance.getCurrency().equals(currency)) {
                return balance.getAmountBalance();
            }
        }
        return BigDecimal.ZERO;
    }

    public static void update() {
        try {
            UserInfo userInfo = exFactory.getUserInfo();
            userInfoDao.update(userInfo);
        } catch (IOException e) {
            Logs.error(e.getMessage());
        }
    }

    public List<UserBalanceDto> getBalanceDtoList() {
        List<UserBalanceDto> userBalanceDtos = new ArrayList<>();
        try {
            List<UserInfo.BalanceValue> balanceValues = getBalance();
            balanceValues.forEach(balance -> userBalanceDtos.add(new UserBalanceDto(balance.getCurrency(), balance.getAmountBalance(), balance.getAmountReserved())));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return userBalanceDtos;
    }


}
