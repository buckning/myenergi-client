package com.amcglynn.myenergi;

import com.amcglynn.myenergi.units.KiloWatt;

import java.util.List;

public class EvStatusSummary {

    private static final List<EvConnectionStatus> CONNECTED_STATUSES = List.of(EvConnectionStatus.EV_CONNECTED,
            EvConnectionStatus.READY_TO_CHARGE, EvConnectionStatus.CHARGING);

    private EvConnectionStatus evConnectionStatus;
    private KiloWatt chargeRate;

    public EvStatusSummary(ZappiStatusSummary status) {
        this.evConnectionStatus = status.getEvConnectionStatus();
        this.chargeRate = new KiloWatt(status.getEvChargeRate());
    }

    public KiloWatt getChargeRate() {
        return chargeRate;
    }

    public boolean isConnected() {
        return CONNECTED_STATUSES.contains(evConnectionStatus);
    }
}
