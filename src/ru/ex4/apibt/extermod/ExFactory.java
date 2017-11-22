package ru.ex4.apibt.extermod;

import ru.ex4.apibt.MainProperties;
import ru.ex4.apibt.model.*;
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
    public List<PairSetting> getPairSettings() throws IOException {
        String pair_settings = requestPlugin.get("pair_settings", null);
        return new JSONProcessor<PairSetting>().process(PairSetting.class, pair_settings);
    }

    //      Книга ордеров по валютной паре
    public List<OrderBook> getOrderBook(String pair, Integer limit) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);
        if (limit != null && limit <= 1000) {
            arguments.put("limit", String.valueOf(limit));
        }
        String order_book = requestPlugin.get("order_book", arguments);
        return new JSONProcessor<OrderBook>().process(OrderBook.class, order_book);
    }

    //      Cтатистика цен и объемов торгов по валютным парам
    public List<Ticker> getTicker() throws IOException {
        String ticker = requestPlugin.get("ticker", null);
        return new JSONProcessor<Ticker>().process(Ticker.class, ticker);
    }

    //      Список сделок по валютной паре
    public List<Trade> getTrades(String pair) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);

        String trades = requestPlugin.get("trades", arguments);
        return new JSONProcessor<Trade>().process(Trade.class, trades);
    }

    //      Получение информации об аккаунте пользователя
    public UserInfo getUserInfo() throws IOException {
        String userInfo = requestPlugin.post("user_info", null);
        return new JSONProcessor<UserInfo>().processSimple(UserInfo.class, userInfo);
    }

    //      Получение списока открытых ордеров пользователя
    public List<UserOpenOrder> getUserOpenOrders() throws IOException {
        String userOpenOrders = requestPlugin.post("user_open_orders", null);
        return new JSONProcessor<UserOpenOrder>().process(UserOpenOrder.class, userOpenOrders);
    }

    //      Получение сделок пользователя
    public List<UserTrade> getUserTrades(String pair, Integer limit) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);
        arguments.put("offset", "0");
        if (limit != null && limit <= 1000) {
            arguments.put("limit", String.valueOf(limit));
        }

        String userTrades = requestPlugin.post("user_trades", arguments);
        return new JSONProcessor<UserTrade>().process(UserTrade.class, userTrades);
    }

    //      Создание ордера
    public OrderCreateResult orderCreate(OrderCreate orderCreate) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", orderCreate.getPair());
        arguments.put("quantity", String.valueOf(orderCreate.getQuantity()));
        arguments.put("price", String.valueOf(orderCreate.getPrice()));
        arguments.put("type", orderCreate.getType().name());

        String orderCreated = requestPlugin.post("order_create", arguments);
        return new JSONProcessor<OrderCreateResult>().processSimple(OrderCreateResult.class, orderCreated);
    }

    //    Отмена ордера
    public OrderCreateResult orderCancel(String orderId) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("order_id", orderId);

        String orderCanceled = requestPlugin.post("order_cancel", arguments);
        return new JSONProcessor<OrderCreateResult>().processSimple(OrderCreateResult.class, orderCanceled);
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
