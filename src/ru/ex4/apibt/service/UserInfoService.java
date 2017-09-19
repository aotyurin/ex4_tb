package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserInfoDao;

public class UserInfoService {
    private static UserInfoDao userInfoDao = new UserInfoDao();


    public static float getBalance(String currency) {
        return userInfoDao.getBalance(currency);
    }


}
