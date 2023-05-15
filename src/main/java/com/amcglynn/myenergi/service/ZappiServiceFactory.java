package com.amcglynn.myenergi.service;

public class ZappiServiceFactory {

    public ZappiService newZappiService(String userId) {
        return new ZappiService(userId);
    }
}
