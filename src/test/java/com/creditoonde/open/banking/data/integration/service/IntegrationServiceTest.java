package com.creditoonde.open.banking.data.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IntegrationServiceTest {

    @Autowired
    private IntegrationService integrationService;

    @Test
    void shouldLoadIntegrationData() {
        String itauData = integrationService.retrievePersonalLoansData();

        assert itauData.contains("EMPRESTIMO_CREDITO_PESSOAL_SEM_CONSIGNACAO");
    }

}