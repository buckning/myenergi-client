package com.amcglynn.myenergi.service;

import com.amcglynn.myenergi.MyEnergiClient;
import com.amcglynn.myenergi.ZappiChargeMode;
import com.amcglynn.myenergi.ZappiStatusSummary;

import java.util.List;
import java.util.stream.Collectors;

public class ZappiService {

    private final MyEnergiClient client;

    public ZappiService() {
        var apiKey = System.getenv("myEnergiHubApiKey");
        var serialNumber = System.getenv("myEnergiHubSerialNumber");
        this.client = new MyEnergiClient(serialNumber, apiKey);
    }

    public List<ZappiStatusSummary> getStatusSummary() {
        return client.getZappiStatus().getZappi()
                .stream().map(ZappiStatusSummary::new).collect(Collectors.toList());
    }

    public void setChargeMode(ZappiChargeMode chargeMode) {
        client.setZappiChargeMode(chargeMode);
    }
}
