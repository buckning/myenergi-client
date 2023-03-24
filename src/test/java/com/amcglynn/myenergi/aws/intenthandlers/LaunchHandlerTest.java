package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amcglynn.myenergi.service.ZappiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LaunchHandlerTest {

    @Mock
    private ZappiService zappiServiceMock;
    private LaunchRequest launchRequest;

    private LaunchHandler handler;

    @BeforeEach
    void setUp() {
        handler = new LaunchHandler(zappiServiceMock);
        launchRequest = LaunchRequest.builder()
                .build();
    }

    @Test
    void testCanHandleOnlyTriggersForTheIntent() {
        assertThat(handler.canHandle(handlerInputBuilder().build())).isTrue();
    }

    @Test
    void testCanHandleReturnsFalseWhenNotTheCorrectIntent() {
        var intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("SetChargeMode").build()).build();
        var requestEnvelope = RequestEnvelope.builder().withRequest(intentRequest).build();
        var handlerInput = HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();
        assertThat(handler.canHandle(handlerInput)).isFalse();
    }

    private HandlerInput.Builder handlerInputBuilder() {
        return HandlerInput.builder()
                .withRequestEnvelope(requestEnvelopeBuilder().build());
    }

    private RequestEnvelope.Builder requestEnvelopeBuilder() {
        return RequestEnvelope.builder()
                .withRequest(launchRequest);
    }
}
