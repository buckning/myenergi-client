package com.amcglynn.myenergi.units;


public class Joule {
    private final Long value;

    public Joule() {
        this.value = 0L;
    }

    public Joule(long value) {
        this.value = value;
    }

    public Long getLong() {
        return value;
    }

    public Joule add(Joule joule) {
        return new Joule(value + joule.getLong());
    }

    public Joule subtract(Joule joule) {
        return new Joule(value - joule.getLong());
    }
}
