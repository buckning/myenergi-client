package com.amcglynn.myenergi.aws.responses;

import java.time.LocalDate;

public class ZappiEnergyCostVoiceResponse {
    private String response;

    public ZappiEnergyCostVoiceResponse(LocalDate date, double cost) {
        response = "Cost for " + date + " is €" + String.format("%.2f", cost);
    }

    @Override
    public String toString() {
        return response;
    }
}
