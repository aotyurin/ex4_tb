package ru.ex4.apibt.logic;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TypeOrder;
import ru.ex4.apibt.dto.UserOpenOrderDto;
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
        List<UserOpenOrderDto.UserOpenOrder> userOpenOrderList = OrderService.getOpenOrderByPair(IExConst.PAIR);
        if (userOpenOrderList.size() <= 0) {
            Logs.error(String.format(" - открытых ордеров по валюте %1$s не найдено", IExConst.PAIR));
            return false;
        } else {
            for (UserOpenOrderDto.UserOpenOrder userOpenOrder : userOpenOrderList) {
                Logs.info(String.format(" - имеется открытый ордер. %1$s", userOpenOrder));

                TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
                if (tickerDtoByPair != null) {
                    // отклонение цены от текущей в процентах
                    float sellPrice1 = tickerDtoByPair.getSellPrice();
                    float deviationPrice = (userOpenOrder.getPrice() - sellPrice1) / sellPrice1 * 100;
                    Logs.info(String.format(" - отклонение цены ордера от текущей в процентах: %1$s, цена ордера: %2$s, цена продажи: %3$s",
                            deviationPrice, userOpenOrder.getPrice(), sellPrice1));

                    Calendar orderLife = Calendar.getInstance();
                    orderLife.setTime(userOpenOrder.getCreated());
                    orderLife.add(Calendar.DAY_OF_YEAR, IExConst.DAY_ORDER_LIFE);

                    Calendar current = Calendar.getInstance();
                    current.setTime(new Date());

                    if (deviationPrice >= 5 || orderLife.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR)) {
                        Logs.info("Отмена ордера: " + userOpenOrder.getOrderId());
                        OrderService.orderCancel(userOpenOrder.getOrderId());

                        InitBaseService.updateChangeData();

                        continue;
                    }

                    if (userOpenOrder.getType() == TypeOrder.sell) {
                        if (deviationPrice <= 0.5 && deviationPrice > 0) {
                            float lastPriceByOrder = OrderService.getLastPriceByOrder(userOpenOrder.getOrderId());
                            float sellPrice = tickerDtoByPair.getSellPrice();
                            if (lastPriceByOrder > 0 && sellPrice > lastPriceByOrder) {
                                if (Wait.upwardTrend(null)) {
                                    tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
                                    if (tickerDtoByPair != null) {
                                        sellPrice = tickerDtoByPair.getSellPrice();
                                        float deviationPrice2 = (userOpenOrder.getPrice() - sellPrice) / sellPrice * 100;
                                        Logs.info(String.format(" - повышаем ордер. отклонение цены ордера от текущей в процентах: %1$s, цена ордера: %2$s, цена продажи: %3$s",
                                                deviationPrice2, userOpenOrder.getPrice(), sellPrice));
                                        if (deviationPrice2 < deviationPrice) {
                                            OrderService.orderCancel(userOpenOrder.getOrderId());

                                            float quantity = UserInfoService.getBalanceOnLine(IExConst.CURRENCY_BASE);
                                            if (quantity > 0) {
                                                float buyPrice = tickerDtoByPair.getBuyPrice();
                                                float priceProfit = buyPrice + buyPrice * IExConst.STOCK_FEE + buyPrice * IExConst.PROFIT_MARKUP / 10;
                                                OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, priceProfit, TypeOrder.sell);
                                                OrderService.orderCreate(orderCreateDto, sellPrice);
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
