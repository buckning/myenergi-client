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
    @Getter final Type type;
    @Getter
    private final double costPerKwh;


    // build ranges instead of start and end time
    public Tariff(Type type, int startTime, int endTime, double costPerKwh) {
        this.type = type;
        this.costPerKwh = costPerKwh;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
