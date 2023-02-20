package com.amcglynn.myenergi;

import com.amcglynn.myenergi.apiresponse.ZappiHistory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ZappiDaySummaryTest {

    @Test
    void testZappiDaySummary() {
        var dataPoints = List.of(ZappiHistory.builder()
                        .gridExportJoules(1000000L)
                        .importedJoules(3600000L)
                        .solarGenerationJoules(6000000L)
                        .zappiBoostModeJoules(5400000L)
                        .zappiDivertedModeJoules(2200000L)
                        .build(),
                ZappiHistory.builder()
                        .gridExportJoules(800000L)
                        .importedJoules(3600000L)
                        .solarGenerationJoules(12000000L)
                        .zappiBoostModeJoules(5400000L)
                        .zappiDivertedModeJoules(12200000L)
                        .build());
        var summary = new ZappiDaySummary(dataPoints);
        assertThat(summary.getExported().getDouble()).isEqualTo(0.5);
        assertThat(summary.getImported().getDouble()).isEqualTo(2.0);
        assertThat(summary.getSolarGeneration().getDouble()).isEqualTo(5.0);
        assertThat(summary.getEvSummary().getDiverted().getDouble()).isEqualTo(4.0);
        assertThat(summary.getEvSummary().getBoost().getDouble()).isEqualTo(3.0);
        assertThat(summary.getEvSummary().getTotal().getDouble()).isEqualTo(7.0);
    }
}
