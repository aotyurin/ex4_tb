package ru.ex4.apibt;

public interface IExConst {
    String currency1 = "RUB";
    String currency2 = "ETH";
    String PAIR = currency2 + "_" + currency1;               // пара

    float MIN_QUANTITY = 0.01f;     // min сумма ставки - берется из pair_settings
    int ORDER_LIFE = 3;         // время жизни ордера
    float STOCK_FEE = 0.002f;       // комиссия 0.2%
    int LAST_TRADE_PERIOD = 3;  // за какой период брать цену (минут)
    float PROFIT_MARKUP = 0.01f;     // навар сделки


    Boolean DEBUG = true;
}
