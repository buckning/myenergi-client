package com.amcglynn.myenergi;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MyEnergiClientIntegrationTest {
    private final String serialNumber = System.getenv("myEnergiHubSerialNumber");
    private final String apiKey = System.getenv("myEnergiHubApiKey");

    @BeforeEach
    void setUp() {
        Assumptions.assumeTrue(() -> apiKey != null, "API key must not be null. Please define myEnergiHubApiKey environment variable.");
        Assumptions.assumeTrue(() -> serialNumber != null, "Serial number must not be null. Please define myEnergiHubSerialNumber environment variable.");
    }

    @Test
    void getZappiSummary() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        var zappis = client.getZappiStatus().getZappi()
                .stream().map(ZappiStatusSummary::new).collect(Collectors.toList());
        assertThat(zappis).isNotNull().hasSize(1);
    }

    @Test
    void setZappiChargeMode() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        client.setZappiChargeMode(ZappiChargeMode.ECO_PLUS);
    }

    @Test
    void getZappiHourlyHistory() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        var date = LocalDate.now().minus(1, ChronoUnit.DAYS);
        var hourlyHistory = client.getZappiHourlyHistory(date);
        assertThat(hourlyHistory.getReadings()).hasSize(24);
        var summary = new ZappiDaySummary(hourlyHistory.getReadings());
        assertThat(summary.getEvSummary()).isNotNull();
    }

    @Test
    void getZappiHistory() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        var date = LocalDate.now().minus(3, ChronoUnit.DAYS);
        var hourlyHistory = client.getZappiHistory(date);
        var summary = new ZappiDaySummary(hourlyHistory.getReadings());
        assertThat(summary.getEvSummary()).isNotNull();
    }
}
