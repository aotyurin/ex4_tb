package ru.ex4.apibt.util;

import ru.ex4.apibt.IExConst;

import java.math.BigDecimal;

public class DecimalUtil {
    public static BigDecimal round(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return value.setScale(0, IExConst.ROUND_DECIMAL_METHOD);
        }
        return value.setScale(IExConst.SCALE_DECIMAL_METHOD, IExConst.ROUND_DECIMAL_METHOD);
    }

    public static BigDecimal parse(String text) {
        if (text == null || text.equals("")) {
            return round(BigDecimal.valueOf(0));
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            return BigDecimal.valueOf(0);
        }
    }



}
