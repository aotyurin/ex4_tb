package ru.ex4.apibt.service;

import ru.ex4.apibt.dao.UserOrderDao;
import ru.ex4.apibt.dto.OpenOrderDto;
import ru.ex4.apibt.logic.OpenOrder;
import ru.ex4.apibt.model.OrderCreate;
import ru.ex4.apibt.model.OrderCreateResult;
import ru.ex4.apibt.model.UserOpenOrder;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderService {
    private static UserOrderDao userOrderDao = new UserOrderDao();
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static String orderCreate(OrderCreate orderCreate, float lastPrice) throws IOException {
        OrderCreateResult orderCreateResult = exFactory.orderCreate(orderCreate);
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

    public List<OpenOrderDto> getOrderByPair(String pair) {
        ArrayList<OpenOrderDto> openOrderDtoList = new ArrayList<>();
        try {
            List<UserOpenOrder> userOpenOrders = exFactory.getUserOpenOrders();
            userOpenOrders.forEach(userOpenOrder -> {
                if (userOpenOrder.getPair().equals(pair)) {
                    List<UserOpenOrder.UserOpenOrderValue> openOrders = userOpenOrder.getOpenOrders();
                    openOrders.forEach(userOpenOrderValue -> {
                        openOrderDtoList.add(new OpenOrderDto(userOpenOrderValue.getOrderId(), userOpenOrderValue.getPair(), userOpenOrderValue.getType(),
                                userOpenOrderValue.getPrice(), userOpenOrderValue.getQuantity(), userOpenOrderValue.getAmount(), userOpenOrderValue.getCreated()));
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return openOrderDtoList;
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
