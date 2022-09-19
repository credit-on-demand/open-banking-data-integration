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
public class InterestRate {
    private String referentialRateIndexer;
    private String rate;
    private List<Application> applications;
    private String minimumRate;
    private String maximumRate;
}
