package ru.ex4.apibt.logic;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.model.OrderCreate;
import ru.ex4.apibt.model.Ticker;
import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.model.UserOpenOrder;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.InitBaseService;
import ru.ex4.apibt.service.OrderService;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OpenOrder {
    public static boolean checkOrder() throws IOException {
        Logs.info("Проверяем ордера ... ");
        List<UserOpenOrder.UserOpenOrderValue> userOpenOrderValueList = OrderService.getOpenOrderByPair(IExConst.PAIR);
        if (userOpenOrderValueList.size() <= 0) {
            Logs.error(String.format(" - открытых ордеров по валюте %1$s не найдено", IExConst.PAIR));
            return false;
        } else {
            for (UserOpenOrder.UserOpenOrderValue userOpenOrderValue : userOpenOrderValueList) {
                Logs.info(String.format(" - имеется открытый ордер. %1$s", userOpenOrderValue));

                Ticker tickerByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
                if (tickerByPair != null) {
                    // отклонение цены от текущей в процентах
                    float sellPrice1 = tickerByPair.getSellPrice().floatValue();
                    float deviationPrice = (userOpenOrderValue.getPrice().floatValue() - sellPrice1) / sellPrice1 * 100;
                    Logs.info(String.format(" - отклонение цены ордера от текущей в процентах: %1$s, цена ордера: %2$s, цена продажи: %3$s",
                            deviationPrice, userOpenOrderValue.getPrice(), sellPrice1));

                    Calendar orderLife = Calendar.getInstance();
                    orderLife.setTime(userOpenOrderValue.getCreated());
                    orderLife.add(Calendar.DAY_OF_YEAR, IExConst.DAY_ORDER_LIFE);

                    Calendar current = Calendar.getInstance();
                    current.setTime(new Date());

                    if (deviationPrice >= 5 || orderLife.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR)) {
                        Logs.info("Отмена ордера: " + userOpenOrderValue.getOrderId());
                        OrderService.orderCancel(userOpenOrderValue.getOrderId());

                        InitBaseService.updateChangeData();

                        continue;
                    }

                    if (userOpenOrderValue.getType() == TypeOrder.sell) {
                        if (deviationPrice <= 0.5 && deviationPrice > 0) {
                            float lastPriceByOrder = OrderService.getLastPriceByOrder(userOpenOrderValue.getOrderId());
                            float sellPrice = tickerByPair.getSellPrice().floatValue();
                            if (lastPriceByOrder > 0 && sellPrice > lastPriceByOrder) {
                                if (Wait.upwardTrend(null)) {
                                    tickerByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
                                    if (tickerByPair != null) {
                                        sellPrice = tickerByPair.getSellPrice().floatValue();
                                        float deviationPrice2 = (userOpenOrderValue.getPrice().floatValue() - sellPrice) / sellPrice * 100;
                                        Logs.info(String.format(" - повышаем ордер. отклонение цены ордера от текущей в процентах: %1$s, цена ордера: %2$s, цена продажи: %3$s",
                                                deviationPrice2, userOpenOrderValue.getPrice(), sellPrice));
                                        if (deviationPrice2 < deviationPrice) {
                                            OrderService.orderCancel(userOpenOrderValue.getOrderId());

                                            float quantity = UserInfoService.getBalanceOnLine(IExConst.CURRENCY_BASE).floatValue();
                                            if (quantity > 0) {
                                                float buyPrice = tickerByPair.getBuyPrice().floatValue();
                                                float priceProfit = buyPrice + buyPrice * IExConst.STOCK_FEE + buyPrice * IExConst.PROFIT_MARKUP / 10;
                                                OrderCreate orderCreate = new OrderCreate(IExConst.PAIR, quantity, priceProfit, TypeOrder.sell);
                                                OrderService.orderCreate(orderCreate, sellPrice);
                                            } else {
                                                Logs.info(" - checkOrder. ордер не пересоздан, тк баланс = : " + quantity);
                                            }

                                            InitBaseService.updateChangeData(2L);

                                            Wait.sleep(IExConst.WAIT_ORDER_LIFE, String.format(" - checkOrder. Ждем %1$s мин после пересоздания", IExConst.WAIT_ORDER_LIFE));

                                            // // ***
                                        }
                                    }
                                } else {
                                    Wait.sleep(9, " - checkOrder. Ждем 9 мин, downwardTrend ");
                                    checkOrder();
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

}
