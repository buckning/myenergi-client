package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ZappiDaySummary;

public class ZappiDaySummaryVoiceResponse {
    private String response;

    public ZappiDaySummaryVoiceResponse(ZappiDaySummary summary) {
        response = "Imported " + summary.getImported() + " kilowatt hours. ";
        response += "Exported " + summary.getExported() + " kilowatt hours. ";
        response += "Consumed " + summary.getConsumed() + " kilowatt hours. ";
        response += "Solar generation was " + summary.getSolarGeneration() + " kilowatt hours. ";
        response += "Charged " + summary.getEvSummary().getTotal() + " kilowatt hours to your E.V.";
        if (summary.getSampleSize() < 24) {
            response += "Note that there are missing data points so this reading is not completely accurate. ";
        }
    }

    @Override
    public String toString() {
        return response;
    }
}
