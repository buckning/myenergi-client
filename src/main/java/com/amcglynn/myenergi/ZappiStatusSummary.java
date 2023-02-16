package com.amcglynn.myenergi;

import com.amcglynn.myenergi.apiresponse.ZappiStatus;
import com.amcglynn.myenergi.units.KiloWattHour;
import com.amcglynn.myenergi.units.Watt;
import lombok.Getter;
import lombok.ToString;

/**
 * This class converts the raw values from the API and provides some convenience methods.
 */
@ToString
public class ZappiStatusSummary {

    @Getter
    private Watt gridImport;
    @Getter
    private Watt gridExport;
    @Getter
    private Watt consumed;
    @Getter
    private Watt generated;
    private Watt charge;
    private KiloWattHour chargeAddedThisSession;
    private EvConnectionStatus evConnectionStatus;
    private ZappiChargeMode chargeMode;

    public ZappiStatusSummary(ZappiStatus zappiStatus) {
        gridImport = new Watt(Math.max(0, zappiStatus.getGridWatts()));
        gridExport = new Watt(Math.abs(Math.min(0, zappiStatus.getGridWatts())));
        generated = new Watt(zappiStatus.getSolarGeneration());
        consumed = new Watt(generated).add(gridImport).subtract(gridExport);
        // consumed  - charge can be broken down to house and car. House = (consumed - charge)

        chargeMode = ZappiChargeMode.values()[zappiStatus.getZappiChargeMode() - 1];

        chargeAddedThisSession = new KiloWattHour(zappiStatus.getChargeAddedThisSessionKwh());
        charge = new Watt(zappiStatus.getCarDiversionAmountWatts());
        evConnectionStatus = EvConnectionStatus.fromString(zappiStatus.getEvConnectionStatus());
    }
}
