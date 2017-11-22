package ru.ex4.apibt.menuAuction;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.model.UserOpenOrder;
import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.service.OrderService;

import java.io.IOException;
import java.util.List;

public class OrderCancel {
    ExFactory exFactory = ExFactory.exFactoryInstance();

    public void start() throws IOException {



        List currencyList = exFactory.getCurrency();
        for (Object so : currencyList) {

        }


        List<UserOpenOrder.UserOpenOrderValue> userOpenOrderValueList = OrderService.getOpenOrderByPair(IExConst.PAIR);
        for (UserOpenOrder.UserOpenOrderValue userOpenOrderValue : userOpenOrderValueList) {
            System.out.println(userOpenOrderValue.toString());
        }

    }
}
