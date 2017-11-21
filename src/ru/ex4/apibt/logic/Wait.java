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
            sleep(2, " - upwardTrend. Ждем 2 мин");
            upwardTrend(trendType);
        }

        Logs.debug(Wait.class.getName(), String.format(" firstTrendType = %1$s, trendType = %2$s", firstTrendType.name(), trendType.name()));

        if (firstTrendType == TrendType.downward) {
            sleep(3, " - upwardTrend. Ждем 3 мин");
            upwardTrend(trendType);
        }

        return (firstTrendType != TrendType.downward && trendType == TrendType.upward);
    }

    public static boolean downwardTrend(TrendType firstTrendType) throws IOException {
        TrendType trendType = TrendService.getTrendType(IExConst.PAIR);
        if (firstTrendType == null) {
            firstTrendType = trendType;
            sleep(2, " - downwardTrend. Ждем 2 мин");
            downwardTrend(trendType);
        }

        Logs.debug(Wait.class.getName(), String.format(" firstTrendType = %1$s, trendType = %2$s", firstTrendType.name(), trendType.name()));

        if (firstTrendType == TrendType.upward) {
            sleep(3, " - downwardTrend. Ждем 3 мин");
            downwardTrend(trendType);
        }

        return (firstTrendType != TrendType.upward && trendType == TrendType.downward);
    }


    public static void sleep(long minutes, String msg) {
        try {
            if (msg != null) {
                Logs.info(msg);
            }
            Thread.sleep(1000 * 60 * minutes);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}