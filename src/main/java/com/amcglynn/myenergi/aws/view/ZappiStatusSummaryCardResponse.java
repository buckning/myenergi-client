package com.amcglynn.myenergi.aws.view;

import com.amcglynn.myenergi.ZappiStatusSummary;
import com.amcglynn.myenergi.units.KiloWatt;

public class ZappiStatusSummaryCardResponse {
    private String responseStr;

    public ZappiStatusSummaryCardResponse(ZappiStatusSummary summary) {
        responseStr = "";
        responseStr += getSolarGeneration(summary);
        responseStr += getGridImport(summary);
        responseStr += getChargingInformation(summary);
    }

    private String getSolarGeneration(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGenerated().getLong() > 0L) {
            str += "Solar generation is " + new KiloWatt(summary.getGenerated()) + " KiloWatts.\n";
        }
        return str;
    }

    private String getGridImport(ZappiStatusSummary summary) {
        String str = "";
        if (summary.getGridImport().getLong() > 0L) {
            str += "Importing " + new KiloWatt(summary.getGridImport()) + " KiloWatts from the grid\n";
        }
        return str;
    }

    private String getChargingInformation(ZappiStatusSummary summary) {
        var str = "Charge mode is " + summary.getChargeMode() + "\n";
        str += "Charge added this session is " + summary.getChargeAddedThisSession() + " KiloWatt Hours\n";
        return str;
    }

    @Override
    public String toString() {
        return responseStr;
    }
}
