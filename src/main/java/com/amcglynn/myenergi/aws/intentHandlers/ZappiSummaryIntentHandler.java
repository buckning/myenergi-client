package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.aws.view.ZappiStatusSummaryCardResponse;
import com.amcglynn.myenergi.service.ZappiService;
import com.amcglynn.myenergi.units.KiloWatt;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ZappiSummaryIntentHandler implements RequestHandler {
    private final ZappiService zappiService;

    public ZappiSummaryIntentHandler() {
        zappiService = new ZappiService();
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("summary"));
    }

    private String getEnergyUsage(ZappiStatusSummary summary) {
        var response = "solar generation is " + new KiloWatt(summary.getGenerated()) + " Kilowatts. ";
        response += "Importing " + new KiloWatt(summary.getGridImport()) + " Kilowatts from the grid. ";
        System.out.println(response);
        return response;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        var summary = zappiService.getStatusSummary().get(0);
        return handlerInput.getResponseBuilder()
                .withSpeech(getEnergyUsage(summary))
                .withSimpleCard("Zappi Summary", new ZappiStatusSummaryCardResponse(summary).toString())
                .build();
    }
}
