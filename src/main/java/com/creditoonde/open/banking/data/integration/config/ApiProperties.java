package com.creditoonde.open.banking.data.integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private String simulationUrl;
    private List<String> externalUrls;

    public String getSimulationUrl() {
        return simulationUrl;
    }

    public void setSimulationUrl(String simulationUrl) {
        this.simulationUrl = simulationUrl;
    }

    public List<String> getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(List<String> externalUrls) {
        this.externalUrls = externalUrls;
    }
}
