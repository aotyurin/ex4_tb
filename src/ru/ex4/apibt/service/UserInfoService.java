package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.UserInfoDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.ArrayList;

public class UserInfoService {
    static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static float getBalances(String pair) throws IOException {
        UserInfoDto userInfo = exFactory.getUserInfo();

        for (UserInfoDto.PairSum pairSum : userInfo.getBalances()) {
            if (pairSum.getPair().equals(pair)) {
                return pairSum.getAmount();
            }
        }
        return 0;
    }

    public static float getReserved(String pair) throws IOException {
        UserInfoDto userInfo = exFactory.getUserInfo();

        ArrayList<UserInfoDto.PairSum> reserved = userInfo.getReserved();
        if (reserved != null) {
            for (UserInfoDto.PairSum pairSum : reserved) {
                if (pairSum.getPair().equals(pair)) {
                    return pairSum.getAmount();
                }
            }
        }

        return 0;
    }


}
