package com.amcglynn.myenergi.units;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class KiloWattHour {

    private double value;

    public KiloWattHour(Joule joule) {
        value = (double) joule.getLong() / 3600000;
    }

    public KiloWattHour(double value) {
        this.value = value;
    }

    public double getDouble() {
        return value;
    }

    public void add(KiloWattHour kiloWattHour) {
        this.value += kiloWattHour.getDouble();
    }
}
