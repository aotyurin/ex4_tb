package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class TickerDto {
    //      валюта
    @JsonProperty("currency")
    private String currency;
    //    текущая максимальная цена покупки
    @JsonProperty("buy_price")
    private Float buyPrice;
    //    текущая минимальная цена продажи
    @JsonProperty("sell_price")
    private Float sellPrice;
    //    цена последней сделки
    @JsonProperty("last_trade")
    private Float lastTrade;
    //    максимальная цена сделки за 24 часа
    @JsonProperty("high")
    private Float high;
    //    минимальная цена сделки за 24 часа
    @JsonProperty("low")
    private Float low;
    //    средняя цена сделки за 24 часа
    @JsonProperty("avg")
    private Float avg;
    //    объем всех сделок за 24 часа
    @JsonProperty("vol")
    private Float vol;
    //    сумма всех сделок за 24 часа
    @JsonProperty("vol_curr")
    private Float volCurr;
    //    дата и время обновления данных
    private Date updated;

    @JsonProperty("updated") //в формате Unix
    private void setUpdated(Long updated) {
        this.updated = new Date(updated * 1000L);
    }


    private TickerDto() {
    }


    public Float getAvg() {
        return avg;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public Float getHigh() {
        return high;
    }

    public Float getLastTrade() {
        return lastTrade;
    }

    public Float getLow() {
        return low;
    }

    public Float getSellPrice() {
        return sellPrice;
    }

    public Date getUpdated() {
        return updated;
    }

    public Float getVol() {
        return vol;
    }

    public Float getVolCurr() {
        return volCurr;
    }
}
