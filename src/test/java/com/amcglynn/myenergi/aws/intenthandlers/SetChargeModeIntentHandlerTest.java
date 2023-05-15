package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.User;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.service.ZappiService;
import com.amcglynn.myenergi.service.ZappiServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySimpleCardInResponse;
import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySpeechInResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SetChargeModeIntentHandlerTest {

    @Mock
    private ZappiService mockZappiService;
    @Mock
    private ZappiServiceFactory mockZappiServiceFactory;
    private IntentRequest intentRequest;

    private SetChargeModeIntentHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SetChargeModeIntentHandler(mockZappiServiceFactory);
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("SetChargeMode").build())
                .build();
    }

    @Test
    void testCanHandleOnlyTriggersForTheIntent() {
        assertThat(handler.canHandle(handlerInputBuilder().build())).isTrue();
    }

    @Test
    void testCanHandleReturnsFalseWhenNotTheCorrectIntent() {
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("GoGreen").build())
                .build();
        assertThat(handler.canHandle(handlerInputBuilder().build())).isFalse();
    }

    @ParameterizedTest
    @MethodSource("zappiChargeMode")
    void testHandle(ZappiChargeMode zappiChargeMode) {
        when(mockZappiServiceFactory.newZappiService(anyString())).thenReturn(mockZappiService);
        initIntentRequest(zappiChargeMode);
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();

        verifySpeechInResponse(result.get(), "<speak>Changed charging mode to "
                + zappiChargeMode.getDisplayName() + ". This may take a few minutes.</speak>");
        verifySimpleCardInResponse(result.get(), "My Zappi", "Changed charging mode to " +
                        zappiChargeMode.getDisplayName() + ". This may take a few minutes.");

        verify(mockZappiService).setChargeMode(zappiChargeMode);
    }

    private HandlerInput.Builder handlerInputBuilder() {
        return HandlerInput.builder()
                .withRequestEnvelope(requestEnvelopeBuilder().build());
    }

    private RequestEnvelope.Builder requestEnvelopeBuilder() {
        return RequestEnvelope.builder()
                .withRequest(intentRequest)
                .withSession(Session.builder().withUser(User.builder().withUserId("test").build()).build());
    }

    private void initIntentRequest(ZappiChargeMode chargeMode) {
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder()
                        .putSlotsItem("ChargeMode", Slot.builder().withValue(chargeMode.getDisplayName()).build())
                        .withName("ChargeMode").build())
                .build();
    }

    private static Stream<Arguments> zappiChargeMode() {
        return Stream.of(
                Arguments.of(ZappiChargeMode.STOP),
                Arguments.of(ZappiChargeMode.ECO_PLUS),
                Arguments.of(ZappiChargeMode.ECO),
                Arguments.of(ZappiChargeMode.FAST),
                Arguments.of(ZappiChargeMode.BOOST));
    }
}
