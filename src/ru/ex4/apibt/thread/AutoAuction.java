package ru.ex4.apibt.thread;

import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.logic.Currency;
import ru.ex4.apibt.logic.OpenOrder;
import ru.ex4.apibt.logic.Wait;

import java.io.IOException;

public class AutoAuction extends Thread{


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Currency.info();

                boolean checkQuotedAndBuy   = Currency.checkQuotedAndBuy();

                boolean checkBaseAndSell    = Currency.checkBaseAndSell();

                boolean checkOrder          = OpenOrder.checkOrder();

                if (!checkQuotedAndBuy && !checkBaseAndSell && !checkOrder) {
                    Logs.error("!!! Выход из программы !!! Нет операций для выполнения !!!");
                    break;
                } else {
                    Wait.sleep(5, "ждем 5 мин и запускаем повторно");
                }
            } catch (IOException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

    }
}
