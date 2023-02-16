package com.amcglynn.myenergi.units;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnitConversionTest {

    private Joule joule = new Joule(3600000);

    @Test
    void jouleToKiloWattConversion() {
        assertThat(new KiloWattHour(joule)).isEqualTo(new KiloWattHour(1));
    }
}
