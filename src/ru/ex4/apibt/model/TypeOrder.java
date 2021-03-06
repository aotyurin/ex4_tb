package ru.ex4.apibt.model;

public enum TypeOrder {
    //    ордер на покупку
    buy,
    //    ордер на продажу
    sell,
    //    ордера на покупку по рынку
    market_buy,
    //    ордер на продажу по рынку
    market_sell,
    //    ордер на покупку по рынку на определенную сумму
    market_buy_total,
    //    ордер на продажу по рынку на определенную сумму
    market_sell_total,

    buy_loss,
    sell_loss
}
