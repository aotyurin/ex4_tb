package ru.ex4.apibt.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderBook {
    @JsonIgnore
    private ObjectMapper objectMapper = new ObjectMapper();

    //      валютная пара
    @JsonProperty("pair")
    private String pair;
    //        объем всех ордеров на продажу
    @JsonProperty("ask_quantity")
    private String ask_quantity;
    //        сумма всех ордеров на продажу
    @JsonProperty("ask_amount")
    private String ask_amount;
    //        минимальная цена продажи
    @JsonProperty("ask_top")
    private String ask_top;
    //        объем всех ордеров на покупку
    @JsonProperty("bid_quantity")
    private String bid_quantity;
    //        сумма всех ордеров на покупку
    @JsonProperty("bid_amount")
    private String bid_amount;
    //        максимальная цена покупки
    @JsonProperty("bid_top")
    private String bid_top;
    //        список ордеров на покупку, где каждая строка это цена, количество и сумма
    private ArrayList<OrderValue> bid;
    //        список ордеров на продажу, где каждая строка это цена, количество и сумма
    private ArrayList<OrderValue> ask;


    @JsonProperty("ask")
    private void setAsk(ArrayList<Object> ask) throws IOException {
        this.ask = new ArrayList<>();
        convertObjectToOrderValue(this.ask, ask);
    }

    @JsonProperty("bid")
    private void setBid(ArrayList<Object> bid) throws IOException {
        this.bid = new ArrayList<>();
        convertObjectToOrderValue(this.bid, bid);
    }

    private void convertObjectToOrderValue(ArrayList<OrderValue> thisOrderValues, ArrayList<Object> arrayList) throws IOException {
        if (arrayList != null) {
            for (Object o : arrayList) {
                List list = objectMapper.readValue(o.toString(), List.class);
                if (list != null && list.size() == 3) {
                    thisOrderValues.add(new OrderValue(list.get(0).toString(), list.get(1).toString(), list.get(2).toString()));
                }
            }
        }
    }


    private OrderBook() {
    }


    public class OrderValue {
        private String price;
        private String quantity;
        private String amount;

        public OrderValue() {
        }

        public OrderValue(String amount, String price, String quantity) {
            this.amount = amount;
            this.price = price;
            this.quantity = quantity;
        }
    }

}
