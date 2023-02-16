package com.amcglynn.myenergi;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.util.HashMap;
import java.util.Map;

public enum EvConnectionStatus {
    EV_DISCONNECTED("A"),
    EV_CONNECTED("B1"),     // plugged in, waiting for PV charge (awaiting surplus)
    WAITING_FOR_EV("B2"),   // plugged in, charging is complete
    READY_TO_CHARGE("C1"),
    CHARGING("C2"),         // plugged in, charging from PV or grid
    FAULT("F"),
    UNKNOWN("U");

    private static final Map<String, EvConnectionStatus> CODES = new HashMap<>();

    static {
        for (EvConnectionStatus status : values()) {
            CODES.put(status.code, status);
        }
    }

    private String code;

    EvConnectionStatus(String code) {
        this.code = code;
    }

    public static EvConnectionStatus fromString(String s) {
        var status = CODES.get(s);
        if (status == null) {
            status = UNKNOWN;
        }
        return status;
    }
}
