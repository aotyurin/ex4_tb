package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.List;

public class TickerService {
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static TickerDto getTickerDtoByPair(String pair) throws IOException {
        List<TickerDto> tickerDtoList = exFactory.getTicker();
        for (TickerDto tickerDto : tickerDtoList) {
            if (tickerDto.getCurrency().equals(pair)) {
                return tickerDto;
            }
        }
        return null;
    }


}
