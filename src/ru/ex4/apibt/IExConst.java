package ru.ex4.apibt;

public interface IExConst {
    String CURRENCY_BASE = "BTC";                           // базовая
    String CURRENCY_QUOTED = "USD";                         // котируемая
    String PAIR = CURRENCY_BASE + "_" + CURRENCY_QUOTED;    // пара

    int WAIT_ORDER_LIFE = 10;           // время ожидания ордера (минут)
    int DAY_ORDER_LIFE = 1;             // время жизни ордера (дней)
    float STOCK_FEE = 0.00002f;         // комиссия 0.002%
    int LAST_TRADE_PERIOD = 4;          // за какой период брать цену (минут)
    float PROFIT_MARKUP = 0.005f;       // навар сделки


    Boolean DEBUG = true;

    String DATE_FORMAT = "yyyy-MM-dd HH:mm";
}
