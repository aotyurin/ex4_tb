package ru.ex4.apibt.model.custom;

import ru.ex4.apibt.model.TrendType;

import java.math.BigDecimal;
import java.util.Date;

public class Trailing {
    private String pair;
    private TrendType trendType;
    private BigDecimal price;
    private Date dateCreated;
    private Date dateNotify;


    public Trailing(String pair, TrendType trendType, BigDecimal price, Date dateCreated, Date dateNotify) {
        this.pair = pair;
        this.trendType = trendType;
        this.price = price;
        this.dateCreated = dateCreated;
        this.dateNotify = dateNotify;
    }

    public String getPair() {
        return pair;
    }

    public TrendType getTrendType() {
        return trendType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateNotify() {
        return dateNotify;
    }
}
