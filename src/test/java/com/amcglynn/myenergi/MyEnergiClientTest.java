package com.amcglynn.myenergi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        client.setZappiChargeMode(ZappiChargeMode.ECO_PLUS);
    }

    @Disabled("Disabled as this requires a real serial number and API key")
    @Test
    void setZappiChargeMode() {
        var client = new MyEnergiClient(serialNumber, apiKey);
        client.setZappiChargeMode(ZappiChargeMode.ECO_PLUS);
    }
}
