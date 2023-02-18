package com.amcglynn.myenergi.aws;

import com.amcglynn.myenergi.ZappiChargeMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlexaZappiChargeModeMapperTest {

    @ValueSource(strings = {
            "eco",
            "Eco",
            "EcO",
            "eCo",
            "  eco  ",
            "\neco\n"
    })
    @ParameterizedTest
    void testEcoMode(String input) {
        assertThat(new AlexaZappiChargeModeMapper().getZappiChargeMode(input))
                .isEqualTo(ZappiChargeMode.ECO);
    }

    @ValueSource(strings = {
            "eco plus",
            "Eco +",
            "EcO +",
            "eCo plus",
            "  eco plus",
            "  ecoPlus",
            "\neco PlUs\n"
    })
    @ParameterizedTest
    void testEcoPlusMode(String input) {
        assertThat(new AlexaZappiChargeModeMapper().getZappiChargeMode(input))
                .isEqualTo(ZappiChargeMode.ECO_PLUS);
    }

    @ValueSource(strings = {
            "stop",
            "SToP",
            "  Stop ",
            "\n StoP \n"
    })
    @ParameterizedTest
    void testStopMode(String input) {
        assertThat(new AlexaZappiChargeModeMapper().getZappiChargeMode(input))
                .isEqualTo(ZappiChargeMode.STOP);
    }

    @ValueSource(strings = {
            "fast",
            "FaSt",
            "  Fast ",
            "\n FAST \n"
    })
    @ParameterizedTest
    void testFastMode(String input) {
        assertThat(new AlexaZappiChargeModeMapper().getZappiChargeMode(input))
                .isEqualTo(ZappiChargeMode.FAST);
    }

    @ValueSource(strings = {
            "",
            "bananas",
            "\n"
    })
    @ParameterizedTest
    void testInvalidInput(String input) {
        var alexaZappiChargeModeMapper = new AlexaZappiChargeModeMapper();
        assertThatThrownBy(() ->
                alexaZappiChargeModeMapper.getZappiChargeMode(input)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
