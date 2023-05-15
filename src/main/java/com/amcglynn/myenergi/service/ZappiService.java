package com.amcglynn.myenergi.service;

import com.amcglynn.myenergi.MyEnergiClient;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.ZappiDaySummary;
import com.amcglynn.myenergi.ZappiMonthSummary;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.aws.ZappiCredentialRepository;
import com.amcglynn.myenergi.energycost.ImportedEnergyHourSummary;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.units.KiloWattHour;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ZappiService {

    private final MyEnergiClient client;
    private final Supplier<LocalTime> localTimeSupplier;
    private ZappiServiceNotificationListener listener;

    public ZappiService(String userId) {
        this.localTimeSupplier = LocalTime::now;
        var repository = new ZappiCredentialRepository();
        var creds = repository.readData(userId);
        if (creds.isPresent()) {
            client = new MyEnergiClient(creds.get().getSerialNumber(), creds.get().getApiKey());
        } else {
            throw new ClientException(0);
        }
    }

    public ZappiService(MyEnergiClient client, Supplier<LocalTime> localTimeSupplier) {
        this.localTimeSupplier = localTimeSupplier;
        this.client = client;
    }

    public List<ZappiStatusSummary> getStatusSummary() {
        return client.getZappiStatus().getZappi()
                .stream().map(ZappiStatusSummary::new).collect(Collectors.toList());
    }

    public void setChargeMode(ZappiChargeMode chargeMode) {
        client.setZappiChargeMode(chargeMode);
    }

    public void startBoost(final KiloWattHour targetChargeAmount) {
        client.boost(targetChargeAmount);
    }

    public LocalTime startSmartBoost(final Duration duration) {
        var boostEndTime = roundToNearest15Mins(duration);
        client.boost(boostEndTime);
        return boostEndTime;
    }

    public LocalTime startSmartBoost(final LocalTime endTime) {
        var boostEndTime = roundToNearest15Mins(endTime);
        client.boost(boostEndTime);
        return boostEndTime;
    }

    public void registerNotificationListener(ZappiServiceNotificationListener listener) {
        this.listener = listener;
    }

    public void stopBoost() {
        client.stopBoost();
    }

    public ZappiDaySummary getEnergyUsage(LocalDate localDate) {
        return new ZappiDaySummary(client.getZappiHistory(localDate).getReadings());
    }

    public ZappiMonthSummary getEnergyUsage(YearMonth yearMonth) {
        var result = IntStream.range(1, yearMonth.lengthOfMonth() + 1)
                .mapToObj(day -> {
                    listener.notify(day, yearMonth.lengthOfMonth() + 1);
                    return client.getZappiHistory(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day));
                })
                .map(dayHistory -> new ZappiDaySummary(dayHistory.getReadings()))
                .collect(Collectors.toList());
        return new ZappiMonthSummary(yearMonth, result);
    }

    public List<ImportedEnergyHourSummary> getHourlySummary(LocalDate date) {
        return client.getZappiHourlyHistory(date).getReadings().stream()
                .map(ImportedEnergyHourSummary::new)
                .collect(Collectors.toList());
    }

    private LocalTime roundToNearest15Mins(Duration duration) {
        var targetChargeEndTime = localTimeSupplier.get().plus(duration);
        return roundToNearest15Mins(targetChargeEndTime);
    }

    private LocalTime roundToNearest15Mins(LocalTime endTime) {
        var overflow15Minutes = endTime.getMinute() % 15;
        if (overflow15Minutes > 7) {
            return endTime.plus((15 - overflow15Minutes), ChronoUnit.MINUTES);
        }
        return endTime.minus(overflow15Minutes, ChronoUnit.MINUTES);
    }
}
