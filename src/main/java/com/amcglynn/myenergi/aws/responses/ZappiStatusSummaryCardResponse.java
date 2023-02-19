package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ChargeStatus;
import com.amcglynn.myenergi.EvConnectionStatus;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.units.KiloWatt;

public class ZappiStatusSummaryCardResponse {
    private String response;

    public ZappiStatusSummaryCardResponse(ZappiStatusSummary summary) {
        response = "";
        response += getSolarGeneration(summary);
        response += getGridExport(summary);
        response += getGridImport(summary);
        response += getChargingRate(summary);
        response += getChargeMode(summary);
        response += getBoostMode(summary);
        response += getChargeAdded(summary);
    }

    private String getSolarGeneration(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGenerated().getLong() >= 100L) {
            str += "Solar: " + new KiloWatt(summary.getGenerated()) + "kW\n";
        }
        return str;
    }

    private String getGridExport(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGridExport().getLong() > 0L) {
            str += "Export: " + new KiloWatt(summary.getGridExport()) + "kW\n";
        }
        return str;
    }

    private String getGridImport(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGridImport().getLong() > 0L) {
            str += "Import: " + new KiloWatt(summary.getGridImport()) + "kW\n";
        }
        return str;
    }

    private String getChargingRate(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getEvConnectionStatus() == EvConnectionStatus.CHARGING) {
            str += "Charge rate: " + new KiloWatt(summary.getEvChargeRate()) + "kW\n";
        }
        return str;
    }

    private String getBoostMode(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getChargeStatus() == ChargeStatus.BOOSTING) {
            str += "Boost mode: enabled\n";
        }
        return str;
    }

    private String getChargeMode(ZappiStatusSummary summary) {
        return "Charge mode: " + summary.getChargeMode().getDisplayName() + "\n";
    }

    private String getChargeAdded(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getChargeAddedThisSession().getDouble() > 0.01) {
            str += "Charge added: " + summary.getChargeAddedThisSession() + "kWh\n";
        }
        return str;
    }

    @Override
    public String toString() {
        return response;
    }
}
