package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;


public class TradeDto {
    //      валютная пара
    @JsonProperty("pair")
    private String pair;

    @JsonProperty("values")
    private List<Trade> trades;

    private TradeDto() {
    }

    public TradeDto(String pair, List<Trade> trades) {
        this.pair = pair;
        this.trades = trades;
    }

    public String getPair() {
        return pair;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public static class Trade {
        //        идентификатор сделки
        @JsonProperty("trade_id")
        private Long tradeId;
        //        тип сделки
        @JsonProperty("type")
        private TypeOrder type;
        //        цена сделки
        @JsonProperty("price")
        private float price;
        //        кол-во по сделке
        @JsonProperty("quantity")
        private float quantity;
        //        сумма сделки
        @JsonProperty("amount")
        private float amount;
        //        дата и время сделки
        private Date date;

        @JsonProperty("date")
        private void setDate(Long date) {
            this.date = new Date(date * 1000L);
        }

        protected Trade() {
        }

        public Trade(float amount, Date date, float price, float quantity, Long tradeId, TypeOrder type) {
            this.amount = amount;
            this.date = date;
            this.price = price;
            this.quantity = quantity;
            this.tradeId = tradeId;
            this.type = type;
        }


        public float getAmount() {
            return amount;
        }

        public Date getDate() {
            return date;
        }

        public float getPrice() {
            return price;
        }

        public float getQuantity() {
            return quantity;
        }

        public Long getTradeId() {
            return tradeId;
        }

        public TypeOrder getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Trade{" +
                    "tradeId=" + tradeId +
                    ", type=" + type +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    ", amount=" + amount +
                    ", date=" + date +
                    '}';
        }
    }


}
