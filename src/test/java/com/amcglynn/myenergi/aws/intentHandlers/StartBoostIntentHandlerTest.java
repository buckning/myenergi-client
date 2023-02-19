package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Slot;
import com.amcglynn.myenergi.service.ZappiService;
import com.amcglynn.myenergi.units.KiloWattHour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.amcglynn.myenergi.aws.intentHandlers.ResponseVerifier.verifySimpleCardInResponse;
import static com.amcglynn.myenergi.aws.intentHandlers.ResponseVerifier.verifySpeechInResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartBoostIntentHandlerTest {

    @Mock
    private ZappiService mockZappiService;
    private IntentRequest intentRequest;

    private StartBoostIntentHandler handler;

    @BeforeEach
    void setUp() {
        handler = new StartBoostIntentHandler(mockZappiService);
        initIntentRequest("Duration", "PT25M");
    }

    @Test
    void testCanHandleOnlyTriggersForTheIntent() {
        assertThat(handler.canHandle(handlerInputBuilder().build())).isTrue();
    }

    @Test
    void testCanHandleReturnsFalseWhenNotTheCorrectIntent() {
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder()
                        .withName("SetChargeMode").build())
                .build();
        assertThat(handler.canHandle(handlerInputBuilder().build())).isFalse();
    }

    @Test
    void testHandleWithDuration() {
        when(mockZappiService.startSmartBoost(any(Duration.class))).thenReturn(LocalTime.parse("09:15:00"));

        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Boosting until 9:15 am</speak>");
        verifySimpleCardInResponse(result.get(), "Boosting...", "Boosting until 9:15 am.");
        verify(mockZappiService).startSmartBoost(Duration.of(25, ChronoUnit.MINUTES));
    }

    @Test
    void testHandleWithEndTime() {
        when(mockZappiService.startSmartBoost(any(LocalTime.class))).thenReturn(LocalTime.parse("10:30:00"));
        initIntentRequest("Time", "10:30");

        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Boosting until 10:30 am</speak>");
        verifySimpleCardInResponse(result.get(), "Boosting...", "Boosting until 10:30 am.");
        verify(mockZappiService).startSmartBoost(LocalTime.of(10, 30));
    }

    @Test
    void testHandleWithKilowattHours() {
        initIntentRequest("KiloWattHours", "20");

        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Charging 20.0 kilowatt hours</speak>");
        verifySimpleCardInResponse(result.get(), "Charging...", "Charging 20.0 kilowatt hours");
        verify(mockZappiService).startBoost(new KiloWattHour(20));
    }

    @Test
    void testHandleWithNoSlotValues() {
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder()
                        .withName("StartBoostMode").build())
                .build();

        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Sorry, I didn't understand that</speak>");
        verifySimpleCardInResponse(result.get(), "Sorry", "Sorry, I didn't understand that");
    }

    private HandlerInput.Builder handlerInputBuilder() {
        return HandlerInput.builder()
                .withRequestEnvelope(requestEnvelopeBuilder().build());
    }

    private RequestEnvelope.Builder requestEnvelopeBuilder() {
        return RequestEnvelope.builder()
                .withRequest(intentRequest);
    }

    private void initIntentRequest(String slotName, String slotValue) {
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder()
                        .putSlotsItem(slotName, Slot.builder().withValue(slotValue).build())
                        .withName("StartBoostMode").build())
                .build();
    }
}
