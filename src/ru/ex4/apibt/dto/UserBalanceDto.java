package ru.ex4.apibt.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.ex4.apibt.util.DecimalUtil;

import java.math.BigDecimal;

public class UserBalanceDto {
    private String currency;
    private BigDecimal amountBalance;
    private BigDecimal amountReserved;

    public UserBalanceDto(String currency, BigDecimal amountBalance, BigDecimal amountReserved) {
        this.currency = currency;
        this.amountBalance = amountBalance;
        this.amountReserved = amountReserved;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmountBalance() {
        return DecimalUtil.round(amountBalance);
    }

    public BigDecimal getAmountReserved() {
        return DecimalUtil.round(amountReserved);
    }

    public StringProperty currencyProperty() {
        return new SimpleStringProperty(getCurrency());
    }

    public StringProperty amountBalanceProperty() {
        return new SimpleStringProperty(getAmountBalance().toPlainString());
    }

    public StringProperty amountReservedProperty() {
        return new SimpleStringProperty(getAmountReserved().toPlainString());
    }


}
