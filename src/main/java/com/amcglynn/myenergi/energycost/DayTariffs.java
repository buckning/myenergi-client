package com.amcglynn.myenergi.energycost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayTariffs {

    // add all the tariffs for the day
    private Map<Integer, Tariff> hourlyTariffs;

    private List<Tariff> tariffs;

    public DayTariffs() {
        tariffs = new ArrayList<>();
        tariffs.add(new Tariff(Tariff.Type.NIGHT, 0, 8, 0.2092));
        tariffs.add(new Tariff(Tariff.Type.DAY, 8, 17, 0.4241));
        tariffs.add(new Tariff(Tariff.Type.PEAK, 17, 19, 0.4241));
        tariffs.add(new Tariff(Tariff.Type.DAY, 19, 23, 0.4241));
        tariffs.add(new Tariff(Tariff.Type.NIGHT, 23, 24, 0.2092));

        hourlyTariffs = new HashMap<>();
        buildHourlyTariffMap();
    }

    /**
     * Build hourly slots
     */
    private void buildHourlyTariffMap() {
        // loop over each and make sure there's no overlap
        for (var tariff : tariffs) {
            // get all hours from start to end
            for (int i = tariff.getStartTime(); i < tariff.getEndTime(); i++) {
                var existingTariff = hourlyTariffs.get(i);
                if (existingTariff != null) {
                    throw new IllegalArgumentException("Overlap in tariffs");
                }
                hourlyTariffs.put(i, tariff);
            }
        }
    }

    public Tariff getTariff(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Can only get a tariff between 0 - 23");
        }
        return hourlyTariffs.get(hour);
    }
}
