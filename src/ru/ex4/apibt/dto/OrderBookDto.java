package ru.ex4.apibt.dto;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

import java.math.BigDecimal;
import java.util.List;

public class OrderBookDto {
    private String pair;
    private List<Ask> askList;
    private List<Bid> bidList;

    public OrderBookDto() {
    }

    public OrderBookDto(String pair, List<Ask> askList, List<Bid> bidList) {
        this.pair = pair;
        this.askList = askList;
        this.bidList = bidList;
    }

    public String getPair() {
        return pair;
    }

    public List<Ask> getAskList() {
        return askList;
    }

    public List<Bid> getBidList() {
        return bidList;
    }


    public class Ask extends OrderBookValue {
        public Ask(BigDecimal amount, BigDecimal price, BigDecimal quantity) {
            super(amount, price, quantity);
        }
    }

    public class Bid extends OrderBookValue {
        public Bid(BigDecimal amount, BigDecimal price, BigDecimal quantity) {
            super(amount, price, quantity);
        }
    }



    private class OrderBookValue {
        private BigDecimal amount;
        private BigDecimal price;
        private BigDecimal quantity;

        public OrderBookValue(BigDecimal amount, BigDecimal price, BigDecimal quantity) {
            this.price = price;
            this.quantity = quantity;
            this.amount = amount;
        }

        public FloatProperty amountProperty() {
            return new SimpleFloatProperty(amount.floatValue());
        }

        public FloatProperty priceProperty() {
            return new SimpleFloatProperty(price.floatValue());
        }

        public FloatProperty quantityProperty() {
            return new SimpleFloatProperty(quantity.floatValue());
        }
    }


}
