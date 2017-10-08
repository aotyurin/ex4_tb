package ru.ex4.apibt.logic;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.TrendType;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.TrendService;

import java.io.IOException;

public class Wait {
    public static boolean upwardTrend(TrendType firstTrendType) throws IOException {
        TrendType trendType = TrendService.getTrendType(IExConst.PAIR);
        if (firstTrendType == null) {
            firstTrendType = trendType;
            sleep(3, " - upwardTrend. Ждем 3 мин");
            upwardTrend(trendType);
        }

        Logs.debug(Wait.class.getClass(), String.format(" firstTrendType = %1$s, trendType = %2$s", firstTrendType.name(), trendType.name()));

        if (firstTrendType == TrendType.downward) {
            sleep(3, " - upwardTrend. Ждем 3 мин");
            upwardTrend(trendType);
        }

        return (firstTrendType != TrendType.downward && trendType == TrendType.upward);
    }

    public static boolean downwardTrend(TrendType lastTrendType) throws IOException {
        TrendType trendType = TrendService.getTrendType(IExConst.PAIR);
        if (lastTrendType == null) {
            lastTrendType = trendType;
        }

        Logs.debug(Wait.class.getClass(), String.format(" trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));

        if (trendType == TrendType.downward) {
            sleep(3, " - downwardTrend. Ждем 3 мин");
            downwardTrend(trendType);
        }
        if (trendType == TrendType.upward) {
            if (lastTrendType != TrendType.downward) {
                sleep(3, " - downwardTrend. Ждем 3 мин");
                downwardTrend(trendType);
            } else {
                trendType = TrendType.flat;
            }
        }

        return TrendType.flat == trendType && lastTrendType == TrendType.downward;
    }


    public static void sleep(long minutes, String msg) {
        try {
            Logs.info(msg);
            Thread.sleep(1000 * 60 * minutes);
        } catch (InterruptedException ignore) {
        }
    }
}