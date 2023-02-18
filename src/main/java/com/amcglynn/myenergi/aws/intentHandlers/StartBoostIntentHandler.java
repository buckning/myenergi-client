package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import com.amcglynn.myenergi.service.ZappiService;
import com.amcglynn.myenergi.units.KiloWattHour;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StartBoostIntentHandler implements RequestHandler {

    private final ZappiService zappiService;

    public StartBoostIntentHandler() {
        this.zappiService = new ZappiService();
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

        if (kilowattHours.isPresent()) {
            System.out.println("Adding " + kilowattHours + "kilowatt hours");
            zappiService.startBoost(kilowattHours.get());
            return buildResponse(handlerInput, kilowattHours.get());
        }

        if (time.isPresent()) {
            var localTime = LocalTime.parse(time.get());

            String formattedTime = localTime.format(DateTimeFormatter.ofPattern("h:mm a"));
            System.out.println("Boosting until " + formattedTime);
            zappiService.startSmartBoost(localTime);
            return buildResponse(handlerInput, localTime);
        }

        if (duration.isPresent()) {
            return duration
                    .map(zappiService::startSmartBoost)
                    .map(ldt -> buildResponse(handlerInput, ldt))
                    .orElseGet(() -> buildNotFoundResponse(handlerInput));
        }

        return buildNotFoundResponse(handlerInput);
    }

    private Optional<Response> buildNotFoundResponse(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Sorry, I didn't understand that")
                .build();
    }

    private Optional<Response> buildResponse(HandlerInput handlerInput, LocalTime endTime) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Boosting until " + endTime.format(DateTimeFormatter.ofPattern("h:mm a")))
                .build();
    }

    private Optional<Response> buildResponse(HandlerInput handlerInput, KiloWattHour kilowattHours) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Charging " + kilowattHours + " kilowatt hours")
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
