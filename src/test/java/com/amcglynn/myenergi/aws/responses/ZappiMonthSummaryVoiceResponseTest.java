package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ZappiMonthSummary;
import com.amcglynn.myenergi.units.KiloWattHour;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZappiMonthSummaryVoiceResponseTest {

    @Test
    void testCardResponseReturnsChargeMode() {
        var zappiMonthSummary = mock(ZappiMonthSummary.class);
        when(zappiMonthSummary.getYearMonth()).thenReturn(YearMonth.of(2022, 2));
        when(zappiMonthSummary.getExported()).thenReturn(new KiloWattHour(50));
        when(zappiMonthSummary.getImported()).thenReturn(new KiloWattHour(150));
        when(zappiMonthSummary.getEvTotal()).thenReturn(new KiloWattHour(200));
        when(zappiMonthSummary.getSolarGeneration()).thenReturn(new KiloWattHour(13));

        var response = new ZappiMonthSummaryVoiceResponse(zappiMonthSummary);
        assertThat(response).hasToString("Here's your usage for February 2022. " +
                "Imported 150.0 kilowatt hours. " +
                "Exported 50.0 kilowatt hours. " +
                "Solar generation was 13.0 kilowatt hours. " +
                "Charged 200.0 kilowatt hours to your E.V.");
    }
}
