package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ChargeStatus;
import com.amcglynn.myenergi.EvConnectionStatus;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.apiresponse.ZappiStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ZappiStatusSummaryVoiceResponseTest {
    @Test
    void testVoiceResponseReturnsChargeMode() {
        var zappiStatus = zappiStatusBuilder().build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Charge mode is Eco+. ");
    }

    @Test
    void testSolarGenerationIsSet() {
        var zappiStatus = zappiStatusBuilder().solarGeneration(1500L).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Solar generation is 1.5 kilowatts. Charge mode is Eco+. ");
    }

    @Test
    void testGridImport() {
        var zappiStatus = zappiStatusBuilder().gridWatts(2500L).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Importing 2.5 kilowatts. Charge mode is Eco+. ");
    }

    @Test
    void testGridExport() {
        var zappiStatus = zappiStatusBuilder().gridWatts(-4500L).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Exporting 4.5 kilowatts. Charge mode is Eco+. ");
    }

    @Test
    void testChargeAdded() {
        var zappiStatus = zappiStatusBuilder().chargeAddedThisSessionKwh(45.3).build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Charge mode is Eco+. Charge added this session is 45.3 kilowatt hours. ");
    }

    @Test
    void testCharging() {
        var zappiStatus = zappiStatusBuilder()
                .carDiversionAmountWatts(7000L)
                .evConnectionStatus(EvConnectionStatus.CHARGING.getCode())
                .chargeAddedThisSessionKwh(12.5)
                .build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Charging 7.0 kilowatts to your E.V. - Charge mode is Eco+. " +
                "Charge added this session is 12.5 kilowatt hours. ");
    }

    @Test
    void testVoiceResponse() {
        var zappiStatus = zappiStatusBuilder()
                .solarGeneration(1500L)
                .gridWatts(5500L)
                .carDiversionAmountWatts(7000L)
                .evConnectionStatus(EvConnectionStatus.CHARGING.getCode())
                .zappiChargeMode(ZappiChargeMode.FAST.getApiValue())
                .chargeAddedThisSessionKwh(20.2)
                .build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Solar generation is 1.5 kilowatts. Importing 5.5 kilowatts. " +
                "Charging 7.0 kilowatts to your E.V. - Charge mode is Fast. " +
                "Charge added this session is 20.2 kilowatt hours. ");
    }

    @Test
    void testChargeComplete() {
        var zappiStatus = zappiStatusBuilder()
                .evConnectionStatus(EvConnectionStatus.EV_CONNECTED.getCode())
                .zappiChargeMode(ZappiChargeMode.FAST.getApiValue())
                .chargeStatus(ChargeStatus.COMPLETE.ordinal())
                .chargeAddedThisSessionKwh(20.2)
                .build();
        var summary = new ZappiStatusSummary(zappiStatus);
        var response = new ZappiStatusSummaryVoiceResponse(summary);
        assertThat(response).hasToString("Charge mode is Fast. " +
                "Charging session is complete. Charge added this session is 20.2 kilowatt hours. ");
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
