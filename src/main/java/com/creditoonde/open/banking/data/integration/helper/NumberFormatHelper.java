package com.creditoonde.open.banking.data.integration.helper;

import java.math.BigDecimal;

public class NumberFormatHelper {

    private NumberFormatHelper() {
    }

    public static Double formatToDouble(String value) {
        if (value == null) {
            throw new NumberFormatException("Value cannot be null.");
        }
        BigDecimal bdValue = new BigDecimal(value);
        if (bdValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NumberFormatException("Value is invalid.");
        }
        return bdValue.multiply(new BigDecimal(100)).doubleValue();
    }
}
