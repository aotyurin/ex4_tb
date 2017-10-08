package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;


public class UserTradeDto {
    //      валютная пара
    @JsonProperty("pair")
    private String pair;

    @JsonProperty("values")
    private List<UserTrade> trades;

    private UserTradeDto() {
    }

    public UserTradeDto(String pair, List<UserTrade> trades) {
        this.pair = pair;
        this.trades = trades;
    }

    public String getPair() {
        return pair;
    }

    public List<UserTrade> getTrades() {
        return trades;
    }


    public static class UserTrade extends TradeDto.Trade{
        //        идентификатор ордера пользователя
        @JsonProperty("order_id")
        private String orderId;
        //        валютная пара
        @JsonProperty("pair")
        private String pair;

        private UserTrade() {
            super();
        }

        public UserTrade(String orderId, String pair, float amount, Date date, float price, float quantity, Long tradeId, TypeOrder type) {
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
            return "UserTrade{" +
                    "orderId=" + orderId +
                    ", pair='" + pair + '\'' + super.toString() +
                    '}';
        }
    }


}
