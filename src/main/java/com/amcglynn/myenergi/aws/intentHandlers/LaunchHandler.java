package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.LaunchRequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchHandler implements LaunchRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        return handlerInput.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Hi, I can change your charge type and provide you energy usage. " +
                        "Ask me to start charging or to switch to solar. " +
                        "You can also ask me for an energy summary.")
                .withSimpleCard("Greetings!", "I can change your charge type and provide you energy usage. " +
                        "Ask me to start charging or to switch to solar. You can also ask me for an energy summary.")
                .build();
    }
}
