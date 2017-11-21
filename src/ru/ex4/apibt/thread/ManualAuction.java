package ru.ex4.apibt.thread;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TypeOrder;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.InitBaseService;
import ru.ex4.apibt.service.OrderService;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;

import java.io.IOException;

public class ManualAuction {

    public void buyBase() throws IOException {
        // todo Currency.buyBase


        float balances_quoted = UserInfoService.getBalance(IExConst.CURRENCY_QUOTED);
        Logs.info("Доступная котируемая валюта:" + balances_quoted);
        TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerDtoByPair != null) {
            float buyPrice = tickerDtoByPair.getBuyPrice();
            float quantity = (balances - balances * IExConst.STOCK_FEE) / price;
            Logs.info(String.format("Выставляем ордер на покупку: кол-во %1$s, цена %2$s", quantity, price));
            OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, price, TypeOrder.buy);
            String orderId = OrderService.orderCreate(orderCreateDto, buyPrice);
            OrderService.setStackOrder(orderId);

            InitBaseService.updateChangeData();
        }
    }

    public void sellBase() {

    }

}
