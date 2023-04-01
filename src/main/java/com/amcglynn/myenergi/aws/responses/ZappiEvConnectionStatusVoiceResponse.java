package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.EvStatusSummary;

public class ZappiEvConnectionStatusVoiceResponse {

    private String response;

    public ZappiEvConnectionStatusVoiceResponse(EvStatusSummary summary) {
        response = "Your E.V. is " + (summary.isConnected() ? "connected" : "not connected") + ". ";
        if (summary.getChargeRate().getDouble() >= 0.1) {
            response += "Charge rate is " + summary.getChargeRate();
        }
    }

    @Override
    public String toString() {
        return response;
    }
}
