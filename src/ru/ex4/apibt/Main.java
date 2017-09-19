package ru.ex4.apibt;


import ru.ex4.apibt.dto.*;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.logic.Currency;
import ru.ex4.apibt.service.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {
    static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static void main(String[] args) throws IOException {
        Logs.info("  ---------- hello  ---------- ");

        initBD();

        Currency.checkQuotedAndBuy();

        Currency.checkBaseAndSell();


        //region проверяем ордера

        //// task_1
        Logs.info("Проверяем ордера ... ");
        List<UserOpenOrderDto.UserOpenOrder> userOpenOrderList = OrderService.getOpenOrderByPair(IExConst.PAIR);
        if (userOpenOrderList != null) {
            for (UserOpenOrderDto.UserOpenOrder userOpenOrder : userOpenOrderList) {
                Logs.info(String.format(" - имеется открытый ордер. %1$s", userOpenOrder));

                TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
                if (tickerDtoByPair != null) {
                    // отклонение цены от текущей в процентах
                    int deviationPrice = Math.round((userOpenOrder.getPrice() - tickerDtoByPair.getBuyPrice()) / tickerDtoByPair.getBuyPrice() * 100);
                    Logs.info(" - отклонение цены ордера от текущей в процентах: " + deviationPrice);

                    Calendar orderCreated = Calendar.getInstance();
                    orderCreated.setTime(userOpenOrder.getCreated());
                    Calendar current = Calendar.getInstance();
                    current.setTime(new Date());

                    if (deviationPrice >= 5 || orderCreated.get(Calendar.DAY_OF_YEAR) != current.get(Calendar.DAY_OF_YEAR)) {
                        Logs.info("Отмена ордера: " + userOpenOrder.getOrderId());
                        OrderService.orderCancel(userOpenOrder.getOrderId());

                        OrderService.deleteByOrderId(userOpenOrder.getOrderId());
                    }
                }
            }
        } else {
            Logs.error("удалить из бд 'актуальные ордера', тк уже все проданы");
            OrderService.deleteAll();
        }
        //endregion


        Logs.info("  ---------- end ------------- ");
    }

    private static void initBD() throws IOException {
        new InitBaseService().init();
    }


}
