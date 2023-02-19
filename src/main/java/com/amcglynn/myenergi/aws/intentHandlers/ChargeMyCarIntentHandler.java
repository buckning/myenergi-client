package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.service.ZappiService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ChargeMyCarIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public ChargeMyCarIntentHandler() {
        this.zappiService = new ZappiService();
    }

    protected ChargeMyCarIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("ChargeMyCar"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        zappiService.setChargeMode(ZappiChargeMode.FAST);
        String result = "Changed charging mode to fast. This may take a few minutes.";
        return handlerInput.getResponseBuilder()
                .withSpeech(result)
                .withSimpleCard("Charging...", result)
                .build();
    }
}
