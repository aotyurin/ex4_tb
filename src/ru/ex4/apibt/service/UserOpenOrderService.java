package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserOrderDao;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.OrderCreateResultDto;
import ru.ex4.apibt.dto.UserOpenOrderDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;
import java.util.List;

public class UserOpenOrderService {
    private static ExFactory exFactory = ExFactory.exFactoryInstance();
    private static UserOrderDao userOrderDao = new UserOrderDao();

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

    public static void save(String orderId, OrderCreateDto orderCreateDto, float currPrice) {
        userOrderDao.save(orderId, orderCreateDto, currPrice);
    }

    public static void deleteByOrderId(String orderId) {
        userOrderDao.deleteByOrderId(orderId);
    }

    public static void deleteAll() {
        userOrderDao.deleteAll();
    }
}
