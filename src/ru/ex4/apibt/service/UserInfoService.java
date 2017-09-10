package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.UserInfoDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.ArrayList;

public class UserInfoService {
    static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static float getBalances(String currency) throws IOException {
        UserInfoDto userInfo = exFactory.getUserInfo();

        for (UserInfoDto.Balance balance : userInfo.getBalances()) {
            if (balance.getCurrency().equals(currency)) {
                return balance.getAmount();
            }
        }
        return 0;
    }

    public static float getReserved(String currency) throws IOException {
        UserInfoDto userInfo = exFactory.getUserInfo();

        ArrayList<UserInfoDto.Balance> reserved = userInfo.getReserved();
        if (reserved != null) {
            for (UserInfoDto.Balance balance : reserved) {
                if (balance.getCurrency().equals(currency)) {
                    return balance.getAmount();
                }
            }
        }
        return 0;
    }


}
