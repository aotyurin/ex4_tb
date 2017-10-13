package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.InitBaseDao;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.logic.Wait;

public class InitBaseService {

    public static void init() {
        InitBaseDao initBaseDao = new InitBaseDao();

        Logs.info("init bd ...");
        Logs.info(" - создаем таблицы");
        initBaseDao.init();

        Logs.info(" - заполняем настройки валютных пар");
        PairSettingsService.update();

        updateChangeData(0L);
    }

    public static void updateChangeData(Long... waitMin) {
        long wait = 1;

        if (waitMin.length > 0) {
            wait = waitMin[0];
        }
        Wait.sleep(wait, String.format("updateChangeData. Ждем %1$s минут ...", wait));

        Logs.info(" - обновляем данные пользователя");
        UserInfoService.update();

        Logs.info(" - обновляем историю сделок пользователя");
        HistoryTradeService.update();
    }


}
