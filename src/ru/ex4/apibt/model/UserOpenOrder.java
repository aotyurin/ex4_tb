package ru.ex4.apibt.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

public class UserOpenOrder {
    //  валютная пара
    @JsonProperty("pair")
    private String pair;

    @JsonProperty("values")
    private List<UserOpenOrderValue> openOrders;

    private UserOpenOrder() {
    }

    public UserOpenOrder(String pair, List<UserOpenOrderValue> openOrders) {
        this.pair = pair;
        this.openOrders = openOrders;
    }

    public String getPair() {
        return pair;
    }

    public List<UserOpenOrderValue> getOpenOrders() {
        return openOrders;
    }



    public static class UserOpenOrderValue {
        //  идентификатор ордера
        @JsonProperty("order_id")
        private String orderId;
        //  валютная пара
        @JsonProperty("pair")
        private String pair;
        //  тип сделки
        @JsonProperty("type")
        private TypeOrder type;
        //  цена по ордеру
        @JsonProperty("price")
        private float price;
        //  кол-во по ордеру
        @JsonProperty("quantity")
        private float quantity;
        //   сумма по ордеру
        @JsonProperty("amount")
        private float amount;
        //   дата и время создания ордера
        private Date created;

        @JsonProperty("created")
        private void setCreated(Long created) {
            this.created = new Date(created * 1000L);
        }

        private UserOpenOrderValue() {
        }

        public UserOpenOrderValue(String orderId, String pair, TypeOrder type, float price, float quantity, float amount, Date created) {
            this.orderId = orderId;
            this.pair = pair;
            this.type = type;
            this.price = price;
            this.quantity = quantity;
            this.amount = amount;
            this.created = created;
        }


        public String getOrderId() {
            return orderId;
        }

        public String getPair() {
            return pair;
        }

        public TypeOrder getType() {
            return type;
        }

        public float getPrice() {
            return price;
        }

        public float getQuantity() {
            return quantity;
        }

        public float getAmount() {
            return amount;
        }

        public Date getCreated() {
            return created;
        }


        @Override
        public String toString() {
            return "UserOpenOrderValue{" +
                    "orderId='" + orderId + '\'' +
                    ", pair='" + pair + '\'' +
                    ", type=" + type +
                    ", price='" + price + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", amount='" + amount + '\'' +
                    ", created=" + created +
                    '}';
        }
    }



}
