package com.amcglynn.myenergi;

import lombok.Getter;

public enum ChargeStatus {
    STARTING(0),
    PAUSED(1),
    DSR(2),
    DIVERTING(3),
    BOOSTING(4),
    COMPLETE(5);

    @Getter
    private final int boostValue;

    ChargeStatus(int boostValue) {
        this.boostValue = boostValue;
    }
}
