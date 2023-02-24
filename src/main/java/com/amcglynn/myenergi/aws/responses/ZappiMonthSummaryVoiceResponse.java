package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.ZappiMonthSummary;

import java.time.format.TextStyle;
import java.util.Locale;

public class ZappiMonthSummaryVoiceResponse {
    private String response;

    public ZappiMonthSummaryVoiceResponse(ZappiMonthSummary summary) {
        response = "Here's your usage for " + summary.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + summary.getYearMonth().getYear() + ". ";
        response += "Imported " + summary.getImported() + " kilowatt hours. ";
        response += "Exported " + summary.getExported() + " kilowatt hours. ";
        response += "Solar generation was " + summary.getSolarGeneration() + " kilowatt hours. ";
        response += "Charged " + summary.getEvTotal() + " kilowatt hours to your E.V.";
    }

    @Override
    public String toString() {
        return response;
    }
}
