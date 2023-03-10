package com.amcglynn.myenergi.aws.responses;

import java.time.LocalDate;

public class ZappiEnergyCostVoiceResponse {
    private String response;

    public ZappiEnergyCostVoiceResponse(LocalDate date, double importCost, double exportCost) {
        response = "Cost for " + date + " is €" + String.format("%.2f", importCost) + ". ";
        response += "You exported €" + String.format("%.2f", exportCost);
    }

    @Override
    public String toString() {
        return response;
    }
}
