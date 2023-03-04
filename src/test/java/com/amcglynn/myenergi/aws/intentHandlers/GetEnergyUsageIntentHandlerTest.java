package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Slot;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.ZappiDaySummary;
import com.amcglynn.myenergi.ZappiMonthSummary;
import com.amcglynn.myenergi.service.ZappiService;
import com.amcglynn.myenergi.units.KiloWattHour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySimpleCardInResponse;
import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySpeechInResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetEnergyUsageIntentHandlerTest {

    @Mock
    private ZappiService mockZappiService;
    private IntentRequest intentRequest;

    private GetEnergyUsageIntentHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetEnergyUsageIntentHandler(mockZappiService);
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("GetEnergyUsage").build())
                .build();
    }

    @Test
    void testCanHandleOnlyTriggersForTheIntent() {
        assertThat(handler.canHandle(handlerInputBuilder().build())).isTrue();
    }

    @Test
    void testCanHandleReturnsFalseWhenNotTheCorrectIntent() {
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("SetChargeMode").build())
                .build();
        assertThat(handler.canHandle(handlerInputBuilder().build())).isFalse();
    }

    @Test
    void testHandleYear() {
        initIntentRequest(Year.of(2023));
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Sorry, it would take me too long to crunch the numbers " +
                "for an entire year! Please provide a day or month</speak>");
        verifySimpleCardInResponse(result.get(), "My Zappi", "Sorry, it would take me too " +
                "long to crunch the numbers for an entire year! Please provide a day or month.");
        verify(mockZappiService).getEnergyUsage(Year.of(2023));
    }

    @Test
    void testHandleYearMonth() {
        initIntentRequest(YearMonth.of(2023, 2));

        var zappiMonthSummary = mock(ZappiMonthSummary.class);
        when(zappiMonthSummary.getYearMonth()).thenReturn(YearMonth.of(2022, 2));
        when(zappiMonthSummary.getExported()).thenReturn(new KiloWattHour(50));
        when(zappiMonthSummary.getImported()).thenReturn(new KiloWattHour(150));
        when(zappiMonthSummary.getEvTotal()).thenReturn(new KiloWattHour(200));
        when(zappiMonthSummary.getSolarGeneration()).thenReturn(new KiloWattHour(13));

        when(mockZappiService.getEnergyUsage(YearMonth.of(2023, 2))).thenReturn(zappiMonthSummary);
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Here's your usage for February 2022. " +
                "Imported 150.0 kilowatt hours. " +
                "Exported 50.0 kilowatt hours. " +
                "Solar generation was 13.0 kilowatt hours. " +
                "Charged 200.0 kilowatt hours to your E.V.</speak>");
        verifySimpleCardInResponse(result.get(), "My Zappi", "Usage for February 2022\n" +
                "Imported: 150.0kWh\n" +
                "Exported: 50.0kWh\n" +
                "Solar generated: 13.0kWh\n" +
                "Charged: 200.0kWh\n");
        verify(mockZappiService).getEnergyUsage(YearMonth.of(2023, 2));
    }

    @Test
    void testHandleLocalDate() {
        initIntentRequest(LocalDate.of(2023, 2, 20));
        var zappiDaySummary = mock(ZappiDaySummary.class);
        when(zappiDaySummary.getSampleSize()).thenReturn(1421);
        when(zappiDaySummary.getImported()).thenReturn(new KiloWattHour(7));
        when(zappiDaySummary.getExported()).thenReturn(new KiloWattHour(10));
        when(zappiDaySummary.getConsumed()).thenReturn(new KiloWattHour(8));
        when(zappiDaySummary.getSolarGeneration()).thenReturn(new KiloWattHour(5));
        when(zappiDaySummary.getEvSummary()).thenReturn(new ZappiDaySummary.EvSummary(new KiloWattHour(3),
                new KiloWattHour(4), new KiloWattHour(7)));

        when(mockZappiService.getEnergyUsage(any(LocalDate.class))).thenReturn(zappiDaySummary);
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Imported 7.0 kilowatt hours. Exported 10.0 kilowatt hours. " +
                "Consumed 8.0 kilowatt hours. Solar generation was 5.0 kilowatt hours. Charged 7.0 kilowatt hours to your E.V.</speak>");
        verifySimpleCardInResponse(result.get(), "My Zappi", "Imported: 7.0kWh\n" +
                "Exported: 10.0kWh\n" +
                "Consumed: 8.0kWh\n" +
                "Solar generated: 5.0kWh\n" +
                "Charged: 7.0kWh\n");
        verify(mockZappiService).getEnergyUsage(LocalDate.of(2023, 2, 20));
    }

    @Test
    void testHandleReturnsErrorMessageWhenDateIsNotProvided() {
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Please ask me for energy " +
                "usage for a specific day or month</speak>");
        verifySimpleCardInResponse(result.get(), "My Zappi", "Please ask me for energy " +
                "usage for a specific day or month.");
    }

    private HandlerInput.Builder handlerInputBuilder() {
        return HandlerInput.builder()
                .withRequestEnvelope(requestEnvelopeBuilder().build());
    }

    private RequestEnvelope.Builder requestEnvelopeBuilder() {
        return RequestEnvelope.builder()
                .withRequest(intentRequest);
    }

    private void initIntentRequest(Object object) {
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder()
                        .putSlotsItem("date", Slot.builder().withValue(object.toString()).build())
                        .withName("date").build())
                .build();
    }
}
