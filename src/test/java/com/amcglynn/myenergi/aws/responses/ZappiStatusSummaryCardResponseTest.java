package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ChargeStatus;
import com.amcglynn.myenergi.EvConnectionStatus;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.apiresponse.ZappiStatus;
import com.amcglynn.myenergi.aws.responses.ZappiStatusSummaryCardResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ZappiStatusSummaryCardResponseTest {

    @Test
    void testVoiceResponseReturnsChargeMode() {
        var zappiStatus = zappiStatusBuilder().build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Charge mode: Eco+\n");
    }

    @Test
    void testSolarGenerationIsSet() {
        var zappiStatus = zappiStatusBuilder().solarGeneration(1500L).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Solar: 1.5kW\nCharge mode: Eco+\n");
    }

    @Test
    void testGridImport() {
        var zappiStatus = zappiStatusBuilder().gridWatts(2500L).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Import: 2.5kW\nCharge mode: Eco+\n");
    }

    @Test
    void testGridExport() {
        var zappiStatus = zappiStatusBuilder().gridWatts(-4500L).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Export: 4.5kW\nCharge mode: Eco+\n");
    }

    @Test
    void testChargeAdded() {
        var zappiStatus = zappiStatusBuilder().chargeAddedThisSessionKwh(45.3).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Charge mode: Eco+\nCharge added: 45.3kWh\n");
    }

    @Test
    void testChargeAddedBoostModeEnabled() {
        var zappiStatus = zappiStatusBuilder()
                .chargeStatus(ChargeStatus.BOOSTING.ordinal())
                .chargeAddedThisSessionKwh(45.3).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Charge mode: Eco+\nBoost mode: enabled\nCharge added: 45.3kWh\n");
    }

    @Test
    void testCharging() {
        var zappiStatus = zappiStatusBuilder()
                .carDiversionAmountWatts(7000L)
                .evConnectionStatus(EvConnectionStatus.CHARGING.getCode())
                .chargeAddedThisSessionKwh(12.5)
                .build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Charge rate: 7.0kW\n" +
                "Charge mode: Eco+\n" +
                "Charge added: 12.5kWh\n");
    }

    @Test
    void testCardResponse() {
        var zappiStatus = zappiStatusBuilder()
                .solarGeneration(1500L)
                .gridWatts(5500L)
                .carDiversionAmountWatts(7000L)
                .evConnectionStatus(EvConnectionStatus.CHARGING.getCode())
                .zappiChargeMode(ZappiChargeMode.FAST.getApiValue())
                .chargeAddedThisSessionKwh(20.2)
                .build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryCardResponse(summary);
        assertThat(response).hasToString("Solar: 1.5kW\nImport: 5.5kW\n" +
                "Charge rate: 7.0kW\nCharge mode: Fast\n" +
                "Charge added: 20.2kWh\n");
    }

    private ZappiStatus.ZappiStatusBuilder zappiStatusBuilder() {
        return ZappiStatus.builder()
                .solarGeneration(0L)
                .gridWatts(0L)
                .zappiChargeMode(ZappiChargeMode.ECO_PLUS.getApiValue())
                .carDiversionAmountWatts(0L)
                .evConnectionStatus(EvConnectionStatus.EV_DISCONNECTED.toString())
                .chargeAddedThisSessionKwh(0.0);
    }
}
