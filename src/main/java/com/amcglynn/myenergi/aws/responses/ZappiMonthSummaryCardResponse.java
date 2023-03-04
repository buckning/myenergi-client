package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ZappiMonthSummary;

import java.time.format.TextStyle;
import java.util.Locale;

public class ZappiMonthSummaryCardResponse {
    private String response;

    public ZappiMonthSummaryCardResponse(ZappiMonthSummary summary) {
        response = "Usage for " + summary.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + summary.getYearMonth().getYear() + "\n";
        response += "Imported: " + summary.getImported() + "kWh\n";
        response += "Exported: " + summary.getExported() + "kWh\n";
        response += "Solar generated: " + summary.getSolarGeneration() + "kWh\n";
        response += "Charged: " + summary.getEvTotal() + "kWh\n";
    }

    @Override
    public String toString() {
        return response;
    }
}
