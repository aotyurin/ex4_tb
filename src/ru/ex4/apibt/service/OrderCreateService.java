package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.OrderCreateResultDto;
import ru.ex4.apibt.factory.ExFactory;
import ru.ex4.apibt.log.Logs;

import java.io.IOException;

public class OrderCreateService {
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
}
