package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class PairSettingDto {
    //      валюта
    @JsonProperty("currency")
    private String currency;
    //    минимальное кол-во по ордеру
    @JsonProperty("min_quantity")
    private float minQuantity;
    //    максимальное кол-во по ордеру
    @JsonProperty("max_quantity")
    private float maxQuantity;
    //    минимальная цена по ордеру
    @JsonProperty("min_price")
    private float minPrice;
    //    максимальная цена по ордеру
    @JsonProperty("max_price")
    private float maxPrice;
    //    минимальная сумма по ордеру
    @JsonProperty("min_amount")
    private float minAmount;
    //    максимальная сумма по ордеру
    @JsonProperty("max_amount")
    private float maxAmount;


    private PairSettingDto() {
    }



    public String getCurrency() {
        return currency;
    }

    public float getMinQuantity() {
        return minQuantity;
    }

    public float getMaxQuantity() {
        return maxQuantity;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public float getMinAmount() {
        return minAmount;
    }

    public float getMaxAmount() {
        return maxAmount;
    }


    @Override
    public String toString() {
        return "PairSettingDto{" +
                "currency='" + currency + '\'' +
                ", minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                '}';
    }
}
