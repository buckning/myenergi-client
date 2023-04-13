package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.aws.MyZappi;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StopIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("AMAZON.StopIntent")) ||
                handlerInput.matches(intentName("AMAZON.CancelIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Thank you for using " + MyZappi.TITLE)
                .withSimpleCard(MyZappi.TITLE, "Thank you for using " + MyZappi.TITLE)
                .build();
    }
}