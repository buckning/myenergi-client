package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.User;
import com.amcglynn.myenergi.service.ZappiService;
import com.amcglynn.myenergi.service.ZappiServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySimpleCardInResponse;
import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySpeechInResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StopBoostIntentHandlerTest {

    @Mock
    private ZappiService mockZappiService;
    @Mock
    private ZappiServiceFactory mockZappiServiceFactory;
    private IntentRequest intentRequest;

    private StopBoostIntentHandler handler;

    @BeforeEach
    void setUp() {
        handler = new StopBoostIntentHandler(mockZappiServiceFactory);
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("StopBoostMode").build())
                .build();
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
    void testHandle() {
        when(mockZappiServiceFactory.newZappiService(anyString())).thenReturn(mockZappiService);
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();
        verifySpeechInResponse(result.get(), "<speak>Stopping boost mode now.</speak>");
        verifySimpleCardInResponse(result.get(), "My Zappi", "Stopping boost mode now.");
        verify(mockZappiService).stopBoost();
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
}
