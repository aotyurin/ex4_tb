package ru.ex4.apibt.service;

import ru.ex4.apibt.dto.OrderCreateDto;
import ru.ex4.apibt.dto.OrderCreateResultDto;
import ru.ex4.apibt.factory.ExFactory;

import java.io.IOException;

public class OrderCreateService {
    private static ExFactory exFactory = ExFactory.exFactoryInstance();


    public static String orderCreate(OrderCreateDto orderCreateDto) throws IOException {
        OrderCreateResultDto orderCreateResultDto = exFactory.orderCreate(orderCreateDto);
        if (orderCreateResultDto.getResult()) {
           return orderCreateResultDto.getOrderId();
        } else {
            throw new RuntimeException
                    ("Ошибка создания ордера !!!" + orderCreateResultDto.getError());
        }
    }


}
