package com.amcglynn.myenergi.service;

import com.amcglynn.myenergi.MyEnergiClient;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.ZappiDaySummary;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.units.KiloWattHour;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ZappiService {

    private final MyEnergiClient client;
    private final Supplier<LocalTime> localTimeSupplier;

    public ZappiService() {
        var apiKey = System.getenv("myEnergiHubApiKey");
        var serialNumber = System.getenv("myEnergiHubSerialNumber");
        this.client = new MyEnergiClient(serialNumber, apiKey);
        this.localTimeSupplier = LocalTime::now;
    }

    public ZappiService(MyEnergiClient client, Supplier<LocalTime> localTimeSupplier) {
        this.client = client;
        this.localTimeSupplier = localTimeSupplier;
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

    public void stopBoost() {
        client.stopBoost();
    }

    public ZappiDaySummary getEnergyUsage(LocalDate localDate) {
        return new ZappiDaySummary(client.getZappiHistory(localDate).getReadings());
    }

    public void getEnergyUsage(YearMonth yearMonth) {

    }

    public void getEnergyUsage(Year year) {

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
