package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ChargeStatus;
import com.amcglynn.myenergi.EvConnectionStatus;
import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.units.KiloWatt;

public class ZappiStatusSummaryVoiceResponse {
    private String response;

    public ZappiStatusSummaryVoiceResponse(ZappiStatusSummary summary) {
        response = "";
        response += getSolarGeneration(summary);
        response += getGridExport(summary);
        response += getGridImport(summary);
        response += getChargingRate(summary);
        response += getChargeMode(summary);
        response += getChargeAdded(summary);
    }

    private String getSolarGeneration(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGenerated().getLong() >= 100L) {
            str += "Solar generation is " + new KiloWatt(summary.getGenerated()) + " kilowatts. ";
        }
        return str;
    }

    private String getGridExport(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGridExport().getLong() > 0L) {
            str += "Exporting " + new KiloWatt(summary.getGridExport()) + " kilowatts. ";
        }
        return str;
    }

    private String getGridImport(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGridImport().getLong() > 0L) {
            str += "Importing " + new KiloWatt(summary.getGridImport()) + " kilowatts. ";
        }
        return str;
    }

    private String getChargingRate(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getEvConnectionStatus() == EvConnectionStatus.CHARGING) {
            var chargeType = summary.getChargeStatus() == ChargeStatus.BOOSTING ? "Boosting " : "Charging ";
            str += chargeType + new KiloWatt(summary.getEvChargeRate()) + " kilowatts to your E.V. - ";
        }
        return str;
    }

    private String getChargeMode(ZappiStatusSummary summary) {
        return "Charge mode is " + summary.getChargeMode().getDisplayName() + ". ";
    }

    private String getChargeAdded(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getChargeAddedThisSession().getDouble() > 0.0) {
            str += "Charge added this session is " + summary.getChargeAddedThisSession() + " kilowatt hours. ";
        }
        return str;
    }

    @Override
    public String toString() {
        return response;
    }
}
