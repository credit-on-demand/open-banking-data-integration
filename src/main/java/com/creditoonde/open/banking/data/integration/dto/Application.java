package com.creditoonde.open.banking.data.integration.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Application {
    private String interval;
    private SingleRate indexer;
    private SingleRate customers;
}
