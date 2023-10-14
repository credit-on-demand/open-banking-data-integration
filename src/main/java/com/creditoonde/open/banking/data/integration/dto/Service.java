package com.creditoonde.open.banking.data.integration.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Service {
    private String name;
    private String code;
    private String chargingTriggerInfo;
    private List<Price> prices;
    private ValueCurrency minimum;
    private ValueCurrency maximum;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    class ValueCurrency {
        private String value;
        private String currency;
    }
}