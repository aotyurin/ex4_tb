package ru.ex4.apibt.model;

public class OrderCreate {
    //      валютная пара
    private String pair;
    //    кол-во по ордеру
    private float quantity;
    //    цена по ордеру (страйк)
    private float price;
    //    тип ордера
    private TypeOrder type;


    public OrderCreate(String pair, float quantity, float price, TypeOrder type) {
        this.pair = pair;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }


    public String getPair() {
        return pair;
    }

    public float getPrice() {
        return price;
    }

    public float getQuantity() {
        return quantity;
    }

    public TypeOrder getType() {
        return type;
    }
}
