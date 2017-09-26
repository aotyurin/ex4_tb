package ru.ex4.apibt.logic;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.TrendType;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.TrendService;

import java.io.IOException;

public class Wait {
    public static boolean upwardTrend(TrendType lastTrendType) throws IOException {
        TrendType trendType = TrendService.getTrendType(IExConst.PAIR);
        if (lastTrendType == null) {
            lastTrendType = trendType;
        }

        if (trendType == TrendType.upward) {
            sleep(7, String.format(" - buyBase. Ждем 7 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
            upwardTrend(trendType);
        }
        if (trendType == TrendType.downward) {
            if (lastTrendType != TrendType.upward) {
                sleep(7, String.format(" - buyBase. Ждем 7 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
                upwardTrend(trendType);
            } else {
                trendType = TrendType.flat;
            }
        }
        if (TrendType.flat == trendType && lastTrendType == TrendType.upward) {
            return true;
        }

        return false;
    }

    public static boolean downwardTrend(TrendType lastTrendType) throws IOException {
        TrendType trendType = TrendService.getTrendType(IExConst.PAIR);
        if (lastTrendType == null) {
            lastTrendType = trendType;
        }

        if (trendType == TrendType.downward) {
            sleep(7, String.format(" - sellBase. Ждем 7 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
            downwardTrend(trendType);
        }
        if (trendType == TrendType.upward) {
            if (lastTrendType != TrendType.downward) {
                sleep(9, String.format(" - sellBase. Ждем 9 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
                downwardTrend(trendType);
            } else {
                trendType = TrendType.flat;
            }
        }
        if (TrendType.flat == trendType && lastTrendType == TrendType.downward) {
            return true;
        }
        return false;
    }


    public static void sleep(long minutes, String msg) {
        try {
            Logs.info(msg);
            Thread.sleep(1000 * 60 * minutes);
        } catch (InterruptedException ignore) {
        }
    }
}