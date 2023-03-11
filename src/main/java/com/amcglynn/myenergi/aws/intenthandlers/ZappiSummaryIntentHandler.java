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
import com.amcglynn.myenergi.service.ZappiService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ZappiSummaryIntentHandler implements RequestHandler {
    private final ZappiService zappiService;

    public ZappiSummaryIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
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

        var summary = zappiService.getStatusSummary().get(0);
        return handlerInput.getResponseBuilder()
                .withSpeech(new ZappiStatusSummaryVoiceResponse(summary).toString())
                .withSimpleCard(MyZappi.TITLE, new ZappiStatusSummaryCardResponse(summary).toString())
                .build();
    }
}
