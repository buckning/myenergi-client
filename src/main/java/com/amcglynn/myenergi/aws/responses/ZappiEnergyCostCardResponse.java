package com.amcglynn.myenergi.aws.responses;

import java.time.LocalDate;

public class ZappiEnergyCostCardResponse {
    private String response;

    public ZappiEnergyCostCardResponse(LocalDate date, double importCost, double exportCost) {
        response = "Import cost: €" + String.format("%.2f", importCost) + "\n";
        response += "Export cost: €" + String.format("%.2f", exportCost) + "\n";
        response += "Date: " + date;
    }

    @Override
    public String toString() {
        return response;
    }
}
