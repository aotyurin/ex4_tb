package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.model.Ticker;
import ru.ex4.apibt.extermod.ExFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TickerService {
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


    public static List<TickerDto> getTickerDtoListByCurrency(String currency) {
        List<TickerDto> tickerDtoList = new ArrayList<>();
        try {
            List<Ticker> tickerList = exFactory.getTicker();
            for (Ticker ticker : tickerList) {
                if (ticker.getPair().startsWith(currency + IExConst.PAIR_PREFIX) || ticker.getPair().endsWith(IExConst.PAIR_PREFIX + currency)) {
                    tickerDtoList.add(new TickerDto(ticker.getPair(), ticker.getBuyPrice(), ticker.getSellPrice(), ticker.getLastTrade(),
                            ticker.getHigh(), ticker.getLow(), ticker.getAvg(), ticker.getVol(), ticker.getVolCurr()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tickerDtoList;
    }
}
