package com.amcglynn.myenergi;

import lombok.Getter;

public enum ZappiBoostMode {
    // 2=Stop, 10=Boost, 11=SmartBoost
    STOP(2),
    BOOST(10),
    SMART_BOOST(11),
    OFF(0);

    @Getter
    private final int boostValue;

    ZappiBoostMode(int boostValue) {
        this.boostValue = boostValue;
    }
}
