package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.service.ZappiService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class GoGreenIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public GoGreenIntentHandler() {
        this.zappiService = new ZappiService();
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("GoGreen"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        zappiService.setChargeMode(ZappiChargeMode.ECO_PLUS);
        return handlerInput.getResponseBuilder()
                .withSpeech("Changed charging mode to Eco+. This may take a few minutes.")
                .build();
    }
}
