package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TrendType;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrendService {
    public static TrendType getTrendType(String pair) throws IOException {
        Logs.info("определяем тренд в течении >= " + IExConst.LAST_TRADE_PERIOD + " мин.");
        List<Float> lastTradeList = new ArrayList<>();
        fillTradeList(lastTradeList, pair);

        return extractType(lastTradeList);
    }


    private static void fillTradeList(List<Float> lastTradeList, String pair) throws IOException {
        int iter = 5;
        double pauseMin = (double) IExConst.LAST_TRADE_PERIOD / iter;

        for (int i = 0; i < iter; i++) {
            TickerDto tickerDto = TickerService.getTickerDtoByPair(pair);
            if (tickerDto != null) {
                Logs.info("собираем последнии уник. продажи по lastTrade - " + tickerDto);

                float lastTradeRound = Math.round(tickerDto.getLastTrade());
                if (lastTradeList.size() > 0) {
                    Float aFloat = lastTradeList.get(lastTradeList.size() - 1);
                    if (lastTradeRound != aFloat) {
                        lastTradeList.add(lastTradeRound);
                    } else {
                        --i;
                    }
                } else {
                    lastTradeList.add(lastTradeRound);
                }
            }
            try {
                Thread.sleep((long) (1000 * 60 * pauseMin));
            } catch (InterruptedException ignore) {
            }
        }
    }

    static TrendType extractType(List<Float> lastTradeList) {
        TrendType trendType = null;

        if (lastTradeList.size() > 1) {
            float lastValue = lastTradeList.get(0);

            for (int i = 1; i < lastTradeList.size(); i++) {
                float d = Math.round(lastValue) - Math.round(lastTradeList.get(i));
                lastValue = lastTradeList.get(i);

                if ((trendType == TrendType.upward && d > 0) || (trendType == TrendType.downward && d < 0)) {
                    trendType = TrendType.flat;
                } else {
                    if (d < 0) {
                        trendType = TrendType.upward;
                    } else if (d > 0) {
                        trendType = TrendType.downward;
                    } else {
                        trendType = TrendType.flat;
                    }
                }
            }
        }
        return trendType;
    }


}
