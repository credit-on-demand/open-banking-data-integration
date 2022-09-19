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
public class Company {
    private String cnpjNumber;
    private String name;
    private String urlComplementaryList;
    private List<PersonalLoan> personalLoans;
}
