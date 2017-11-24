package ru.ex4.apibt;

import java.math.BigDecimal;

public interface IExConst {
    String PAIR_PREFIX = "_";
    String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    int ROUND_DECIMAL_METHOD = BigDecimal.ROUND_DOWN;
    int SCALE_DECIMAL_METHOD = 8;

    String CURRENCY_BASE = "XRP";                           // базовая
    String CURRENCY_QUOTED = "USD";                         // котируемая
    String PAIR = CURRENCY_BASE + PAIR_PREFIX + CURRENCY_QUOTED;    // пара

    int WAIT_ORDER_LIFE = 10;           // время ожидания ордера (минут)
    int DAY_ORDER_LIFE = 1;             // время жизни ордера (дней)
    float STOCK_FEE = 0.002f;         // комиссия 0.002%

    int LAST_TRADE_PERIOD = 4;          // за какой период брать цену (минут)

    float PROFIT_MARKUP = 0.005f;       // навар сделки

    boolean DEBUG = false;
}
