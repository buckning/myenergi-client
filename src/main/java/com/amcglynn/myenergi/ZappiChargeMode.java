package com.amcglynn.myenergi;

import lombok.Getter;

public enum ZappiChargeMode {
    //1=Fast, 2=Eco, 3=Eco+, 4=Stopped
    FAST("Fast"),
    ECO("Eco"),
    ECO_PLUS("Eco+"),
    STOP("Stop");

    @Getter
    private String displayName;

    ZappiChargeMode(String displayName) {
        this.displayName = displayName;
    }

    public int getApiValue() {
        return ordinal() + 1;
    }
}
