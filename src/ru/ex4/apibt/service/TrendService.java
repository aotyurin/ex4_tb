package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.model.Ticker;
import ru.ex4.apibt.model.TrendType;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrendService {
    public static TrendType getTrendType(String pair) throws IOException {
        Logs.info("определяем тренд в течении не менее " + IExConst.LAST_TRADE_PERIOD + " мин.");
        List<Integer> lastTradeList = new ArrayList<>();
        fillTradeList(lastTradeList, pair);

        Logs.info("lastTradeList: " + String.valueOf(lastTradeList));

        return extractType(lastTradeList);
    }


    private static void fillTradeList(List<Integer> lastTradeList, String pair) throws IOException {
        int iter = 5;
        double pauseMin = (double) IExConst.LAST_TRADE_PERIOD / iter;

        for (int i = 0; i < iter; i++) {
            Ticker ticker = TickerOldService.getTickerDtoByPair(pair);
            if (ticker != null) {
                Logs.info("собираем последнии уник. продажи по lastTrade - " + ticker);

                int lastTradeRound = Math.round(ticker.getLastTrade().floatValue());
                if (lastTradeList.size() > 0) {
                    int aFloat = lastTradeList.get(lastTradeList.size() - 1);
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

    static TrendType extractType(List<Integer> lastTradeList) {
        if (lastTradeList.size() > 1) {
            Integer firstValue = lastTradeList.get(0);
            Integer lastValue = lastTradeList.get(lastTradeList.size() - 1);

            if (firstValue.compareTo(lastValue) > 0) {
                return TrendType.downward;
            } else if (firstValue.compareTo(lastValue) < 0) {
                return TrendType.upward;
            }
        }
        return TrendType.flat;

//        TrendType trendType = null;
//
//        if (lastTradeList.size() > 1) {
//            float lastValue = lastTradeList.get(0);
//
//            for (int i = 1; i < lastTradeList.size(); i++) {
//                int d = Math.round(lastValue) - Math.round(lastTradeList.get(i));
//                lastValue = lastTradeList.get(i);
//
//                if ((trendType == TrendType.upward && d > 0) || (trendType == TrendType.downward && d < 0)) {
//                    trendType = TrendType.flat;
//                } else {
//                    if (d < 0) {
//                        trendType = TrendType.upward;
//                    } else if (d > 0) {
//                        trendType = TrendType.downward;
//                    } else {
//                        trendType = TrendType.flat;
//                    }
//                }
//            }
//        }
//        return trendType;
    }


}
