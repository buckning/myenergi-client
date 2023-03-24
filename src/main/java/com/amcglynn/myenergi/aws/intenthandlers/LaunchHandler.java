package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.LaunchRequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.aws.responses.ZappiEnergyCostCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiEnergyCostVoiceResponse;
import com.amcglynn.myenergi.energycost.ImportedEnergyHourSummary;
import com.amcglynn.myenergi.service.ZappiService;

import java.time.LocalDate;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchHandler implements LaunchRequestHandler {

    private final ZappiService zappiService;

    public LaunchHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        return handlerInput.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        return handleDate(handlerInput);
    }

    private Optional<Response> handleDate(HandlerInput handlerInput) {
        var localDate = LocalDate.now();
        var history = zappiService.getHourlySummary(localDate);
        var importCost = history.stream().mapToDouble(ImportedEnergyHourSummary::getImportCost).sum();
        var exportCost = history.stream().mapToDouble(ImportedEnergyHourSummary::getExportCost).sum();
        var solarSavings = history.stream().mapToDouble(ImportedEnergyHourSummary::getSolarSavings).sum();

        var voiceResponse = new ZappiEnergyCostVoiceResponse(localDate, importCost, exportCost, solarSavings).toString();
        var cardResponse = new ZappiEnergyCostCardResponse(localDate, importCost, exportCost, solarSavings).toString();

        return handlerInput.getResponseBuilder()
                .withSpeech(voiceResponse)
                .withSimpleCard(MyZappi.TITLE,
                        cardResponse)
                .build();
    }
}
