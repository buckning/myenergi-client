package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.EvStatusSummary;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.aws.responses.ZappiEvConnectionStatusCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiEvConnectionStatusVoiceResponse;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiServiceFactory;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class GetPlugStatusIntentHandler implements RequestHandler {

    private ZappiServiceFactory factory;

    public GetPlugStatusIntentHandler(ZappiServiceFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("GetPlugStatus"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        try {
            var zappiService = factory.newZappiService(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
            var summary = new EvStatusSummary(zappiService.getStatusSummary().get(0));

            return handlerInput.getResponseBuilder()
                    .withSpeech(new ZappiEvConnectionStatusVoiceResponse(summary).toString())
                    .withSimpleCard(MyZappi.TITLE,
                            new ZappiEvConnectionStatusCardResponse(summary).toString())
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
