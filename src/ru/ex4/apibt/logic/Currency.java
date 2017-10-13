package ru.ex4.apibt.logic;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.*;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.*;
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
    public static boolean checkQuotedAndBuy() throws IOException {
        Logs.info(String.format("Проверяем котируемую валюту %1$s и покупаем ...", IExConst.CURRENCY_QUOTED));
        PairSettingDto pairSettingByPair = PairSettingsService.getPairSettingByPair(IExConst.PAIR);
        TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerDtoByPair != null && pairSettingByPair != null) {
            float balances_quoted = UserInfoService.getBalance(IExConst.CURRENCY_QUOTED);
            float minBalances = tickerDtoByPair.getBuyPrice() * pairSettingByPair.getMinQuantity() + (tickerDtoByPair.getBuyPrice() * pairSettingByPair.getMinQuantity()) * IExConst.STOCK_FEE;
            if (balances_quoted < minBalances) {
                Logs.error(String.format("Недостаточно валюты на счете, необходимо минимум %1$s %2$s. Баланс %1$s %3$s.",
                        IExConst.CURRENCY_QUOTED, minBalances, balances_quoted));
                return false;
            } else {
                Logs.info(String.format("Баланс %1$s %2$s. Попытаемся купить %3$s.",
                        IExConst.CURRENCY_QUOTED, balances_quoted, IExConst.CURRENCY_BASE));
                buyBase(balances_quoted);
                return true;
            }
        }
        return false;
    }

    /**
     * проверяем базовую валюту и продаем
     *
     * @throws IOException
     */
    public static boolean checkBaseAndSell() throws IOException {
        Logs.info(String.format("Проверяем базовую валюту %1$s и продаем ...", IExConst.CURRENCY_BASE));
        float balances_base = UserInfoService.getBalance(IExConst.CURRENCY_BASE);
        if (balances_base <= 0) {
            Logs.error(String.format("Недостаточно валюты для продажи. Баланс %1$s %2$s.",
                    IExConst.CURRENCY_BASE, balances_base));
            return false;
        } else {
            Logs.info(String.format("Баланс %1$s %2$s. Попытаемся продать.", IExConst.CURRENCY_BASE, balances_base));
            sellBase(balances_base);
            return true;
        }
    }

    private static void buyBase(float balances) throws IOException {
        float profit = 0;
        if (Wait.downwardTrend(null)) {
            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                float buyPrice = tickerDtoByPair.getBuyPrice();
                profit = buyPrice - buyPrice * IExConst.STOCK_FEE - buyPrice * IExConst.PROFIT_MARKUP / 5;
            }
        }

        TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerDtoByPair != null) {
            float buyPrice = tickerDtoByPair.getBuyPrice();
            float price = (profit != 0) ? profit : buyPrice;
            float quantity = (balances - balances * IExConst.STOCK_FEE) / price;
            Logs.info(String.format("Выставляем ордер на покупку: кол-во %1$s, цена %2$s", quantity, price));
            Logs.debug(java.util.Currency.class.getName(), "Текущие цены и объемы торгов: " + tickerDtoByPair);
            OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, price, TypeOrder.buy);
            String orderId = OrderService.orderCreate(orderCreateDto, buyPrice);
            OrderService.setStackOrder(orderId);

            InitBaseService.updateChangeData();

            Wait.sleep(IExConst.ORDER_LIFE, String.format(" !!! buyBase. Ждем %1$s мин после покупки", IExConst.ORDER_LIFE));
        }
    }

    private static void sellBase(float quantity) throws IOException {
        TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
        if (tickerDtoByPair != null) {
            float historyPrice = getHistoryPrice();
            float sellPrice = tickerDtoByPair.getSellPrice();
            if (historyPrice > 0 && historyPrice > sellPrice) {
                Logs.error(String.format("Продать %1$s не сможем, текущая цена (%2$s) ниже покупки (%3$s) ", IExConst.CURRENCY_BASE, sellPrice, historyPrice));
                return;
            }

            if (Wait.upwardTrend(null)) {
                tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
                if (tickerDtoByPair != null) {
                    sellPrice = tickerDtoByPair.getSellPrice();
                    float price = sellPrice + sellPrice * IExConst.STOCK_FEE + sellPrice * IExConst.PROFIT_MARKUP;
                    Logs.info(String.format("Выставляем ордер на продажу: кол-во %1$s, по цене %2$s", quantity, price));
                    Logs.debug(java.util.Currency.class.getName(), "Текущие цены и объемы торгов: " + tickerDtoByPair);
                    OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, price, TypeOrder.sell);
                    OrderService.orderCreate(orderCreateDto, sellPrice);

                    InitBaseService.updateChangeData();

                    Wait.sleep(IExConst.ORDER_LIFE, String.format(" !!! sellBase. Ждем %1$s мин после продажи", IExConst.ORDER_LIFE));
                }
            } else {
                Logs.error(String.format("Продать %1$s не смогли, тренд нисходящий", IExConst.CURRENCY_BASE));
            }
        }
    }

    private static float getHistoryPrice() {
        String orderId = OrderService.getStackOrder();
        if (orderId != null) {
            return HistoryTradeService.getPrice(orderId);
        }

        return 0;
    }



    public static void info() throws IOException {
        List<TickerDto> tickerDtoList = TickerService.getTickerDtos();
        for (TickerDto tickerDto : tickerDtoList) {
            Logs.info(String.format("Цены для %1$s \t \t - на продажу: %2$s, \t - на покупку: %3$s", tickerDto.getPair(), tickerDto.getSellPrice(), tickerDto.getBuyPrice()));
        }
    }


}