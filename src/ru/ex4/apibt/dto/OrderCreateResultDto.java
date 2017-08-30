package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class OrderCreateResultDto {
    //    идентификатор ордера
    @JsonProperty("order_id")
    private String orderId;
    //    успех или ошибка
    @JsonProperty("result")
    private Boolean result;
    //    содержит текст ошибки
    @JsonProperty("error")
    private String error;


    private OrderCreateResultDto() {
    }

    public OrderCreateResultDto(String error, String orderId, Boolean result) {
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
        return "OrderCreateResultDto{" +
                "orderId='" + orderId + '\'' +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
