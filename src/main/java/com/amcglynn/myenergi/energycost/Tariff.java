package com.amcglynn.myenergi.energycost;

import lombok.Getter;

public class Tariff {

    public enum Type {
        DAY,
        NIGHT,
        PEAK
    }

    @Getter
    private final int startTime;
    @Getter
    private final int endTime;
    @Getter
    private final Type type;
    @Getter
    private final double importCostPerKwh;
    @Getter
    private final double exportCostPerKwh;


    // build ranges instead of start and end time
    public Tariff(Type type, int startTime, int endTime, double importCostPerKwh, double exportCostPerKwh) {
        this.type = type;
        this.importCostPerKwh = importCostPerKwh;
        this.exportCostPerKwh = exportCostPerKwh;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
