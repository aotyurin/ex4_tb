package ru.ex4.apibt;


import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.logic.Currency;
import ru.ex4.apibt.logic.OpenOrder;
import ru.ex4.apibt.logic.Wait;
import ru.ex4.apibt.service.InitBaseService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logs.info("  ---------- hello  ---------- ");

        InitBaseService.init();

        while (true) {
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
        }

        Logs.info("  ---------- end ------------- ");
    }


}
