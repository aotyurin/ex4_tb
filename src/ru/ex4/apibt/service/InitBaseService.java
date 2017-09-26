package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.InitBaseDao;
import ru.ex4.apibt.dao.PairSettingDao;
import ru.ex4.apibt.dao.UserInfoDao;
import ru.ex4.apibt.dto.PairSettingDto;
import ru.ex4.apibt.dto.UserInfoDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.List;

public class InitBaseService {
    private ExFactory exFactory = ExFactory.exFactoryInstance();

    public void init() {
        InitBaseDao initBaseDao = new InitBaseDao();
        PairSettingDao pairSettingDao = new PairSettingDao();
        UserInfoDao userInfoDao = new UserInfoDao();

        try {
            Logs.info("init bd ...");
            Logs.info(" - создаем таблицы");

            initBaseDao.init();

            Logs.info(" - заполняем данные пользователя");
            UserInfoDto userInfo = exFactory.getUserInfo();
            userInfoDao.fillUserInfo(userInfo);

            Logs.info(" - заполняем настройки валютных пар");
            List<PairSettingDto> pairSettings = exFactory.getPairSettings();
            pairSettingDao.fillPairSetting(pairSettings);
        } catch (IOException e) {
            Logs.error(e.getMessage());
        }

    }
}
