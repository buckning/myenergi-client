package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.aws.AlexaZappiChargeModeMapper;
import com.amcglynn.myenergi.service.ZappiService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class SetChargeModeIntentHandler implements RequestHandler {

    private final ZappiService zappiService;
    private final AlexaZappiChargeModeMapper mapper;

    public SetChargeModeIntentHandler() {
        this.zappiService = new ZappiService();
        mapper = new AlexaZappiChargeModeMapper();
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("SetChargeMode"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        var request = handlerInput.getRequestEnvelope().getRequest();
        var intentRequest = (IntentRequest) request;
        var slots = intentRequest.getIntent().getSlots();
        var chargeModeSlot = slots.get("ChargeMode");

        var chargeMode = mapper.getZappiChargeMode(chargeModeSlot.getValue().toLowerCase());
        zappiService.setChargeMode(chargeMode);
        return handlerInput.getResponseBuilder()
                .withSpeech("Changed charging mode to  " + chargeMode.getDisplayName() + ". This may take a few minutes.")
                .build();
    }
}
