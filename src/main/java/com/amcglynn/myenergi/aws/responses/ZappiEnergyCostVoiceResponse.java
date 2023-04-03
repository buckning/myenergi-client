package com.amcglynn.myenergi.aws.responses;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class ZappiEnergyCostVoiceResponse {
    private String response;

    public ZappiEnergyCostVoiceResponse(LocalDate date, double importCost, double exportCost,
                                        double solarConsumptionSavings) {
        response = "Total cost for " + date + " is " + getEuroCost(importCost - exportCost) + ". ";
        response += "You imported " + getEuroCost(importCost) + ". ";
        response += "You exported " + getEuroCost(exportCost) + ". ";
        response += "Total saved " + getEuroCost(solarConsumptionSavings + exportCost) + ". ";
    }

    String getEuroCost(double euro) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(euro));
        int intValue = bigDecimal.intValue();

        String result = "";
        if (intValue >= 1) {
            result += intValue + " Euro and ";
        }
        BigDecimal cents = bigDecimal.subtract(bigDecimal.setScale(0, RoundingMode.FLOOR))
                .movePointRight(2)
                .abs()
                .remainder(BigDecimal.valueOf(100));
        String centsString = cents.setScale(0, RoundingMode.FLOOR).toPlainString();
        result += centsString + " cent";
        return result;
    }

    @Override
    public String toString() {
        return response;
    }
}
