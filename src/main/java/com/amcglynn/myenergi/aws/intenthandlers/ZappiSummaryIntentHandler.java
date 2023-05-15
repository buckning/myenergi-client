package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.directive.Header;
import com.amazon.ask.model.services.directive.SendDirectiveRequest;
import com.amazon.ask.model.services.directive.SpeakDirective;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.aws.responses.ZappiStatusSummaryCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiStatusSummaryVoiceResponse;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiServiceFactory;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ZappiSummaryIntentHandler implements RequestHandler {

    private final ZappiServiceFactory factory;

    public ZappiSummaryIntentHandler(ZappiServiceFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("StatusSummary"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        handlerInput.getServiceClientFactory().getDirectiveService()
                .enqueue(SendDirectiveRequest.builder()
                        .withDirective(SpeakDirective.builder().withSpeech("Sure").build())
                        .withHeader(Header.builder().withRequestId(handlerInput.getRequestEnvelope().getRequest().getRequestId()).build())
                        .build());

        try {
            var zappiService = factory.newZappiService(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
            var summary = zappiService.getStatusSummary().get(0);
            return handlerInput.getResponseBuilder()
                    .withSpeech(new ZappiStatusSummaryVoiceResponse(summary).toString())
                    .withSimpleCard(MyZappi.TITLE, new ZappiStatusSummaryCardResponse(summary).toString())
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
