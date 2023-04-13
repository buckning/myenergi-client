package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.aws.exception.InvalidDateException;
import com.amcglynn.myenergi.aws.responses.ZappiEnergyCostCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiEnergyCostVoiceResponse;
import com.amcglynn.myenergi.energycost.ImportedEnergyHourSummary;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class GetEnergyCostIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public GetEnergyCostIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("GetEnergyCost"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        var date = parseSlot(handlerInput, "date");
        if (date.isPresent()) {
            try {
                return handleDate(handlerInput, date.get());
            } catch (InvalidDateException e) {
                return handlerInput.getResponseBuilder()
                        .withSpeech("You cannot request energy cost for a time in the future.")
                        .withSimpleCard(MyZappi.TITLE,
                                "You cannot request energy cost for a time in the future.")
                        .build();
            }
        }
        return handlerInput.getResponseBuilder()
                .withSpeech("Please ask me for energy cost for a specific day")
                .withSimpleCard(MyZappi.TITLE,
                        "Please ask me for energy cost for a specific day.")
                .build();
    }

    private Optional<Response> handleDate(HandlerInput handlerInput, String date) {
        try {
            var localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            validate(localDate);
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
        } catch (ClientException e) {
            String errorMessage = "Could not authenticate with myenergi API, please check your API key and serial number";
            return handlerInput.getResponseBuilder()
                    .withSpeech(errorMessage)
                    .withSimpleCard(MyZappi.TITLE, errorMessage)
                    .build();
        }
    }

    private void validate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException();
        }
    }

    private Optional<String> parseSlot(HandlerInput handlerInput, String slotName) {
        return RequestHelper.forHandlerInput(handlerInput)
                .getSlotValue(slotName);
    }
}
