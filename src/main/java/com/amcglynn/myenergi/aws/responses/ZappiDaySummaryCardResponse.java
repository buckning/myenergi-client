package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ZappiDaySummary;

public class ZappiDaySummaryCardResponse {
    private String response;

    public ZappiDaySummaryCardResponse(ZappiDaySummary summary) {
        response = "Imported: " + summary.getImported() + "kWh\n";
        response += "Exported: " + summary.getExported() + "kWh\n";
        response += "Consumed: " + summary.getConsumed() + "kWh\n";
        response += "Solar generated: " + summary.getSolarGeneration() + "kWh\n";
        response += "Charged: " + summary.getEvSummary().getTotal() + "kWh\n";
        if (summary.getSampleSize() < 24) {
            response += "Note that there are missing data points so this reading is not completely accurate.";
        }
    }

    @Override
    public String toString() {
        return response;
    }
}
