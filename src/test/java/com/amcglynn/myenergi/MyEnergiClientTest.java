package com.amcglynn.myenergi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MyEnergiClientTest {
    private final String serialNumber = "insertserialnumberhere";
    private final String apiKey = "insertapikeyhere";

    @Disabled("Disabled as this requires a real serial number and API key")
    @Test
    void getZappiSummary() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        var zappis = client.getZappiStatus().getZappi()
                .stream().map(ZappiStatusSummary::new).collect(Collectors.toList());
        assertThat(zappis).isNotNull().hasSize(1);
    }

    @Disabled("Disabled as this requires a real serial number and API key")
    @Test
    void setZappiChargeMode() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        client.setZappiChargeMode(ZappiChargeMode.ECO_PLUS);
    }

    @Disabled("Disabled as this requires a real serial number and API key")
    @Test
    void getZappiHourlyHistory() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        var date = LocalDate.now().minus(1, ChronoUnit.DAYS);
        var hourlyHistory = client.getZappiHourlyHistory(date);
        assertThat(hourlyHistory.getReadings()).hasSize(24);
        var summary = new ZappiDaySummary(hourlyHistory.getReadings());
        assertThat(summary.getEvSummary()).isNotNull();
    }

    @Disabled("Disabled as this requires a real serial number and API key")
    @Test
    void getZappiHistory() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        var date = LocalDate.now().minus(1, ChronoUnit.DAYS);
        var hourlyHistory = client.getZappiHistory(date);
        var summary = new ZappiDaySummary(hourlyHistory.getReadings());
        assertThat(summary.getEvSummary()).isNotNull();
    }
}
