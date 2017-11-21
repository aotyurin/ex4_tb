package ru.ex4.apibt.menuAuction;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.model.Ticker;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.InitBaseService;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;

import java.io.IOException;

public class BuyAuction {

    public void start() throws IOException {
        // todo Currency.buyBase


        float balances_quoted = UserInfoService.getBalanceByCurrency(IExConst.CURRENCY_QUOTED);
        Logs.info("Доступная котируемая валюта:" + balances_quoted);
        Ticker tickerByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerByPair != null) {
            float buyPrice = tickerByPair.getBuyPrice().floatValue();
//            float quantity = (balances - balances * IExConst.STOCK_FEE) / price;
//            Logs.info(String.format("Выставляем ордер на покупку: кол-во %1$s, цена %2$s", quantity, price));
//            OrderCreate orderCreateDto = new OrderCreate(IExConst.PAIR, quantity, price, TypeOrder.buy);
//            String orderId = OrderService.orderCreate(orderCreateDto, buyPrice);
//            OrderService.setStackOrder(orderId);

            InitBaseService.updateChangeData();
        }
    }

}
