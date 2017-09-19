package ru.ex4.apibt.extermod;

import ru.ex4.apibt.MainProperties;
import ru.ex4.apibt.dto.*;
import ru.ex4.apibt.extermod.processing.JSONProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ExFactory {
    //region fields
    private static RequestPlugin requestPlugin;
    //endregion

    //region ctor
    private ExFactory() {
    }

    public static ExFactory exFactoryInstance() {
        if (requestPlugin == null) {
            final String path = "main.properties";

            Properties properties = MainProperties.getProperties(path);
            if (properties != null) {
                String url = properties.getProperty("ex.path");
                String key = properties.getProperty("ex.key");
                String secret = properties.getProperty("ex.secret");
                if (url == null | key == null | secret == null) {
                    throw new RuntimeException("Значения в " + path + " заданы не верно");
                }

                requestPlugin = new RequestPlugin(url, key, secret);
            }
        }
        return new ExFactory();
    }
    //endregion

    //region public-method

    //      Cписок валют
    public List getCurrency() throws IOException {
        String currency = requestPlugin.get("currency", null);
        return new JSONProcessor<List>().processSimple(List.class, currency);
    }

    //      Настройки валютных пар
    public List<PairSettingDto> getPairSettings() throws IOException {
        String pair_settings = requestPlugin.get("pair_settings", null);
        return new JSONProcessor<PairSettingDto>().process(PairSettingDto.class, pair_settings);
    }

    //      Книга ордеров по валютной паре
    public List<OrderBookDto> getOrderBook(String pair, Integer limit) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);
        if (limit != null && limit <= 1000) {
            arguments.put("limit", String.valueOf(limit));
        }
        String order_book = requestPlugin.get("order_book", arguments);
        return new JSONProcessor<OrderBookDto>().process(OrderBookDto.class, order_book);
    }

    //      Cтатистика цен и объемов торгов по валютным парам
    public List<TickerDto> getTicker() throws IOException {
        String ticker = requestPlugin.get("ticker", null);
        return new JSONProcessor<TickerDto>().process(TickerDto.class, ticker);
    }

    //      Список сделок по валютной паре
    public List<TradeDto> getTrades(String pair) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);

        String trades = requestPlugin.get("trades", arguments);
        return new JSONProcessor<TradeDto>().process(TradeDto.class, trades);
    }

    //      Получение информации об аккаунте пользователя
    public UserInfoDto getUserInfo() throws IOException {
        String userInfo = requestPlugin.post("user_info", null);
        return new JSONProcessor<UserInfoDto>().processSimple(UserInfoDto.class, userInfo);
    }

    //      Получение списока открытых ордеров пользователя
    public List<UserOpenOrderDto> getUserOpenOrders() throws IOException {
        String userOpenOrders = requestPlugin.post("user_open_orders", null);
        return new JSONProcessor<UserOpenOrderDto>().process(UserOpenOrderDto.class, userOpenOrders);
    }

    //      Получение сделок пользователя
    public List<UserTradeDto> getUserTrades(String pair, Integer limit) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);
        arguments.put("offset", "0");
        if (limit != null && limit <= 1000) {
            arguments.put("limit", String.valueOf(limit));
        }

        String userTrades = requestPlugin.post("user_trades", arguments);
        return new JSONProcessor<UserTradeDto>().process(UserTradeDto.class, userTrades);
    }

    //      Создание ордера
    public OrderCreateResultDto orderCreate(OrderCreateDto orderCreateDto) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", orderCreateDto.getPair());
        arguments.put("quantity", String.valueOf(orderCreateDto.getQuantity()));
        arguments.put("price", String.valueOf(orderCreateDto.getPrice()));
        arguments.put("type", orderCreateDto.getType().name());

        String orderCreated = requestPlugin.post("order_create", arguments);
        return new JSONProcessor<OrderCreateResultDto>().processSimple(OrderCreateResultDto.class, orderCreated);
    }

    //    Отмена ордера
    public OrderCreateResultDto orderCancel(String orderId) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("order_id", orderId);

        String orderCanceled = requestPlugin.post("order_cancel", arguments);
        return new JSONProcessor<OrderCreateResultDto>().processSimple(OrderCreateResultDto.class, orderCanceled);
    }

    //    Подсчет в какую сумму обойдется покупка определенного кол-ва валюты по конкретной валютной паре
    public String requiredAmount(String pair, float quantity) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);
        arguments.put("quantity", String.valueOf(quantity));

        return  requestPlugin.post("required_amount", arguments);
    }

    //endregion


}
