package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.PairSettingDao;
import ru.ex4.apibt.model.PairSetting;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.List;

public class PairSettingsService {
    private static PairSettingDao pairSettingDao = new PairSettingDao();
    private static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static PairSetting getPairSettingByPair(String pair) {
        return pairSettingDao.getPairSettingByPair(pair);
    }

    public static void update() {
        try {
            List<PairSetting> pairSettings = exFactory.getPairSettings();
            pairSettingDao.update(pairSettings);
        } catch (IOException e) {
            Logs.error(e.getMessage());
        }
    }

}
