package ru.ex4.apibt;


import ru.ex4.apibt.dto.*;
import ru.ex4.apibt.factory.ExFactory;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {
    static ExFactory exFactory = ExFactory.exFactoryInstance();

    public static void main(String[] args) throws IOException {
        Logs.info("hello");

        //region проверяем котируемую валюту и покупаем
        PairSettingDto pairSettingByPair = PairSettingsService.getPairSettingByPair(IExConst.PAIR);
        TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerDtoByPair != null && pairSettingByPair != null) {
            float balances_quoted = UserInfoService.getBalances(IExConst.CURRENCY_QUOTED);
            float minBalances = tickerDtoByPair.getBuyPrice() * pairSettingByPair.getMinQuantity() + (tickerDtoByPair.getBuyPrice() * pairSettingByPair.getMinQuantity()) * IExConst.STOCK_FEE;
            if (balances_quoted < minBalances) {
                Logs.error(String.format("Недостаточно валюты на счете, необходимо минимум %1$s %2$s. Баланс %1$s %3$s.",
                        IExConst.CURRENCY_QUOTED, minBalances, balances_quoted));
            } else {
                Logs.info(String.format("Баланс %1$s %2$s. Попытаемся купить %3$s.",
                        IExConst.CURRENCY_QUOTED, balances_quoted, IExConst.CURRENCY_BASE));
                buyCurrencyBase(balances_quoted);
            }
        }
        //endregion

        //region проверяем базовую валюту и продаем
        float balances_base = UserInfoService.getBalances(IExConst.CURRENCY_BASE);
        if (balances_base <= 0) {
            Logs.error(String.format("Недостаточно валюты для продажи. Баланс %1$s %2$s.",
                    IExConst.CURRENCY_BASE, balances_base));
        } else {
            Logs.info(String.format("Баланс %1$s %2$s. Попытаемся продать.", IExConst.CURRENCY_BASE, balances_base));
            sellCurrencyBase(balances_base);
        }
        //endregion


        //region проверяем ордера

        //// task_1

        List<UserOpenOrderDto.UserOpenOrder> userOpenOrderList = UserOpenOrderService.getUserOpenOrderByPair(IExConst.PAIR);
        if (userOpenOrderList != null) {
            for (UserOpenOrderDto.UserOpenOrder userOpenOrder : userOpenOrderList) {
                Logs.info(String.format("userOpenOrderByPair. Имеется открытый ордер. %1$s", userOpenOrder));

                tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);

                if (tickerDtoByPair != null) {
                    // отклонение цены от текущей в процентах
                    int deviationPrice = Math.round((userOpenOrder.getPrice() - tickerDtoByPair.getBuyPrice()) / tickerDtoByPair.getBuyPrice() * 100);
                    Logs.info(" - отклонение цены от текущей в процентах: " + deviationPrice);

                    Calendar orderCreated = Calendar.getInstance();
                    orderCreated.setTime(userOpenOrder.getCreated());
                    Calendar current = Calendar.getInstance();
                    current.setTime(new Date());

//                    if (deviationPrice >= 5 || orderCreated.get(Calendar.DAY_OF_YEAR) != current.get(Calendar.DAY_OF_YEAR)) {
//                        Logs.info("Отмена ордера: " + userOpenOrder.getOrderId());
//                        OrderCreateResultDto orderCancel = exFactory.orderCancel(userOpenOrder.getOrderId());
//                        if (!orderCancel.getResult()) {
//                            throw new RuntimeException("Ошибка отмены ордера: " + orderCancel.getError());
//                        }
//                        // удалить order_id из бд 'актуальные ордера'
//                    }
                }
            }
        } else {
            Logs.error("удалить из бд 'актуальные ордера', тк уже все проданы");
        }
        //endregion


        Logs.info(" ---------- end ------------- ");
    }


    private static void buyCurrencyBase(float balances) throws IOException {
        Logs.info(String.format("buyCurrencyBase. Пытаемся купить %1$s %2$s", IExConst.CURRENCY_BASE, balances));

        if (waitUpwardTrend(null)) {
            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                float sellPrice = tickerDtoByPair.getSellPrice();
                float quantity = (balances - balances * IExConst.STOCK_FEE) / sellPrice;
                Logs.info(String.format("Покупаем по рынку: кол-во %1$s, цена %2$s", quantity, sellPrice));
                Logs.info("Текущие цены и объемы торгов: " + tickerDtoByPair);

//                OrderCreateResultDto createResultDto = exFactory.orderCreate(new OrderCreateDto(IExConst.PAIR, quantity, sellPrice, TypeOrder.buy));
//                if (!createResultDto.getResult()) {
//                    throw new RuntimeException("Ошибка покупки по ордеру: " + createResultDto.getError());
//                }
                //todo сохранить в бд "актуальные ордера" order_id и текущую цену sellPrice, TypeOrder.buy

                try {
                    Logs.info(String.format(" !!! buyCurrencyBase. Ждем %1$s мин после покупки", IExConst.ORDER_LIFE));
                    Thread.sleep(1000 * 60 * IExConst.ORDER_LIFE);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }


    private static void sellCurrencyBase(float balances) throws IOException {
        Logs.info(String.format("sellCurrencyBase. Пытаемся продать %1$s %2$s", IExConst.CURRENCY_BASE, balances));

        if (waitDownwardTrend(null)) {
            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                //todo если текущая цена (- IExConst.STOCK_FEE) больше цены ранее покупки IExConst.CURRENCY_BASE, то продаем

                float buyPrice = tickerDtoByPair.getBuyPrice();
                float quantity = buyPrice * balances - balances * IExConst.STOCK_FEE;
                Logs.info(String.format("Выставлен ордер на продажу: кол-во %1$s, цена %2$s", quantity, buyPrice));
                Logs.info("Текущие цены и объемы торгов: " + tickerDtoByPair);
//                OrderCreateResultDto createResultDto = exFactory.orderCreate(new OrderCreateDto(IExConst.PAIR, quantity, buyPrice, TypeOrder.sell));
//                if (!createResultDto.getResult()) {
//                    throw new RuntimeException("Ошибка продажи по ордеру: " + createResultDto.getError());
//                }
                //todo  сохранить в бд "актуальные ордера" order_id и текущую цену buyPrice, TypeOrder.sell

                try {
                    Logs.info(String.format(" !!! sellCurrencyBase. Ждем %1$s мин после продажи", IExConst.ORDER_LIFE));
                    Thread.sleep(1000 * 60 * IExConst.ORDER_LIFE);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    private static boolean waitUpwardTrend(TrendType lastTrendType) throws IOException {
        TrendType trendType = TrendService.getTrendType(IExConst.PAIR);
        if (lastTrendType == null) {
            lastTrendType = trendType;
        }

        if (trendType == TrendType.upward) {
            try {
                Logs.info(String.format(" - buyCurrencyBase. Ждем 7 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
                Thread.sleep(1000 * 60 * 7);            // 7 min
            } catch (InterruptedException ignore) {
            }
            waitUpwardTrend(trendType);
        }
        if (trendType == TrendType.downward) {
            if (lastTrendType != TrendType.upward) {
                try {
                    Logs.info(String.format(" - buyCurrencyBase. Ждем 7 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
                    Thread.sleep(1000 * 60 * 7);            // 9 min
                } catch (InterruptedException ignore) {
                }
                waitUpwardTrend(trendType);
            } else {
                trendType = TrendType.flat;
            }
        }
        if (TrendType.flat == trendType && lastTrendType == TrendType.upward) {
            return true;
        }

        return false;
    }

    private static boolean waitDownwardTrend(TrendType lastTrendType) throws IOException {
        TrendType trendType = TrendService.getTrendType(IExConst.PAIR);
        if (lastTrendType == null) {
            lastTrendType = trendType;
        }

        if (trendType == TrendType.downward) {
            try {
                Logs.info(String.format(" - sellCurrencyBase. Ждем 7 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
                Thread.sleep(1000 * 60 * 7);            // 7 min
            } catch (InterruptedException ignore) {
            }
            waitDownwardTrend(trendType);
        }
        if (trendType == TrendType.upward) {
            if (lastTrendType != TrendType.downward) {
                try {
                    Logs.info(String.format(" - sellCurrencyBase. Ждем 9 мин, trendType = %1$s, lastTrendType = %2$s", trendType.name(), lastTrendType.name()));
                    Thread.sleep(1000 * 60 * 9);            // 9 min
                } catch (InterruptedException ignore) {
                }
                waitDownwardTrend(trendType);
            } else {
                trendType = TrendType.flat;
            }
        }
        if (TrendType.flat == trendType && lastTrendType == TrendType.downward) {
            return true;
        }
        return false;
    }

}
