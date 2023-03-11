package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySimpleCardInResponse;
import static com.amcglynn.myenergi.aws.ResponseVerifier.verifySpeechInResponse;
import static org.assertj.core.api.Assertions.assertThat;

class FallbackIntentHandlerTest {

    private IntentRequest intentRequest;

    private FallbackIntentHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FallbackIntentHandler();
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("AMAZON.FallbackIntent").build())
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

    @Test
    void testHandle() {
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();

        verifySpeechInResponse(result.get(), "<speak>Sorry, I don't know how to handle that. Please try again.</speak>");
        verifySimpleCardInResponse(result.get(), "My Zappi", "Sorry, I don't know how to handle that. Please try again.");
    }

    private HandlerInput.Builder handlerInputBuilder() {
        return HandlerInput.builder()
                .withRequestEnvelope(requestEnvelopeBuilder().build());
    }

    private RequestEnvelope.Builder requestEnvelopeBuilder() {
        return RequestEnvelope.builder()
                .withRequest(intentRequest);
    }

}
