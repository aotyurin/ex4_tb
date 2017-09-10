package ru.ex4.apibt;

public interface IExConst {
    String CURRENCY_QUOTED = "RUB";                         // котируемая
    String CURRENCY_BASE = "ETH";                           // базовая
    String PAIR = CURRENCY_BASE + "_" + CURRENCY_QUOTED;    // пара

    int ORDER_LIFE = 5;         // время жизни ордера (минут)
    float STOCK_FEE = 0.002f;       // комиссия 0.2%
    int LAST_TRADE_PERIOD = 4;  // за какой период брать цену (минут)
    float PROFIT_MARKUP = 0.01f;     // навар сделки


    Boolean DEBUG = true;
}
