package com.amcglynn.myenergi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MyEnergiClientTest {

    @Disabled("Disabled as this requires a real serial number and API key")
    @Test
    void getZappiSummary() {
        var client = new MyEnergiClient("insertserialnumberhere", "insertapikeyhere");
        var zappis = client.getZappiStatus().getZappi()
                .stream().map(ZappiStatusSummary::new).collect(Collectors.toList());
        assertThat(zappis).isNotNull().hasSize(1);
    }
}
