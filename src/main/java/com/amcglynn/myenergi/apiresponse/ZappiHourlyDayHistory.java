package com.amcglynn.myenergi.apiresponse;

public class ZappiHourlyDayHistory extends ZappiDayHistory {
    private static final int HOURS_PER_DAY = 24;
    @Override
    public int getExpectedReadings() {
        return HOURS_PER_DAY;
    }
}
