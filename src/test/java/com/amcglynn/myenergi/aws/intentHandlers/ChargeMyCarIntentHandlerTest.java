package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.service.ZappiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChargeMyCarIntentHandlerTest {

    @Mock
    private ZappiService mockZappiService;
    private IntentRequest intentRequest;

    private ChargeMyCarIntentHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ChargeMyCarIntentHandler(mockZappiService);
        intentRequest = IntentRequest.builder()
                .withIntent(Intent.builder().withName("ChargeMyCar").build())
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
    void testHandle() {
        var result = handler.handle(handlerInputBuilder().build());
        assertThat(result).isPresent();

        assertThat(result.get().getOutputSpeech()).isInstanceOf(SsmlOutputSpeech.class);
        var ssmlOutputSpeech = (SsmlOutputSpeech) result.get().getOutputSpeech();
        assertThat(ssmlOutputSpeech.getSsml())
                .isEqualTo("<speak>Changed charging mode to fast. This may take a few minutes.</speak>");

        var card = result.get().getCard();
        assertThat(card).isInstanceOf(SimpleCard.class);
        var simpleCard = (SimpleCard) card;
        assertThat(simpleCard.getTitle()).isEqualTo("Charging...");
        assertThat(simpleCard.getContent()).isEqualTo("Changed charging mode to fast. This may take a few minutes.");

        verify(mockZappiService).setChargeMode(ZappiChargeMode.FAST);
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
