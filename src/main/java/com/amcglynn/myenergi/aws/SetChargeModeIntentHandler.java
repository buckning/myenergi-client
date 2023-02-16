package com.amcglynn.myenergi.aws;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amcglynn.myenergi.MyEnergiClient;
import com.amcglynn.myenergi.ZappiChargeMode;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class SetChargeModeIntentHandler implements RequestHandler {

    private static final Map<String, ZappiChargeMode> CHARGE_MODES = Map.of("eco plus", ZappiChargeMode.ECO_PLUS,
            "eco", ZappiChargeMode.ECO,
            "stop", ZappiChargeMode.STOP,
            "fast", ZappiChargeMode.FAST);

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

        var chargeMode = getZappiChargeMode(chargeModeSlot.getValue().toLowerCase());
        changeChargeMode(chargeMode);
        return handlerInput.getResponseBuilder()
                .withSpeech("Changed charging mode to  " + chargeMode.getDisplayName() + ". This may take a few minutes.")
                .build();
    }

    private ZappiChargeMode getZappiChargeMode(String mode) {
        var valueFromMap = CHARGE_MODES.get(mode);

        if (valueFromMap != null) {
            return valueFromMap;
        }
        return ZappiChargeMode.valueOf(mode.toUpperCase());
    }

    private void changeChargeMode(ZappiChargeMode chargeMode) {
        var apiKey = System.getenv("myEnergiHubApiKey");
        var serialNumber = System.getenv("myEnergiHubSerialNumber");
        var client = new MyEnergiClient(serialNumber, apiKey);
        client.setZappiChargeMode(chargeMode);
    }
}
