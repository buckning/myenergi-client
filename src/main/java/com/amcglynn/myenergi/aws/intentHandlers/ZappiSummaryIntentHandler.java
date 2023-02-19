package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.directive.Directive;
import com.amazon.ask.model.services.directive.Header;
import com.amazon.ask.model.services.directive.SendDirectiveRequest;
import com.amazon.ask.model.services.directive.SpeakDirective;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.aws.responses.ZappiStatusSummaryCardResponse;
import com.amcglynn.myenergi.service.ZappiService;
import com.amcglynn.myenergi.units.KiloWatt;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ZappiSummaryIntentHandler implements RequestHandler {
    private final ZappiService zappiService;

    public ZappiSummaryIntentHandler() {
        this(new ZappiService());
    }

    public ZappiSummaryIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("StatusSummary"));
    }

    private String getEnergyUsage(ZappiStatusSummary summary) {
        var response = "Solar generation is " + new KiloWatt(summary.getGenerated()) + " Kilowatts. ";
        response += "Importing " + new KiloWatt(summary.getGridImport()) + " Kilowatts from the grid. ";
        System.out.println(response);
        return response;
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
                .withSpeech(getEnergyUsage(summary))
                .withSimpleCard("Zappi Summary", new ZappiStatusSummaryCardResponse(summary).toString())
                .build();
    }
}
