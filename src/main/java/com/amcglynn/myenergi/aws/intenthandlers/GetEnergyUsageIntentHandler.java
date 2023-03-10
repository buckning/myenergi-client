package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.directive.Header;
import com.amazon.ask.model.services.directive.SendDirectiveRequest;
import com.amazon.ask.model.services.directive.SpeakDirective;
import com.amazon.ask.request.RequestHelper;
import com.amcglynn.myenergi.aws.exception.InvalidDateException;
import com.amcglynn.myenergi.aws.responses.ZappiDaySummaryCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiDaySummaryVoiceResponse;
import com.amcglynn.myenergi.aws.responses.ZappiMonthSummaryCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiMonthSummaryVoiceResponse;
import com.amcglynn.myenergi.service.ZappiService;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class GetEnergyUsageIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public GetEnergyUsageIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("GetEnergyUsage"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        // 2023
        // 2023-05
        // 2023-05-06
        var date = parseSlot(handlerInput, "date");
        if (date.isPresent()) {
            try {
                return handleDate(handlerInput, date.get());
            } catch (InvalidDateException e) {
                return handlerInput.getResponseBuilder()
                        .withSpeech("You cannot request usage data for a time in the future.")
                        .withSimpleCard("My Zappi",
                                "You cannot request usage data for a time in the future.")
                        .build();
            }
        }
        return handlerInput.getResponseBuilder()
                .withSpeech("Please ask me for energy usage for a specific day or month")
                .withSimpleCard("My Zappi",
                        "Please ask me for energy usage for a specific day or month.")
                .build();
    }

    private Optional<Response> handleDate(HandlerInput handlerInput, String date) {
        String cardResponse;
        String voiceResponse;
        if (date.length() == 4) {
            zappiService.getEnergyUsage(Year.parse(date));
            return handlerInput.getResponseBuilder()
                    .withSpeech("Sorry, it would take me too long to crunch the numbers for an entire year! " +
                            "Please provide a day or month")
                    .withSimpleCard("My Zappi",
                            "Sorry, it would take me too long to crunch the numbers for an entire year! " +
                                    "Please provide a day or month.")
                    .build();
        } else if (date.length() == 7) {
            var yearMonth = YearMonth.parse(date);
            validate(yearMonth);
            zappiService.registerNotificationListener((current, total) -> {
                if (current % 5 == 0) {
                    handlerInput.getServiceClientFactory().getDirectiveService()
                            .enqueue(SendDirectiveRequest.builder()
                                    .withDirective(SpeakDirective.builder().withSpeech("Calculating, please wait...").build())
                                    .withHeader(Header.builder().withRequestId(handlerInput.getRequestEnvelope().getRequest().getRequestId()).build())
                                    .build());
                }
            });
            var summary = zappiService.getEnergyUsage(yearMonth);
            voiceResponse = new ZappiMonthSummaryVoiceResponse(summary).toString();
            cardResponse = new ZappiMonthSummaryCardResponse(summary).toString();
        } else {
            var localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            validate(localDate);
            var history = zappiService.getEnergyUsage(localDate);
            voiceResponse = new ZappiDaySummaryVoiceResponse(history).toString();
            cardResponse = new ZappiDaySummaryCardResponse(history).toString();
        }
        return handlerInput.getResponseBuilder()
                .withSpeech(voiceResponse)
                .withSimpleCard("My Zappi",
                        cardResponse)
                .build();
    }

    private void validate(YearMonth yearMonth) {
        if (yearMonth.isAfter(YearMonth.now())) {
            throw new InvalidDateException();
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
