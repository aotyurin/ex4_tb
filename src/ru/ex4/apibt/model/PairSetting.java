package ru.ex4.apibt.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class PairSetting {
    //      валютная пара
    @JsonProperty("pair")
    private String pair;
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


    private PairSetting() {
    }

    public PairSetting(String pair, float minQuantity, float maxQuantity, float minPrice, float maxPrice, float minAmount, float maxAmount) {
        this.pair = pair;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public String getPair() {
        return pair;
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
        return "PairSetting{" +
                "pair='" + pair + '\'' +
                ", minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                '}';
    }
}
