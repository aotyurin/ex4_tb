package ru.ex4.apibt.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class OrderCreateResult {
    //    идентификатор ордера
    @JsonProperty("order_id")
    private String orderId;
    //    успех или ошибка
    @JsonProperty("result")
    private Boolean result;
    //    содержит текст ошибки
    @JsonProperty("error")
    private String error;


    private OrderCreateResult() {
    }

    public OrderCreateResult(String error, String orderId, Boolean result) {
        this.error = error;
        this.orderId = orderId;
        this.result = result;
    }


    public String getError() {
        return error;
    }

    public String getOrderId() {
        return orderId;
    }

    public Boolean getResult() {
        return result;
    }


    @Override
    public String toString() {
        return "OrderCreateResult{" +
                "orderId='" + orderId + '\'' +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
