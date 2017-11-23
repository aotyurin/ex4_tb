package ru.ex4.apibt.dto;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class OpenOrderDto {
    private String orderId;
    private String pair;
    private TypeOrder type;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal amount;
    private Date created;

    public OpenOrderDto(String orderId, String pair, TypeOrder type, BigDecimal price, BigDecimal quantity, BigDecimal amount, Date created) {
        this.orderId = orderId;
        this.pair = pair;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
        this.created = created;
    }

    public String getOrderId() {
        return orderId;
    }

    public StringProperty createdProperty() {
        return new SimpleStringProperty(DateUtil.format(created));
    }

    public StringProperty typeProperty() {
        return new SimpleStringProperty(type.name());
    }

    public FloatProperty priceProperty() {
        return new SimpleFloatProperty(price.floatValue());
    }

    public FloatProperty quantityProperty() {
        return new SimpleFloatProperty(quantity.floatValue());
    }

    public FloatProperty amountProperty() {
        return new SimpleFloatProperty(amount.floatValue());
    }
}
