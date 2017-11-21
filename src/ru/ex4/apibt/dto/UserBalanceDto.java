package ru.ex4.apibt.dto;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

public class UserBalanceDto {
    private StringProperty currency;
    private BigDecimal amountBalance;
    private BigDecimal amountReserved;

    public UserBalanceDto(String currency, BigDecimal amountBalance, BigDecimal amountReserved) {
        this.currency = new SimpleStringProperty(currency);
        this.amountBalance = amountBalance;
        this.amountReserved = amountReserved;
    }


    public StringProperty currencyProperty() {
        return currency;
    }

    public StringProperty amountBalanceProperty() {
        return new SimpleStringProperty(amountBalance.toPlainString());
    }

    public StringProperty amountReservedProperty() {
        return new SimpleStringProperty(amountReserved.toPlainString());
    }
}
