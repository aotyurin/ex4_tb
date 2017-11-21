package ru.ex4.apibt.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;


public class UserTrade {
    //      валютная пара
    @JsonProperty("pair")
    private String pair;

    @JsonProperty("values")
    private List<UserTradeValue> trades;

    private UserTrade() {
    }

    public UserTrade(String pair, List<UserTradeValue> trades) {
        this.pair = pair;
        this.trades = trades;
    }

    public String getPair() {
        return pair;
    }

    public List<UserTradeValue> getTrades() {
        return trades;
    }


    public static class UserTradeValue extends Trade.TradeValue {
        //        идентификатор ордера пользователя
        @JsonProperty("order_id")
        private String orderId;
        //        валютная пара
        @JsonProperty("pair")
        private String pair;

        private UserTradeValue() {
            super();
        }

        public UserTradeValue(String orderId, String pair, float amount, Date date, float price, float quantity, Long tradeId, TypeOrder type) {
            super(amount, date, price, quantity, tradeId, type);
            this.orderId = orderId;
            this.pair = pair;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getPair() {
            return pair;
        }

        @Override
        public String toString() {
            return "UserTradeValue{" +
                    "orderId=" + orderId +
                    ", pair='" + pair + '\'' + super.toString() +
                    '}';
        }
    }


}
