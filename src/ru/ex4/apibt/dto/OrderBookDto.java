package ru.ex4.apibt.dto;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

import java.math.BigDecimal;
import java.util.List;

public class OrderBookDto {
    private String pair;
    //  продажа
    private List<Ask> askList;
    //  покупка
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
        public Ask(BigDecimal price, BigDecimal quantity, BigDecimal amount) {
            super(price, quantity, amount);
        }
    }

    public class Bid extends OrderBookValue {
        public Bid(BigDecimal price, BigDecimal quantity, BigDecimal amount) {
            super(price, quantity, amount);
        }
    }



    private class OrderBookValue {
        //  Цена
        private BigDecimal price;
        //  Количество
        private BigDecimal quantity;
        //  Сумма
        private BigDecimal amount;

        public OrderBookValue(BigDecimal price, BigDecimal quantity, BigDecimal amount) {
            this.price = price;
            this.quantity = quantity;
            this.amount = amount;
        }

        public FloatProperty priceProperty() {
            return new SimpleFloatProperty(price.floatValue());
        }

        public FloatProperty quantityProperty() {
            return new SimpleFloatProperty(quantity.floatValue());
        }

        public FloatProperty amountProperty() {
            return new SimpleFloatProperty(amount.floatValue());
        }
    }


}
