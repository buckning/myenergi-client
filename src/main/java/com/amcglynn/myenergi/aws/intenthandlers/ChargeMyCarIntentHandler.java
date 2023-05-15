package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiServiceFactory;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ChargeMyCarIntentHandler implements RequestHandler {

    private final ZappiServiceFactory factory;

    public ChargeMyCarIntentHandler(ZappiServiceFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("ChargeMyCar"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        try {
            var zappiService = factory.newZappiService(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
            zappiService.setChargeMode(ZappiChargeMode.FAST);
            String result = "Changed charging mode to fast. This may take a few minutes.";
            return handlerInput.getResponseBuilder()
                    .withSpeech(result)
                    .withSimpleCard(MyZappi.TITLE, result)
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
