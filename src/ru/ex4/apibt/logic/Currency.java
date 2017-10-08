package ru.ex4.apibt.logic;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.*;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.OrderService;
import ru.ex4.apibt.service.PairSettingsService;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Currency {
    /**
     * проверяем котируемую валюту и покупаем
     *
     * @throws IOException
     */
    public static void checkQuotedAndBuy() throws IOException {
        Logs.info(String.format("Проверяем котируемую валюту %1$s и покупаем ...", IExConst.CURRENCY_QUOTED));
        PairSettingDto pairSettingByPair = PairSettingsService.getPairSettingByPair(IExConst.PAIR);
        TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerDtoByPair != null && pairSettingByPair != null) {
            float balances_quoted = UserInfoService.getBalance(IExConst.CURRENCY_QUOTED);
            float minBalances = tickerDtoByPair.getBuyPrice() * pairSettingByPair.getMinQuantity() + (tickerDtoByPair.getBuyPrice() * pairSettingByPair.getMinQuantity()) * IExConst.STOCK_FEE;
            if (balances_quoted < minBalances) {
                Logs.error(String.format("Недостаточно валюты на счете, необходимо минимум %1$s %2$s. Баланс %1$s %3$s.",
                        IExConst.CURRENCY_QUOTED, minBalances, balances_quoted));
            } else {
                Logs.info(String.format("Баланс %1$s %2$s. Попытаемся купить %3$s.",
                        IExConst.CURRENCY_QUOTED, balances_quoted, IExConst.CURRENCY_BASE));
                buyBase(balances_quoted);
            }
        }
    }

    /**
     * проверяем базовую валюту и продаем
     *
     * @throws IOException
     */
    public static void checkBaseAndSell() throws IOException {
        Logs.info(String.format("Проверяем базовую валюту %1$s и продаем ...", IExConst.CURRENCY_BASE));
        float balances_base = UserInfoService.getBalance(IExConst.CURRENCY_BASE);
        if (balances_base <= 0) {
            Logs.error(String.format("Недостаточно валюты для продажи. Баланс %1$s %2$s.",
                    IExConst.CURRENCY_BASE, balances_base));
        } else {
            Logs.info(String.format("Баланс %1$s %2$s. Попытаемся продать.", IExConst.CURRENCY_BASE, balances_base));
            sellBase(balances_base);
        }
    }

    private static void buyBase(float balances) throws IOException {
        Logs.info(String.format("buyBase. Пытаемся купить %1$s на %2$s %3$s", IExConst.CURRENCY_BASE, IExConst.CURRENCY_QUOTED, balances));

        if (Wait.downwardTrend(null)) {
            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                float sellPrice = tickerDtoByPair.getSellPrice();
                float quantity = (balances - balances * IExConst.STOCK_FEE) / sellPrice;
                Logs.info(String.format("Покупаем по рынку: кол-во %1$s, цена %2$s", quantity, sellPrice));
                Logs.debug(java.util.Currency.class.getClass(), "Текущие цены и объемы торгов: " + tickerDtoByPair);
                OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, sellPrice, TypeOrder.buy);
                OrderService.orderCreate(orderCreateDto, sellPrice);

                Wait.sleep(IExConst.ORDER_LIFE, String.format(" !!! buyBase. Ждем %1$s мин после покупки", IExConst.ORDER_LIFE));
            }
        } else {
            Logs.error(String.format("Купить %1$s не смогли, тренд восходящий", IExConst.CURRENCY_BASE));
        }

    }

    private static void sellBase(float quantity) throws IOException {
        Logs.info(String.format("sellBase. Пытаемся продать %1$s %2$s", IExConst.CURRENCY_BASE, quantity));

        if (Wait.upwardTrend(null)) {
            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                float buyPrice = tickerDtoByPair.getBuyPrice();
                float price = buyPrice + buyPrice * IExConst.STOCK_FEE + buyPrice * IExConst.PROFIT_MARKUP;
                Logs.info(String.format("Выставляем ордер на продажу: кол-во %1$s, по цене %2$s", quantity, price));
                Logs.debug(java.util.Currency.class.getClass(), "Текущие цены и объемы торгов: " + tickerDtoByPair);
                OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, price, TypeOrder.sell);
                OrderService.orderCreate(orderCreateDto, buyPrice);

                Wait.sleep(IExConst.ORDER_LIFE, String.format(" !!! sellBase. Ждем %1$s мин после продажи", IExConst.ORDER_LIFE));
            }
        } else {
            Logs.error(String.format("Продать %1$s не смогли, тренд нисходящий", IExConst.CURRENCY_BASE));
        }
    }


    public static void checkOrder() throws IOException {
        Logs.info("Проверяем ордера ... ");
        List<UserOpenOrderDto.UserOpenOrder> userOpenOrderList = OrderService.getOpenOrderByPair(IExConst.PAIR);
        for (UserOpenOrderDto.UserOpenOrder userOpenOrder : userOpenOrderList) {
            Logs.info(String.format(" - имеется открытый ордер. %1$s", userOpenOrder));

            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                // отклонение цены от текущей в процентах
                float deviationPrice = (userOpenOrder.getPrice() - tickerDtoByPair.getSellPrice()) / tickerDtoByPair.getSellPrice() * 100;
                Logs.info(" - отклонение цены ордера от текущей в процентах: " + deviationPrice);

                Calendar orderCreated = Calendar.getInstance();
                orderCreated.setTime(userOpenOrder.getCreated());
                Calendar current = Calendar.getInstance();
                current.setTime(new Date());

                if (deviationPrice >= 5 || orderCreated.get(Calendar.DAY_OF_YEAR) != current.get(Calendar.DAY_OF_YEAR)) {
                    Logs.info("Отмена ордера: " + userOpenOrder.getOrderId());
                    OrderService.orderCancel(userOpenOrder.getOrderId());
                }

                if (userOpenOrder.getType() == TypeOrder.sell) {
                    if (deviationPrice <= 0.5 && deviationPrice > 0) {
                        float lastPriceByOrder = OrderService.getLastPriceByOrder(userOpenOrder.getOrderId());
                        float sellPrice = tickerDtoByPair.getSellPrice();
                        if (lastPriceByOrder > 0 && sellPrice > lastPriceByOrder) {
                            if (Wait.upwardTrend(null)) {
                                tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
                                if (tickerDtoByPair != null) {
                                    float deviationPrice2 = (userOpenOrder.getPrice() - tickerDtoByPair.getSellPrice()) / tickerDtoByPair.getSellPrice() * 100;
                                    Logs.info(" - отклонение цены ордера от текущей в процентах: " + deviationPrice2);
                                    if (deviationPrice2 < deviationPrice) {
                                        sellPrice = tickerDtoByPair.getSellPrice();
                                        float buyPrice = tickerDtoByPair.getBuyPrice();
                                        OrderService.orderCancel(userOpenOrder.getOrderId());

                                        float quantity = UserInfoService.getBalance(IExConst.CURRENCY_BASE);
                                        float priceProfit = buyPrice + buyPrice * IExConst.STOCK_FEE + buyPrice * IExConst.PROFIT_MARKUP / 10;
                                        OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, priceProfit, TypeOrder.sell);
                                        OrderService.orderCreate(orderCreateDto, sellPrice);

                                        Wait.sleep(14, " - checkOrder. Ждем 14 мин после пересоздания, upwardTrend ");
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
    }


    public static void info() throws IOException {
        TickerDto tickerDto = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerDto != null) {
            Logs.info(String.format("Цены \t \t - на продажу: %1$s, \t - на покупку: %2$s", tickerDto.getSellPrice(), tickerDto.getBuyPrice()));
        }

    }
}