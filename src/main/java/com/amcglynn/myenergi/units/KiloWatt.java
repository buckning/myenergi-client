package com.amcglynn.myenergi.units;

public class KiloWatt {
    private final double value;

    public KiloWatt(Watt watt) {
        this.value = (double) watt.getLong() / 1000;
    }

    public double getDouble() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%.1f",value);
    }
}
