package com.amcglynn.myenergi.aws;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.MyEnergiClient;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.units.KiloWatt;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.amazon.ask.request.Predicates.intentName;

public class WelcomeRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return !handlerInput.matches(intentName("SetChargeMode"));
    }

    private String getEnergyUsage(ZappiStatusSummary summary) {
        var response = "solar generation is " + new KiloWatt(summary.getGenerated()) + " Kilowatts. ";
        response += "Importing " + new KiloWatt(summary.getGridImport()) + " Kilowatts from the grid. ";
        System.out.println(response);
        return response;
    }

    private ZappiStatusSummary getZappiStatusSummary() {
        var apiKey = System.getenv("myEnergiHubApiKey");
        var serialNumber = System.getenv("myEnergiHubSerialNumber");
        var client = new MyEnergiClient(serialNumber, apiKey);
        var zappis = client.getZappiStatus().getZappi()
                .stream().map(ZappiStatusSummary::new).collect(Collectors.toList());
        return zappis.get(0);
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        var summary = getZappiStatusSummary();
        return handlerInput.getResponseBuilder()
                .withSpeech(getEnergyUsage(summary))
                .withSimpleCard("Zappi Summary", new ZappiStatusSummaryCardResponse(summary).toString())
                .build();
    }
}
