package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.PairSettingDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.List;

public class PairSettingsService {
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static PairSettingDto getPairSettingByPair(String pair) throws IOException {
        List<PairSettingDto> pairSettings = exFactory.getPairSettings();
        for (PairSettingDto pairSetting : pairSettings) {
            if (pairSetting.getCurrency().equals(pair)) {
                return pairSetting;
            }
        }
        return null;
    }


}