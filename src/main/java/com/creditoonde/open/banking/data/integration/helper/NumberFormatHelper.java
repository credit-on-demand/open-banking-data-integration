package com.creditoonde.open.banking.data.integration.helper;

import java.math.BigDecimal;

public class NumberFormatHelper {

    private NumberFormatHelper() {
    }

    public static Double formatToDouble(String value) {
        BigDecimal bdValue = new BigDecimal(value);
        return bdValue.multiply(new BigDecimal(100)).doubleValue();
    }
}
