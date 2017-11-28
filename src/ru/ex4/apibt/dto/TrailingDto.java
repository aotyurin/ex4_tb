package ru.ex4.apibt.dto;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.ex4.apibt.model.TrendType;
import ru.ex4.apibt.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class TrailingDto {
    private String pair;
    private TrendType trendType;
    private BigDecimal price;
    private Date dateCreated;
    private Date dateNotify;

    public TrailingDto(String pair, TrendType trendType, BigDecimal price, Date dateCreated, Date dateNotify) {
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

    public StringProperty pairProperty() {
        return new SimpleStringProperty(pair);
    }

    public StringProperty trendTypeProperty() {
        return new SimpleStringProperty(trendType.name());
    }

    public FloatProperty priceProperty() {
        return new SimpleFloatProperty(price.floatValue());
    }

    public StringProperty dateCreatedProperty() {
        return new SimpleStringProperty(DateUtil.format(dateCreated));
    }

    public StringProperty dateNotifyProperty() {
        return new SimpleStringProperty(DateUtil.format(dateNotify));
    }
}
