package ru.ex4.apibt.dto;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ObservableValue;
import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.util.DecimalUtil;
import ru.ex4.apibt.view.fxmlManager.IFxmlDto;

import java.math.BigDecimal;
import java.util.Date;

public class LossOrderDto extends OpenOrderDto implements IFxmlDto {
    private BigDecimal priceStep;
    private BigDecimal priceLoss;

    public LossOrderDto(String pair, BigDecimal quantity, BigDecimal price, TypeOrder type, BigDecimal priceStep, BigDecimal priceLoss) {
        super(null, pair, type, price, quantity, BigDecimal.ZERO, new Date());
        this.priceStep = priceStep;
        this.priceLoss = priceLoss;
    }

    public BigDecimal getPriceStep() {
        return priceStep;
    }

    public BigDecimal getPriceLoss() {
        return priceLoss;
    }

    public FloatProperty priceStepProperty() {
        return new SimpleFloatProperty(priceStep.floatValue());
    }

    public FloatProperty priceLossProperty() {
        return new SimpleFloatProperty(priceLoss.floatValue());
    }


}
