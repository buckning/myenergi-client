package com.amcglynn.myenergi.aws.responses;

import java.time.LocalDate;

public class ZappiEnergyCostVoiceResponse {
    private String response;

    public ZappiEnergyCostVoiceResponse(LocalDate date, double importCost, double exportCost,
                                        double solarConsumptionSavings) {
        response = "Cost for " + date + " is €" + String.format("%.2f", importCost) + ". ";
        response += "You exported €" + String.format("%.2f", exportCost) + ". ";
        response += "You saved €" + String.format("%.2f", solarConsumptionSavings) + ". ";
        response += "Total saved €" + String.format("%.2f", solarConsumptionSavings + exportCost) + ". ";
    }

    @Override
    public String toString() {
        return response;
    }
}
