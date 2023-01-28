package com.amcglynn.myenergi.units;

public class Watt {
    private Long value;

    public Watt(Long value) {
        this.value = value;
    }

    public Watt(Watt watt) {
        this(watt.value);
    }

    public Long getLong() {
        return value;
    }

    public Watt add(Watt watt) {
        return new Watt(value + watt.value);
    }

    public Watt subtract(Watt watt) {
        return new Watt(value - watt.value);
    }

    public String toString() {
        return value + "W";
    }
}

