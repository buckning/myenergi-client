package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiServiceFactory;
import com.amcglynn.myenergi.units.KiloWattHour;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StartBoostIntentHandler implements RequestHandler {

    private final ZappiServiceFactory factory;

    public StartBoostIntentHandler(ZappiServiceFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("StartBoostMode"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        var duration = parseDurationSlot(handlerInput);
        var time = parseSlot(handlerInput, "Time");
        var kilowattHours = parseKiloWattHourSlot(handlerInput);
        try {
            var zappiService = factory.newZappiService(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
            if (kilowattHours.isPresent()) {
                zappiService.startBoost(kilowattHours.get());
                return buildResponse(handlerInput, kilowattHours.get());
            }

            if (time.isPresent()) {
                var localTime = LocalTime.parse(time.get());
                zappiService.startSmartBoost(localTime);
                return buildResponse(handlerInput, localTime);
            }

            if (duration.isPresent()) {
                return duration
                        .map(zappiService::startSmartBoost)
                        .map(lt -> buildResponse(handlerInput, lt))
                        .orElseGet(() -> buildNotFoundResponse(handlerInput));
            }
        } catch (ClientException e) {
            String errorMessage = "Could not authenticate with myenergi API, please check your API key and serial number";
            return handlerInput.getResponseBuilder()
                    .withSpeech(errorMessage)
                    .withSimpleCard(MyZappi.TITLE, errorMessage)
                    .build();
        }

        return buildNotFoundResponse(handlerInput);
    }

    private Optional<Response> buildNotFoundResponse(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Sorry, I didn't understand that")
                .withSimpleCard(MyZappi.TITLE, "Sorry, I didn't understand that")
                .build();
    }

    private Optional<Response> buildResponse(HandlerInput handlerInput, LocalTime endTime) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Boosting until " + endTime.format(DateTimeFormatter.ofPattern("h:mm a")))
                .withSimpleCard("Boosting...", "Boosting until "
                        + endTime.format(DateTimeFormatter.ofPattern("h:mm a")) + ".")
                .build();
    }

    private Optional<Response> buildResponse(HandlerInput handlerInput, KiloWattHour kilowattHours) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Charging " + kilowattHours + " kilowatt hours")
                .withSimpleCard("Charging...", "Charging " + kilowattHours + " kilowatt hours")
                .build();
    }

    private Optional<Duration> parseDurationSlot(HandlerInput handlerInput) {
        return parseSlot(handlerInput, "Duration").map(Duration::parse);
    }

    private Optional<String> parseSlot(HandlerInput handlerInput, String slotName) {
        return RequestHelper.forHandlerInput(handlerInput)
                .getSlotValue(slotName);
    }

    private Optional<KiloWattHour> parseKiloWattHourSlot(HandlerInput handlerInput) {
        return parseSlot(handlerInput, "KiloWattHours").map(Double::parseDouble).map(KiloWattHour::new);
    }
}
