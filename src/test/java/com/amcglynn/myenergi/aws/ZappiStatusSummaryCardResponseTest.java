package com.amcglynn.myenergi.aws;

import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.apiresponse.ZappiStatus;
import com.amcglynn.myenergi.aws.responses.ZappiStatusSummaryCardResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ZappiStatusSummaryCardResponseTest {

    @Test
    void testGetCardSummary() {
        var summary = new ZappiStatusSummary(zappiStatus().build());
        var card = new ZappiStatusSummaryCardResponse(summary);
        assertThat(card).hasToString("Solar generation is 3.5 KiloWatts.\nCharge mode is FAST\n"
                + "Charge added this session is 0.0 KiloWatt Hours\n");
    }

    private ZappiStatus.ZappiStatusBuilder zappiStatus() {
        return ZappiStatus.builder()
                .gridWatts(-1500L)   // exporting 1.5kW to grid
                .zappiChargeMode(ZappiChargeMode.FAST.getApiValue())
                .chargeAddedThisSessionKwh(0.0)
                .solarGeneration(3500L);
    }
}
