package com.amcglynn.myenergi.units;


public class Joule {
    private Long value;

    public Joule() {
        this.value = 0L;
    }

    public Joule(long value) {
        this.value = value;
    }

    public Long getLong() {
        return value;
    }

    public void add(Joule joule) {
        value += joule.getLong();
    }
}
