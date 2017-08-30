package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TrendType;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrendService{
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static TrendType getTrendType() throws IOException {
        List<Float> lastTradeList = new ArrayList<>();
        fillTradeList(exFactory, lastTradeList);

        return extractType(lastTradeList);
    }


    private static void fillTradeList(ExFactory exFactory, List<Float> lastTradeList) throws IOException {
        float iter = 5;
        float pauseMin = IExConst.LAST_TRADE_PERIOD / iter;

        for (int i = 0; i < iter; i++) {
            List<TickerDto> ticker = exFactory.getTicker();

            for (TickerDto tickerDto : ticker) {
                if (tickerDto.getCurrency().equals(IExConst.PAIR)) {
                    lastTradeList.add(tickerDto.getLastTrade());
                }
            }

            try {
                Thread.sleep((long)(1000 * 60 * pauseMin));
            } catch (InterruptedException ignore) {
            }
        }
    }

    static TrendType extractType(List<Float> lastTradeList) {
        TrendType trendType = null;

        if (lastTradeList.size() > 1) {
            float lastValue = lastTradeList.get(0);

            for (int i = 1; i < lastTradeList.size(); i++) {
                float d = lastValue - lastTradeList.get(i);
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
