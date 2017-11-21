package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dao.HistoryTradeDao;
import ru.ex4.apibt.model.UserTrade;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.List;

public class HistoryTradeService {
    private static HistoryTradeDao historyTradeDao = new HistoryTradeDao();
    private static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static float getPrice(String orderId) {
        List<UserTrade.UserTradeValue> userTrades = historyTradeDao.UserTradeDto(orderId);
        for (UserTrade.UserTradeValue userTrade : userTrades) {
            if (orderId.equals(userTrade.getOrderId())) {
                return userTrade.getPrice();
            }
        }

        return 0;
    }

    public static void update() {
        try {
            List<UserTrade> userTrades = exFactory.getUserTrades(IExConst.PAIR, null);
            historyTradeDao.update(userTrades);
        } catch (IOException e) {
            Logs.error(e.getMessage());
        }
    }
}
