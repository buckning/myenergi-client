package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.service.ZappiService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StopBoostIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public StopBoostIntentHandler() {
        this(new ZappiService());
    }

    protected StopBoostIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("StopBoostMode"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        zappiService.stopBoost();
        return handlerInput.getResponseBuilder()
                .withSpeech("Stopping boost mode now.")
                .withSimpleCard("Stopping boost", "Stopping boost mode now.")
                .build();
    }
}
