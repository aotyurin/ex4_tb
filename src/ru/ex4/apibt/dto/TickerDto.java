package ru.ex4.apibt.dto;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;

public class TickerDto {
    //      валютная пара
    private String pair;
    //    текущая максимальная цена покупки
    private BigDecimal buyPrice;
    //    текущая минимальная цена продажи
    private BigDecimal sellPrice;
    //    цена последней сделки
    private BigDecimal lastTrade;
    //    максимальная цена сделки за 24 часа
    private float high;
    //    минимальная цена сделки за 24 часа
    private float low;
    //    средняя цена сделки за 24 часа
    private float avg;
    //    объем всех сделок за 24 часа
    private float vol;
    //    сумма всех сделок за 24 часа
    private float volCurr;

    public TickerDto(String pair, BigDecimal buyPrice, BigDecimal sellPrice, BigDecimal lastTrade, float high, float low, float avg, float vol, float volCurr) {
        this.pair = pair;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.lastTrade = lastTrade;
        this.high = high;
        this.low = low;
        this.avg = avg;
        this.vol = vol;
        this.volCurr = volCurr;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public StringProperty pairProperty() {
        return new SimpleStringProperty(pair);
    }

    public String getPair() {
        return pair;
    }

    public FloatProperty buyPriceProperty() {
        return new SimpleFloatProperty(buyPrice.floatValue());
    }

    public FloatProperty sellPriceProperty() {
        return new SimpleFloatProperty(sellPrice.floatValue());
    }

    public FloatProperty volatPresentProperty() {
        float v = (high - low) / low * 100;
        return new SimpleFloatProperty(v);
    }

    public FloatProperty changePricePresentProperty() {
        float v = (sellPrice.floatValue() - avg) / avg * 100;
        return new SimpleFloatProperty(v);
    }

    public BooleanProperty changePriceFactorProperty() {
        return new SimpleBooleanProperty(avg < sellPrice.floatValue());
    }

    public StringProperty volumeProperty() {
        return new SimpleStringProperty(vol + "(" + volCurr + ")");
    }

    public FloatProperty highTradeProperty() {
        return new SimpleFloatProperty(high);
    }

    public FloatProperty lastTradeProperty() {
        return new SimpleFloatProperty(lastTrade.floatValue());
    }

    public FloatProperty lowTradeProperty() {
        return new SimpleFloatProperty(low);
    }

    public FloatProperty avgTradeProperty() {
        return new SimpleFloatProperty(avg);
    }
}
