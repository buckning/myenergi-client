package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import com.amcglynn.myenergi.aws.exception.InvalidDateException;
import com.amcglynn.myenergi.aws.responses.ZappiEnergyCostCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiEnergyCostVoiceResponse;
import com.amcglynn.myenergi.energycost.ImportedEnergyHourSummary;
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
                        .withSimpleCard("My Zappi",
                                "You cannot request energy cost for a time in the future.")
                        .build();
            }
        }
        return handlerInput.getResponseBuilder()
                .withSpeech("Please ask me for energy cost for a specific day")
                .withSimpleCard("My Zappi",
                        "Please ask me for energy cost for a specific day.")
                .build();
    }

    private Optional<Response> handleDate(HandlerInput handlerInput, String date) {

        var localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        validate(localDate);
        var history = zappiService.getHourlySummary(localDate);
        var cost = history.stream().mapToDouble(ImportedEnergyHourSummary::getCost).sum();

        var voiceResponse = new ZappiEnergyCostVoiceResponse(localDate, cost).toString();
        var cardResponse = new ZappiEnergyCostCardResponse(localDate, cost).toString();

        return handlerInput.getResponseBuilder()
                .withSpeech(voiceResponse)
                .withSimpleCard("My Zappi",
                        cardResponse)
                .build();
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
