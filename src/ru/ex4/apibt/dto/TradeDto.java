package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;


public class TradeDto {
    //      валюта
    @JsonProperty("currency")
    private String currency;

    @JsonProperty("values")
    public List<Trade> trades;

    private TradeDto() {
    }

    public TradeDto(String currency, List<Trade> trades) {
        this.currency = currency;
        this.trades = trades;
    }


    public static class Trade {
        //        идентификатор сделки
        @JsonProperty("trade_id")
        private Long tradeId;
        //        тип сделки
        @JsonProperty("type")
        private String type;
        //        цена сделки
        @JsonProperty("price")
        private String price;
        //        кол-во по сделке
        @JsonProperty("quantity")
        private String quantity;
        //        сумма сделки
        @JsonProperty("amount")
        private String amount;
        //        дата и время сделки
        private Date date;

        @JsonProperty("date")
        private void setDate(Long date) {
            this.date = new Date(date * 1000L);
        }

        private Trade() {
        }

        public Trade(String amount, Date date, String price, String quantity, Long tradeId, String type) {
            this.amount = amount;
            this.date = date;
            this.price = price;
            this.quantity = quantity;
            this.tradeId = tradeId;
            this.type = type;
        }


        public String getAmount() {
            return amount;
        }

        public Date getDate() {
            return date;
        }

        public String getPrice() {
            return price;
        }

        public String getQuantity() {
            return quantity;
        }

        public Long getTradeId() {
            return tradeId;
        }

        public String getType() {
            return type;
        }
    }


}
