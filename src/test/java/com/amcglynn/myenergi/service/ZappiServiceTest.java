package com.amcglynn.myenergi.service;

import com.amcglynn.myenergi.MyEnergiClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ZappiServiceTest {

    @Mock
    private MyEnergiClient clientMock;

    @ParameterizedTest
    @MethodSource("boostWithDurationSource")
    void testBoostWithDurationWillBoostToTheNearest15Minutes(String startTime, Duration chargeDuration, String expectedEndTime) {
        Supplier<LocalTime> localTimeSupplier = () -> LocalTime.parse(startTime);
        var boostStopTime = new ZappiService(clientMock, localTimeSupplier)
                .startSmartBoost(chargeDuration);

        var timeFormatterForUrl = DateTimeFormatter.ofPattern("HHmm");
        assertThat(expectedEndTime).isEqualTo(boostStopTime.format(timeFormatterForUrl));
    }

    @ParameterizedTest
    @MethodSource("boostWithEndTime")
    void testBoostUntilTimeWillBoostToTheNearest15Minutes(String endTime, String expectedEndTime) {
        var boostStopTime = new ZappiService(clientMock, LocalTime::now)
                .startSmartBoost(LocalTime.parse(endTime));

        var timeFormatterForUrl = DateTimeFormatter.ofPattern("HHmm");
        assertThat(expectedEndTime).isEqualTo(boostStopTime.format(timeFormatterForUrl));
    }

    private static Stream<Arguments> boostWithDurationSource() {
        return Stream.of(
                Arguments.of("11:32:30", Duration.of(1, ChronoUnit.HOURS), "1230"),
                Arguments.of("23:32:30", Duration.of(1, ChronoUnit.HOURS), "0030"),
                Arguments.of("23:40:30", Duration.of(1, ChronoUnit.HOURS), "0045"),
                Arguments.of("23:44:30", Duration.of(1, ChronoUnit.HOURS), "0045"),
                Arguments.of("09:30:30", Duration.of(1, ChronoUnit.HOURS), "1030"),
                Arguments.of("01:00:59", Duration.of(1, ChronoUnit.HOURS), "0200"),
                // round up
                Arguments.of("01:08:59", Duration.of(1, ChronoUnit.HOURS), "0215"),
                Arguments.of("01:23:59", Duration.of(1, ChronoUnit.HOURS), "0230"),
                Arguments.of("01:38:59", Duration.of(1, ChronoUnit.HOURS), "0245"),
                Arguments.of("01:53:59", Duration.of(1, ChronoUnit.HOURS), "0300"),
                // round down
                Arguments.of("01:07:59", Duration.of(1, ChronoUnit.HOURS), "0200"),
                Arguments.of("01:22:59", Duration.of(1, ChronoUnit.HOURS), "0215"),
                Arguments.of("01:37:59", Duration.of(1, ChronoUnit.HOURS), "0230"),
                Arguments.of("01:52:59", Duration.of(1, ChronoUnit.HOURS), "0245"));
    }

    private static Stream<Arguments> boostWithEndTime() {
        return Stream.of(
                Arguments.of("12:32:30", "1230"),
                Arguments.of("00:32:30", "0030"),
                Arguments.of("00:40:30", "0045"),
                Arguments.of("00:44:30", "0045"),
                Arguments.of("10:30:30", "1030"),
                Arguments.of("02:00:59", "0200"),
                // round up
                Arguments.of("02:08:59", "0215"),
                Arguments.of("02:23:59", "0230"),
                Arguments.of("02:38:59", "0245"),
                Arguments.of("03:53:59", "0400"),
                // round down
                Arguments.of("02:07:59", "0200"),
                Arguments.of("02:22:59", "0215"),
                Arguments.of("02:37:59", "0230"),
                Arguments.of("02:52:59", "0245"));
    }
}
