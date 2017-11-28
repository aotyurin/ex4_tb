package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.model.Ticker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TickerService {
    private final ExFactory exFactory;

    public TickerService() {
        exFactory = ExFactory.exFactoryInstance();
    }


    public List<TickerDto> getTickerList() {
        List<TickerDto> tickerDtoList = new ArrayList<>();
        try {
            List<Ticker> tickerList = exFactory.getTicker();
            tickerDtoList.addAll(tickerList.stream().map(ticker -> new TickerDto(ticker.getPair(), ticker.getBuyPrice(), ticker.getSellPrice(), ticker.getLastTrade(),
                    ticker.getHigh(), ticker.getLow(), ticker.getAvg(), ticker.getVol(), ticker.getVolCurr())).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tickerDtoList;
    }

    public List<TickerDto> getTickerListByCurrency(String currency) {
        List<TickerDto> tickerList = this.getTickerList();
        tickerList.removeIf(tickerDto -> !(tickerDto.getPair().startsWith(currency + IExConst.PAIR_PREFIX) || tickerDto.getPair().endsWith(IExConst.PAIR_PREFIX + currency)));

        return tickerList;
    }
}
