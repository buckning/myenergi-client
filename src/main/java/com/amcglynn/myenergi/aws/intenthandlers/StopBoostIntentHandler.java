package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StopBoostIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public StopBoostIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("StopBoostMode"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        try {
            zappiService.stopBoost();
            return handlerInput.getResponseBuilder()
                    .withSpeech("Stopping boost mode now.")
                    .withSimpleCard(MyZappi.TITLE, "Stopping boost mode now.")
                    .build();
        } catch (ClientException e) {
            String errorMessage = "Could not authenticate with myenergi API, please check your API key and serial number";
            return handlerInput.getResponseBuilder()
                    .withSpeech(errorMessage)
                    .withSimpleCard(MyZappi.TITLE, errorMessage)
                    .build();
        }
    }
}
