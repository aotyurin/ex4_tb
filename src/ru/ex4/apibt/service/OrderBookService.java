package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.OrderBookDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.model.OrderBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderBookService {
    private final ExFactory exFactory;

    public OrderBookService() {
        exFactory = ExFactory.exFactoryInstance();
    }


    public OrderBookDto getOrderBookDtoByPair(String pair) {
        List<OrderBookDto.Ask> askArrayList = new ArrayList<>();
        List<OrderBookDto.Bid> bidArrayList = new ArrayList<>();
        try {
            List<OrderBook> orderBookList = exFactory.getOrderBook(pair, 50);

            OrderBookDto obHelper = new OrderBookDto();
            orderBookList.stream().filter(orderBook -> orderBook.getPair().equals(pair)).forEach(orderBook -> {
                ArrayList<OrderBook.OrderValue> orderBookAskList = orderBook.getAsk();
                for (OrderBook.OrderValue orderValueAsk : orderBookAskList) {
                    askArrayList.add(obHelper.new Ask(orderValueAsk.getAmount(), orderValueAsk.getPrice(), orderValueAsk.getQuantity()));
                }
                ArrayList<OrderBook.OrderValue> orderBookBidList = orderBook.getBid();
                for (OrderBook.OrderValue orderValueBid : orderBookBidList) {
                    bidArrayList.add(obHelper.new Bid(orderValueBid.getAmount(), orderValueBid.getPrice(), orderValueBid.getQuantity()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new OrderBookDto(pair, askArrayList, bidArrayList);
    }

}
