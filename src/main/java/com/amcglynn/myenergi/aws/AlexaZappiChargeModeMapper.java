package com.amcglynn.myenergi.aws;

import com.amcglynn.myenergi.ZappiChargeMode;

import java.util.Map;

/**
 * Convert between the input that Alexa passes to a ZappiChargeMode.
 */
public class AlexaZappiChargeModeMapper {
    private static final Map<String, ZappiChargeMode> CHARGE_MODES = Map.of("eco plus", ZappiChargeMode.ECO_PLUS,
            "ecoplus", ZappiChargeMode.ECO_PLUS,
            "eco +", ZappiChargeMode.ECO_PLUS,
            "eco+", ZappiChargeMode.ECO_PLUS,
            "eco", ZappiChargeMode.ECO,
            "stop", ZappiChargeMode.STOP,
            "fast", ZappiChargeMode.FAST);

    public ZappiChargeMode getZappiChargeMode(String mode) {
        var valueFromMap = CHARGE_MODES.get(mode.toLowerCase().trim());

        if (valueFromMap != null) {
            return valueFromMap;
        }
        return ZappiChargeMode.valueOf(mode.toUpperCase().trim());
    }
}
