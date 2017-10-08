package ru.ex4.apibt.service;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dao.HistoryTradeDao;
import ru.ex4.apibt.dto.UserTradeDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.List;

public class HistoryTradeService {
    private static HistoryTradeDao historyTradeDao = new HistoryTradeDao();
    private static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static float getPrice(String orderId) {
        List<UserTradeDto.UserTrade> userTrades = historyTradeDao.UserTradeDto(orderId);
        for (UserTradeDto.UserTrade userTrade : userTrades) {
            if (orderId.equals(userTrade.getOrderId())) {
                return userTrade.getPrice();
            }
        }

        return 0;
    }

    public static void update() {
        try {
            List<UserTradeDto> userTrades = exFactory.getUserTrades(IExConst.PAIR, null);
            historyTradeDao.update(userTrades);
        } catch (IOException e) {
            Logs.error(e.getMessage());
        }
    }
}
