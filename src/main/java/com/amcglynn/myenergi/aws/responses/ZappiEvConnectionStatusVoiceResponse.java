package com.amcglynn.myenergi.aws.responses;

import com.amcglynn.myenergi.EvStatusSummary;

public class ZappiEvConnectionStatusVoiceResponse {

    private String response;

    public ZappiEvConnectionStatusVoiceResponse(EvStatusSummary summary) {
        if (summary.isConnected() && summary.isFinishedCharging()) {
            response = "Your E.V. is finished charging. " +
                    summary.getChargeAddedThisSession() + " kilowatt hours added this session. ";
        } else {
            response = "Your E.V. is " + (summary.isConnected() ? "connected" : "not connected") + ". ";
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
