package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.PairSettingDao;
import ru.ex4.apibt.dto.PairSettingDto;

public class PairSettingsService {
    private static PairSettingDao pairSettingDao = new PairSettingDao();


    public static PairSettingDto getPairSettingByPair(String pair) {
        return pairSettingDao.getPairSettingByPair(pair);
    }

}
