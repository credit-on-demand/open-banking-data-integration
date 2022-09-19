package com.creditoonde.open.banking.data.integration.service;

import com.creditoonde.open.banking.data.integration.dto.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IntegrationServiceTest {

    @Autowired
    private IntegrationService integrationService;

    @Test
    void shouldLoadIntegrationData() {
        Brand brand = integrationService.retrievePersonalLoansData();

        System.out.println(brand.getName());
        Assertions.assertEquals("Itaú", brand.getName());
    }

}