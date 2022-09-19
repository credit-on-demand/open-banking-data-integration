package com.creditoonde.open.banking.data.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalLoan {
    private String type;
    private Fee fees;
    private List<InterestRate> interestRates;
    private List<String> requiredWarranties;
    private String termsConditions;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class Fee {
        private List<Service> services;
    }
}
