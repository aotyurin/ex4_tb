package ru.ex4.apibt.logic;

import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TrailingDto;
import ru.ex4.apibt.model.TrendType;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.TrailingService;

import java.util.Date;
import java.util.List;

public class TrailingProcess extends Thread {
    private TrailingService trailingService;
    private TickerService tickerService;

    public TrailingProcess() {
        this.trailingService = new TrailingService();
        this.tickerService = new TickerService();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("start thread ");

            List<TrailingDto> trailingDtoList = trailingService.getTrailingDtoList();
            if (trailingDtoList.size()==0) {
                System.out.println("stop ...");
                Thread.currentThread().interrupt();

            }

            List<TickerDto> tickerDtoList = tickerService.getTickerList();

            trailingDtoList.forEach(trailingDto -> tickerDtoList.forEach(tickerDto -> {
                if (trailingDto.getPair().equals(tickerDto.getPair())) {
                    if (trailingDto.getTrendType() == TrendType.upward) {
                        if (tickerDto.getSellPrice().compareTo(trailingDto.getPrice()) != -1) {
                            doWork(trailingDto, tickerDto);
                        }
                    }
                    if (trailingDto.getTrendType() == TrendType.downward) {
                        if (tickerDto.getSellPrice().compareTo(trailingDto.getPrice()) != 1) {
                            doWork(trailingDto, tickerDto);
                        }
                    }
                }
            }));

            Wait.sleep(8, "ждем 8 мин и запускаем повторно");
        }
    }

    private void doWork(TrailingDto trailingDto, TickerDto tickerDto) {
        System.out.println(" --SEND!!! value " + trailingDto.getPrice() + " is reached. Пара " + trailingDto.getPair() + " Цена " + tickerDto.getSellPrice());
        trailingDto.setDateNotify(new Date());
        trailingService.save(trailingDto);
    }

}
