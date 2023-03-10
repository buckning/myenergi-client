package com.amcglynn.myenergi.energycost;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TariffsTest {

    @MethodSource("allTariffs")
    @ParameterizedTest
    void testGetTariff(int hour, Tariff.Type expectedTariff) {
        assertThat(new DayTariffs().getTariff(hour).getType()).isEqualTo(expectedTariff);
    }

    @MethodSource("invalidHours")
    @ParameterizedTest
    void testInvalidHours(int hour) {
        var dayTariffs = new DayTariffs();
        assertThatThrownBy(() -> dayTariffs.getTariff(hour))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidHours() {
        return Stream.of(
                Arguments.of(100),
                Arguments.of(24),
                Arguments.of(-100));
    }
    private static Stream<Arguments> allTariffs() {
        return Stream.of(
                Arguments.of(0, Tariff.Type.NIGHT),
                Arguments.of(1, Tariff.Type.NIGHT),
                Arguments.of(2, Tariff.Type.NIGHT),
                Arguments.of(3, Tariff.Type.NIGHT),
                Arguments.of(4, Tariff.Type.NIGHT),
                Arguments.of(5, Tariff.Type.NIGHT),
                Arguments.of(6, Tariff.Type.NIGHT),
                Arguments.of(7, Tariff.Type.NIGHT),
                Arguments.of(8, Tariff.Type.DAY),
                Arguments.of(9, Tariff.Type.DAY),
                Arguments.of(10, Tariff.Type.DAY),
                Arguments.of(11, Tariff.Type.DAY),
                Arguments.of(12, Tariff.Type.DAY),
                Arguments.of(13, Tariff.Type.DAY),
                Arguments.of(14, Tariff.Type.DAY),
                Arguments.of(15, Tariff.Type.DAY),
                Arguments.of(16, Tariff.Type.DAY),
                Arguments.of(17, Tariff.Type.PEAK),
                Arguments.of(18, Tariff.Type.PEAK),
                Arguments.of(19, Tariff.Type.DAY),
                Arguments.of(20, Tariff.Type.DAY),
                Arguments.of(21, Tariff.Type.DAY),
                Arguments.of(22, Tariff.Type.DAY),
                Arguments.of(23, Tariff.Type.NIGHT));
    }
}
