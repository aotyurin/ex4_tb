package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserInfoDao;
import ru.ex4.apibt.dto.UserInfoDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;

public class UserInfoService {
    private static UserInfoDao userInfoDao = new UserInfoDao();
//
//    public static float getBalance(String currency) {
//        return userInfoDao.getBalance(currency);
//    }

    static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static float getBalance(String currency) throws IOException {
        UserInfoDto userInfo = exFactory.getUserInfo();

        for (UserInfoDto.Balance balance : userInfo.getBalances()) {
            if (balance.getCurrency().equals(currency)) {
                return balance.getAmount();
            }
        }
        return 0;
    }

    public static void update() {
        try {
            UserInfoDto userInfo = exFactory.getUserInfo();
            userInfoDao.update(userInfo);
        } catch (IOException e) {
            Logs.error(e.getMessage());
        }
    }

}
