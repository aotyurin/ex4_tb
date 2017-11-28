package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.model.Ticker;
import ru.ex4.apibt.extermod.ExFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TickerOldService {
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static List<Ticker> getTickerDtos() throws IOException {
        return exFactory.getTicker();
    }

    public static Ticker getTickerDtoByPair(String pair) throws IOException {
        List<Ticker> tickerList = getTickerDtos();
        for (Ticker ticker : tickerList) {
            if (ticker.getPair().equals(pair)) {
                return ticker;
            }
        }
        return null;
    }



}
