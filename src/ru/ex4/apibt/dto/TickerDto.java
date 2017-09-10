package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class TickerDto {
    //      валютная пара
    @JsonProperty("pair")
    private String pair;
    //    текущая максимальная цена покупки
    @JsonProperty("buy_price")
    private float buyPrice;
    //    текущая минимальная цена продажи
    @JsonProperty("sell_price")
    private float sellPrice;
    //    цена последней сделки
    @JsonProperty("last_trade")
    private float lastTrade;
    //    максимальная цена сделки за 24 часа
    @JsonProperty("high")
    private float high;
    //    минимальная цена сделки за 24 часа
    @JsonProperty("low")
    private float low;
    //    средняя цена сделки за 24 часа
    @JsonProperty("avg")
    private float avg;
    //    объем всех сделок за 24 часа
    @JsonProperty("vol")
    private float vol;
    //    сумма всех сделок за 24 часа
    @JsonProperty("vol_curr")
    private float volCurr;
    //    дата и время обновления данных
    private Date updated;

    @JsonProperty("updated") //в формате Unix
    private void setUpdated(Long updated) {
        this.updated = new Date(updated * 1000L);
    }


    private TickerDto() {
    }


    public float getAvg() {
        return avg;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public String getPair() {
        return pair;
    }

    public float getHigh() {
        return high;
    }

    public float getLastTrade() {
        return lastTrade;
    }

    public float getLow() {
        return low;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public Date getUpdated() {
        return updated;
    }

    public float getVol() {
        return vol;
    }

    public float getVolCurr() {
        return volCurr;
    }

    @Override
    public String toString() {
        return "TickerDto{" +
                "pair='" + pair + '\'' +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", lastTrade=" + lastTrade +
                ", high=" + high +
                ", low=" + low +
                ", avg=" + avg +
                ", vol=" + vol +
                ", volCurr=" + volCurr +
                ", updated=" + updated +
                '}';
    }
}
