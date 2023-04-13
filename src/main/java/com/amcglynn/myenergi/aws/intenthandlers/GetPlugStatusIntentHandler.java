package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.directive.Header;
import com.amazon.ask.model.services.directive.SendDirectiveRequest;
import com.amazon.ask.model.services.directive.SpeakDirective;
import com.amazon.ask.request.RequestHelper;
import com.amcglynn.myenergi.EvStatusSummary;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.aws.exception.InvalidDateException;
import com.amcglynn.myenergi.aws.responses.ZappiDaySummaryCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiDaySummaryVoiceResponse;
import com.amcglynn.myenergi.aws.responses.ZappiEvConnectionStatusCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiEvConnectionStatusVoiceResponse;
import com.amcglynn.myenergi.aws.responses.ZappiMonthSummaryCardResponse;
import com.amcglynn.myenergi.aws.responses.ZappiMonthSummaryVoiceResponse;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiService;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class GetPlugStatusIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public GetPlugStatusIntentHandler(ZappiService zappiService) {
        this.zappiService = zappiService;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("GetPlugStatus"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        try {
            var summary = new EvStatusSummary(zappiService.getStatusSummary().get(0));

            return handlerInput.getResponseBuilder()
                    .withSpeech(new ZappiEvConnectionStatusVoiceResponse(summary).toString())
                    .withSimpleCard(MyZappi.TITLE,
                            new ZappiEvConnectionStatusCardResponse(summary).toString())
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
