package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.EvStatusSummary;

public class ZappiEvConnectionStatusCardResponse {

    private String response;

    public ZappiEvConnectionStatusCardResponse(EvStatusSummary summary) {
        if (summary.isConnected() && summary.isFinishedCharging()) {
            response = "Your E.V. is finished charging. " +
                    summary.getChargeAddedThisSession() + "kWh added this session.\n";
        } else {
            response = "Your E.V. is " + (summary.isConnected() ? "connected" : "not connected") + ".\n";
        }
        if (summary.getChargeRate().getDouble() >= 0.1) {
            response += "Charge rate is " + summary.getChargeRate();
        }
    }

    @Override
    public String toString() {
        return response;
    }
}
