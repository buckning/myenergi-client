package com.amcglynn.myenergi.aws.responses;

import java.time.LocalDate;

public class ZappiEnergyCostCardResponse {
    private String response;

    public ZappiEnergyCostCardResponse(LocalDate date, double importCost, double exportCost,
                                       double solarConsumptionSavings) {
        response = "Total cost: €" + String.format("%.2f", importCost - exportCost) + "\n";
        response += "Import cost: €" + String.format("%.2f", importCost) + "\n";
        response += "Export cost: €" + String.format("%.2f", exportCost) + "\n";
        response += "Solar consumed saved: €" + String.format("%.2f", solarConsumptionSavings) + "\n";
        response += "Total saved: €" + String.format("%.2f", solarConsumptionSavings + exportCost) + "\n";
        response += "Date: " + date;
    }

    @Override
    public String toString() {
        return response;
    }
}
