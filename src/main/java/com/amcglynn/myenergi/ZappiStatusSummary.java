package com.amcglynn.myenergi;

import com.amcglynn.myenergi.apiresponse.ZappiStatus;
import com.amcglynn.myenergi.units.KiloWattHour;
import com.amcglynn.myenergi.units.Watt;
import lombok.Getter;
import lombok.ToString;

/**
 * This class converts the raw values from the API and provides some convenience methods.
 */
@Getter
@ToString
public class ZappiStatusSummary {

    private Watt gridImport;
    private Watt gridExport;
    private Watt consumed;
    private Watt generated;
    private Watt evChargeRate;
    private KiloWattHour chargeAddedThisSession;
    private EvConnectionStatus evConnectionStatus;
    private ZappiChargeMode chargeMode;
    private ChargeStatus chargeStatus;

    public ZappiStatusSummary(ZappiStatus zappiStatus) {
        gridImport = new Watt(Math.max(0, zappiStatus.getGridWatts()));
        gridExport = new Watt(Math.abs(Math.min(0, zappiStatus.getGridWatts())));
        generated = new Watt(zappiStatus.getSolarGeneration());
        consumed = new Watt(generated).add(gridImport).subtract(gridExport);
        // consumed  - charge can be broken down to house and car. House = (consumed - charge)

        chargeStatus = ChargeStatus.values()[zappiStatus.getChargeStatus()];
        chargeMode = ZappiChargeMode.values()[zappiStatus.getZappiChargeMode()];

        chargeAddedThisSession = new KiloWattHour(zappiStatus.getChargeAddedThisSessionKwh());
        evChargeRate = new Watt(zappiStatus.getCarDiversionAmountWatts());
        evConnectionStatus = EvConnectionStatus.fromString(zappiStatus.getEvConnectionStatus());
    }
}
