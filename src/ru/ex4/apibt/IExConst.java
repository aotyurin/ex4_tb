package ru.ex4.apibt;

public interface IExConst {
    String CURRENCY_BASE = "BTC";                           // базовая
    String CURRENCY_QUOTED = "USD";                         // котируемая
    String PAIR = CURRENCY_BASE + "_" + CURRENCY_QUOTED;    // пара

    int ORDER_LIFE = 5;                 // время жизни ордера (минут)
    float STOCK_FEE = 0.00002f;         // комиссия 0.002%
    int LAST_TRADE_PERIOD = 4;          // за какой период брать цену (минут)
    float PROFIT_MARKUP = 0.005f;       // навар сделки


    Boolean DEBUG = true;
}
