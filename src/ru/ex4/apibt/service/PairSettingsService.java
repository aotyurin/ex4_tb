package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.PairSettingDao;
import ru.ex4.apibt.dto.PairSettingDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.List;

public class PairSettingsService {
    private static PairSettingDao pairSettingDao = new PairSettingDao();


    public static PairSettingDto getPairSettingByPair(String pair) {
        return pairSettingDao.getPairSettingByPair(pair);
    }

}
