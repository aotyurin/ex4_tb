package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.extermod.ExFactory;

import java.io.IOException;
import java.util.List;

public class TickerService {
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static List<TickerDto> getTickerDtos() throws IOException {
        return exFactory.getTicker();
    }

    public static TickerDto getTickerDtoByPair(String pair) throws IOException {
        List<TickerDto> tickerDtoList = getTickerDtos();
        for (TickerDto tickerDto : tickerDtoList) {
            if (tickerDto.getPair().equals(pair)) {
                return tickerDto;
            }
        }
        return null;
    }


}
