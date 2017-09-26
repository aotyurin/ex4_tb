package ru.ex4.apibt;


import ru.ex4.apibt.extermod.ExFactory;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.logic.Currency;
import ru.ex4.apibt.service.InitBaseService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logs.info("  ---------- hello  ---------- ");

        initBD();

        Currency.checkQuotedAndBuy();

        Currency.checkBaseAndSell();

        Currency.checkOrder();


        Logs.info("  ---------- end ------------- ");
    }


    private static void initBD() throws IOException {
        new InitBaseService().init();
    }


}
