package ru.ex4.apibt.dto;

public class OrderBookDto {
    private String price;
    private String quantity;
    private String amount;


    public OrderBookDto(String amount, String price, String quantity) {
        this.amount = amount;
        this.price = price;
        this.quantity = quantity;
    }
}
