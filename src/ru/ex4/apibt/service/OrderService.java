package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserOrderDao;
import ru.ex4.apibt.model.OrderCreate;
import ru.ex4.apibt.model.OrderCreateResult;
import ru.ex4.apibt.model.UserOpenOrder;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OrderService {
    private static UserOrderDao userOrderDao = new UserOrderDao();
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static String orderCreate(OrderCreate orderCreate, float lastPrice) throws IOException {
        OrderCreateResult orderCreateResult = exFactory.orderCreate(orderCreate);
//        Logs.info("!!! СОЗДАЕМ МНИМЫЙ ОРДЕР id=7777777");
//        OrderCreateResult orderCreateResult = new OrderCreateResult("", "7777777", true);
        Logs.info(orderCreateResult.toString());
        if (!orderCreateResult.getResult()) {
            throw new RuntimeException
                    ("Ошибка создания ордера !!!" + orderCreateResult.getError());
        } else {
            save(orderCreateResult.getOrderId(), orderCreate, lastPrice);

            return orderCreateResult.getOrderId();
        }
    }

    public static void orderCancel(String orderId) throws IOException {
        OrderCreateResult orderCreateResult = exFactory.orderCancel(orderId);
//        Logs.info("!!! ОТМЕНЯЕМ МНИМЫЙ ОРДЕР");
//        OrderCreateResult orderCreateResult = new OrderCreateResult("", null, true);
        Logs.info(orderCreateResult.toString());
        if (!orderCreateResult.getResult()) {
            throw new RuntimeException("Ошибка отмены ордера: " + orderCreateResult.getError());
        } else {
            deleteByOrderId(orderId);
        }
    }

    public static List<UserOpenOrder.UserOpenOrderValue> getOpenOrderByPair(String pair) throws IOException {
        List<UserOpenOrder> userOpenOrders = exFactory.getUserOpenOrders();
        if (userOpenOrders.size() > 0) {
            for (UserOpenOrder userOpenOrder : userOpenOrders) {
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

    private static void save(String orderId, OrderCreate orderCreate, float lastPrice) {
        userOrderDao.save(orderId, orderCreate, lastPrice);
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
