package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserOrderDao;
import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.OrderCreateResultDto;
import ru.ex4.apibt.dto.UserOpenOrderDto;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OrderService {
    private static UserOrderDao userOrderDao = new UserOrderDao();
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static String orderCreate(OrderCreateDto orderCreateDto, float lastPrice) throws IOException {
        OrderCreateResultDto orderCreateResultDto = exFactory.orderCreate(orderCreateDto);
        Logs.info(orderCreateResultDto.toString());
        if (!orderCreateResultDto.getResult()) {
            throw new RuntimeException
                    ("Ошибка создания ордера !!!" + orderCreateResultDto.getError());
        } else {
            save(orderCreateResultDto.getOrderId(), orderCreateDto, lastPrice);

            return orderCreateResultDto.getOrderId();
        }
    }

    public static void orderCancel(String orderId) throws IOException {
        OrderCreateResultDto orderCreateResultDto = exFactory.orderCancel(orderId);
        Logs.info(orderCreateResultDto.toString());
        if (!orderCreateResultDto.getResult()) {
            throw new RuntimeException("Ошибка отмены ордера: " + orderCreateResultDto.getError());
        } else {
            deleteByOrderId(orderId);
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
        return Collections.emptyList();
    }

    public static float getLastPriceByOrder(String orderId) {
        return userOrderDao.getLastPriceByOrder(orderId);
    }

    private static void save(String orderId, OrderCreateDto orderCreateDto, float lastPrice) {
        userOrderDao.save(orderId, orderCreateDto, lastPrice);
    }

    private static void deleteByOrderId(String orderId) {
        userOrderDao.deleteByOrderId(orderId);
    }



    public static void setStackOrder(String orderId) {
        userOrderDao.setStackOrder(orderId);
    }

    public static String getStackOrder() {
        return userOrderDao.getStackOrder();
    }


}
