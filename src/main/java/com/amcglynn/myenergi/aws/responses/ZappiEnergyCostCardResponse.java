package com.amcglynn.myenergi.aws.responses;

import java.time.LocalDate;

public class ZappiEnergyCostCardResponse {
    private String response;

    public ZappiEnergyCostCardResponse(LocalDate date, double cost) {
        response = "Import cost: €" + String.format("%.2f", cost) + "\n";
        response += "Date: " + date;
    }

    @Override
    public String toString() {
        return response;
    }
}
