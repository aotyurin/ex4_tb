package ru.ex4.apibt.model.custom;

import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.model.UserOpenOrder;

import java.math.BigDecimal;
import java.util.Date;

public class LossOrder extends UserOpenOrder.UserOpenOrderValue {

    private BigDecimal priceStep;
    private BigDecimal priceLoss;

    public LossOrder(String pair, BigDecimal price, BigDecimal quantity, TypeOrder type, BigDecimal priceStep, BigDecimal priceLoss, Date created) {
        super(null, pair, type, price, quantity, BigDecimal.ZERO, created);
        this.priceStep = priceStep;
        this.priceLoss = priceLoss;
    }

    public BigDecimal getPriceStep() {
        return priceStep;
    }

    public BigDecimal getPriceLoss() {
        return priceLoss;
    }
}
