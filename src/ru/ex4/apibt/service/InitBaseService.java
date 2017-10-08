package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.InitBaseDao;
import ru.ex4.apibt.dao.PairSettingDao;
import ru.ex4.apibt.dao.UserInfoDao;
import ru.ex4.apibt.dao.HistoryTradeDao;
import ru.ex4.apibt.dto.PairSettingDto;
import ru.ex4.apibt.dto.UserInfoDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.List;

public class InitBaseService {

    public void init() {
        InitBaseDao initBaseDao = new InitBaseDao();
        PairSettingDao pairSettingDao = new PairSettingDao();

        Logs.info("init bd ...");
        Logs.info(" - создаем таблицы");
        initBaseDao.init();

        Logs.info(" - заполняем настройки валютных пар");
        PairSettingsService.update();


        Logs.info(" - заполняем данные пользователя");
        UserInfoService.update();

        Logs.info(" - заполняем историю сделок пользователя");
        HistoryTradeService.update();
    }
}
