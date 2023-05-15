package com.amcglynn.myenergi.aws.intenthandlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.aws.AlexaZappiChargeModeMapper;
import com.amcglynn.myenergi.aws.MyZappi;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.service.ZappiServiceFactory;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class SetChargeModeIntentHandler implements RequestHandler {

    private final AlexaZappiChargeModeMapper mapper;
    private final ZappiServiceFactory factory;

    public SetChargeModeIntentHandler(ZappiServiceFactory factory) {
        mapper = new AlexaZappiChargeModeMapper();
        this.factory = factory;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("SetChargeMode"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        try {
            var zappiService = factory.newZappiService(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
            var request = handlerInput.getRequestEnvelope().getRequest();
            var intentRequest = (IntentRequest) request;
            var slots = intentRequest.getIntent().getSlots();
            var chargeModeSlot = slots.get("ChargeMode");

            var chargeMode = mapper.getZappiChargeMode(chargeModeSlot.getValue().toLowerCase());
            zappiService.setChargeMode(chargeMode);
            return handlerInput.getResponseBuilder()
                    .withSpeech("Changed charging mode to " + chargeMode.getDisplayName() + ". This may take a few minutes.")
                    .withSimpleCard(MyZappi.TITLE, "Changed charging mode to "
                            + chargeMode.getDisplayName() + ". This may take a few minutes.")
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
