package com.amcglynn.myenergi.aws;

public class ZappiCredentials {

    private final String userId;
    private final String serialNumber;
    private final String apiKey;

    public ZappiCredentials(String userId, String serialNumber, String apiKey) {
        this.userId = userId;
        this.serialNumber = serialNumber;
        this.apiKey = apiKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getApiKey() {
        return apiKey;
    }
}
