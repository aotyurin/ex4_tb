package ru.ex4.apibt.dto;

import ru.ex4.apibt.model.TypeOrder;

import java.math.BigDecimal;
import java.util.Date;

public class LossOrderDto extends OpenOrderDto {
    private BigDecimal priceLoss;

    public LossOrderDto(String pair, BigDecimal quantity, BigDecimal price, TypeOrder type, BigDecimal priceLoss) {
        super(null, pair, type, price, quantity, BigDecimal.ZERO, new Date());
        this.priceLoss = priceLoss;
    }

    public BigDecimal getPriceLoss() {
        return priceLoss;
    }
}
