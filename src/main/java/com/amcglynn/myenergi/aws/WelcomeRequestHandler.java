package com.amcglynn.myenergi.aws;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.MyEnergiClient;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.units.KiloWatt;

import java.util.Optional;
import java.util.stream.Collectors;

public class WelcomeRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return true;
    }

    private String getEnergyUsage() {
        var apiKey = System.getenv("myEnergiHubApiKey");
        var serialNumber = System.getenv("myEnergiHubSerialNumber");
        var client = new MyEnergiClient(serialNumber, apiKey);
        var zappis = client.getZappiStatus().getZappi()
                .stream().map(ZappiStatusSummary::new).collect(Collectors.toList());
        var response = "solar generation is " + new KiloWatt(zappis.get(0).getGenerated()) + " Kilowatts. ";
        response += "Importing " + new KiloWatt(zappis.get(0).getGridImport()) + " Kilowatts from the grid. ";
        System.out.println(response);
        return response;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech(getEnergyUsage())
                .build();
    }
}
