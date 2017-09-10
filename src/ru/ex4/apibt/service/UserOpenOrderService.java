package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.UserOpenOrderDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.List;

public class UserOpenOrderService {
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static List<UserOpenOrderDto.UserOpenOrder> getUserOpenOrderByPair(String pair) throws IOException {
        List<UserOpenOrderDto> userOpenOrders = exFactory.getUserOpenOrders();
        if (userOpenOrders.size() > 0) {
            for (UserOpenOrderDto userOpenOrder : userOpenOrders) {
                if (userOpenOrder.getPair().equals(pair)) {
                    return userOpenOrder.getOpenOrders();
                }
            }
        }
        return null;
    }


}
