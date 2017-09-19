package ru.ex4.apibt.logic;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.PairSettingDto;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TypeOrder;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.OrderService;
import ru.ex4.apibt.service.PairSettingsService;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;

import java.io.IOException;

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
        Logs.info(String.format("buyBase. Пытаемся купить %1$s %2$s", IExConst.CURRENCY_BASE, balances));

        if (Wait.upwardTrend(null)) {
            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                float sellPrice = tickerDtoByPair.getSellPrice();
                float quantity = (balances - balances * IExConst.STOCK_FEE) / sellPrice;
                Logs.info(String.format("Покупаем по рынку: кол-во %1$s, цена %2$s", quantity, sellPrice));
                Logs.info("Текущие цены и объемы торгов: " + tickerDtoByPair);
                OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, sellPrice, TypeOrder.buy);
                String orderId = OrderService.orderCreate(orderCreateDto);

                OrderService.save(orderId, orderCreateDto, sellPrice);

                try {
                    Logs.info(String.format(" !!! buyBase. Ждем %1$s мин после покупки", IExConst.ORDER_LIFE));
                    Thread.sleep(1000 * 60 * IExConst.ORDER_LIFE);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    private static void sellBase(float balances) throws IOException {
        Logs.info(String.format("sellBase. Пытаемся продать %1$s %2$s", IExConst.CURRENCY_BASE, balances));

        if (Wait.downwardTrend(null)) {
            TickerDto tickerDtoByPair = TickerService.getTickerDtoByPair(IExConst.PAIR);
            if (tickerDtoByPair != null) {
                float buyPrice = tickerDtoByPair.getBuyPrice();
                float quantity = buyPrice * balances - balances * IExConst.STOCK_FEE;
                Logs.info(String.format("Выставлен ордер на продажу: кол-во %1$s, цена %2$s", quantity, buyPrice));
                Logs.info("Текущие цены и объемы торгов: " + tickerDtoByPair);
                OrderCreateDto orderCreateDto = new OrderCreateDto(IExConst.PAIR, quantity, buyPrice, TypeOrder.sell);
                String orderId = OrderService.orderCreate(orderCreateDto);

                OrderService.save(orderId, orderCreateDto, buyPrice);

                try {
                    Logs.info(String.format(" !!! sellBase. Ждем %1$s мин после продажи", IExConst.ORDER_LIFE));
                    Thread.sleep(1000 * 60 * IExConst.ORDER_LIFE);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }
}