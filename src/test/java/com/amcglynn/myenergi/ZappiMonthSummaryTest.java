package com.amcglynn.myenergi;

import com.amcglynn.myenergi.units.KiloWattHour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZappiMonthSummaryTest {

    @Mock
    private ZappiDaySummary dataPoint1;
    @Mock
    private ZappiDaySummary dataPoint2;

    @BeforeEach
    void setUp() {
        when(dataPoint1.getSolarGeneration()).thenReturn(new KiloWattHour(0.5));
        when(dataPoint2.getSolarGeneration()).thenReturn(new KiloWattHour(1.0));
        when(dataPoint1.getExported()).thenReturn(new KiloWattHour(0.5));
        when(dataPoint2.getExported()).thenReturn(new KiloWattHour(0.25));
        when(dataPoint1.getImported()).thenReturn(new KiloWattHour(10));
        when(dataPoint2.getImported()).thenReturn(new KiloWattHour(3.5));
        when(dataPoint1.getEvSummary()).thenReturn(new ZappiDaySummary.EvSummary(
                new KiloWattHour(5), new KiloWattHour(5), new KiloWattHour(10)));
        when(dataPoint2.getEvSummary()).thenReturn(new ZappiDaySummary.EvSummary(
                new KiloWattHour(15), new KiloWattHour(15), new KiloWattHour(30)));
    }

    @Test
    void testMonthSummary() {
        var summary = new ZappiMonthSummary(YearMonth.of(2022, 2), List.of(dataPoint1, dataPoint2));
        assertThat(summary.getSolarGeneration()).isEqualTo(new KiloWattHour(1.5));
        assertThat(summary.getExported()).isEqualTo(new KiloWattHour(0.75));
        assertThat(summary.getImported()).isEqualTo(new KiloWattHour(13.5));
        assertThat(summary.getEvTotal()).isEqualTo(new KiloWattHour(40));
        assertThat(summary.getYearMonth()).isEqualTo(YearMonth.of(2022, 2));
    }
}
