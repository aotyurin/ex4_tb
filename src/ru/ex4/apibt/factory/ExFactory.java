package ru.ex4.apibt.factory;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.*;
import ru.ex4.apibt.factory.processing.JSONProcessor;
import ru.ex4.apibt.requestPlugin.ExRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExFactory {
    //region fields
    private static ExRequest exRequest;
    //endregion

    //region ctor
    private ExFactory() {
    }

    public static ExFactory exFactoryInstance() {
        if (exRequest == null) {
            exRequest = new ExRequest(IExConst.Ex_PATH, IExConst.Ex_KEY, IExConst.Ex_SECRET);
        }
        return new ExFactory();
    }
    //endregion

    //region public-method

    //      Cписок валют
    public List getCurrency() throws IOException {
        String currency = exRequest.get("currency", null);
        return new JSONProcessor<List>().processSimple(List.class, currency);
    }

    //      Настройки валютных пар
    public List<PairSettingDto> getPairSettings() throws IOException {
        String pair_settings = exRequest.get("pair_settings", null);
        return new JSONProcessor<PairSettingDto>().process(PairSettingDto.class, pair_settings);
    }

    //      Книга ордеров по валютной паре
    public List<OrderBookDto> getOrderBook(String pair, Integer limit) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);
        if (limit != null && limit <= 1000) {
            arguments.put("limit", String.valueOf(limit));
        }
        String order_book = exRequest.get("order_book", arguments);
        return new JSONProcessor<OrderBookDto>().process(OrderBookDto.class, order_book);
    }

    //      Cтатистика цен и объемов торгов по валютным парам
    public List<TickerDto> getTicker() throws IOException {
        String ticker = exRequest.get("ticker", null);
        return new JSONProcessor<TickerDto>().process(TickerDto.class, ticker);
    }

    //      Список сделок по валютной паре
    public List<TradeDto> getTrades(String pair) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", pair);

        String trades = exRequest.get("trades", arguments);
        return new JSONProcessor<TradeDto>().process(TradeDto.class, trades);
    }

    //      Получение информации об аккаунте пользователя
    public UserInfoDto getUserInfo() throws IOException {
        String userInfo = exRequest.post("user_info", null);
        return new JSONProcessor<UserInfoDto>().processSimple(UserInfoDto.class, userInfo);
    }

    //      Создание ордера
    public OrderCreateResultDto orderCreate(OrderCreateDto orderCreateDto) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("pair", orderCreateDto.getPair());
        arguments.put("quantity", String.valueOf(orderCreateDto.getQuantity()));
        arguments.put("price", String.valueOf(orderCreateDto.getPrice()));
        arguments.put("type", orderCreateDto.getType().name());

        String orderCreated = exRequest.post("order_create", arguments);
        return new JSONProcessor<OrderCreateResultDto>().processSimple(OrderCreateResultDto.class, orderCreated);
    }

    //    Отмена ордера
    public OrderCreateResultDto orderCancel(String orderId) throws IOException {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("order_id", orderId);

        String orderCanceled = exRequest.post("order_cancel", arguments);
        return new JSONProcessor<OrderCreateResultDto>().processSimple(OrderCreateResultDto.class, orderCanceled);
    }

    //endregion


}
