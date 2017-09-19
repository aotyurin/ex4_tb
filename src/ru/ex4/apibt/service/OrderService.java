package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserOrderDao;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.OrderCreateResultDto;
import ru.ex4.apibt.dto.UserOpenOrderDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.List;

public class OrderService {
    private static UserOrderDao userOrderDao = new UserOrderDao();
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static String orderCreate(OrderCreateDto orderCreateDto) throws IOException {
        OrderCreateResultDto orderCreateResultDto = exFactory.orderCreate(orderCreateDto);
        Logs.info(orderCreateResultDto.toString());
        if (orderCreateResultDto.getResult()) {
            return orderCreateResultDto.getOrderId();
        } else {
            throw new RuntimeException
                    ("Ошибка создания ордера !!!" + orderCreateResultDto.getError());
        }
    }

    public static void orderCancel(String orderId) throws IOException {
        OrderCreateResultDto orderCreateResultDto = exFactory.orderCancel(orderId);
        Logs.info(orderCreateResultDto.toString());
        if (!orderCreateResultDto.getResult()) {
            throw new RuntimeException("Ошибка отмены ордера: " + orderCreateResultDto.getError());
        }
    }

    public static List<UserOpenOrderDto.UserOpenOrder> getOpenOrderByPair(String pair) throws IOException {
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

    public static float getPriceByOrder(String orderId) {
        return userOrderDao.getPriceByOrder(orderId);
    }

    public static void deleteByOrderId(String orderId) {
        userOrderDao.deleteByOrderId(orderId);
    }

    public static void deleteAll() {
        userOrderDao.deleteAll();
    }


}
