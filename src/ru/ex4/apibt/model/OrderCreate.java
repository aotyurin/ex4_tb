package ru.ex4.apibt.model;

import java.math.BigDecimal;

public class OrderCreate {
    //      валютная пара
    private String pair;
    //    кол-во по ордеру
    private BigDecimal quantity;
    //    цена по ордеру (страйк)
    private BigDecimal price;
    //    тип ордера
    private TypeOrder type;


    public OrderCreate(String pair, BigDecimal quantity, BigDecimal price, TypeOrder type) {
        this.pair = pair;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }


    public String getPair() {
        return pair;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public TypeOrder getType() {
        return type;
    }
}
