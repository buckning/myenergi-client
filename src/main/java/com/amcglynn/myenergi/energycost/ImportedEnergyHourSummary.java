package com.amcglynn.myenergi.energycost;

import com.amcglynn.myenergi.apiresponse.ZappiHistory;
import com.amcglynn.myenergi.units.KiloWattHour;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Information about the imported energy for this hour, including the electricity tariff for this time
 */
public class ImportedEnergyHourSummary {
    private static final DayTariffs TARIFFS = new DayTariffs();

    private LocalDateTime date;
    private KiloWattHour kiloWattHour;
    private Tariff tariff;

    public ImportedEnergyHourSummary(ZappiHistory history) {
        this.kiloWattHour = new KiloWattHour(history.getImported());
        tariff = TARIFFS.getTariff(history.getHour());
        date = LocalDateTime.of(history.getYear(), Month.of(history.getMonth()), history.getDayOfMonth(),
                history.getHour(), 0);
    }

    public double getCost() {
        return tariff.getCostPerKwh() * kiloWattHour.getDouble();
    }
}
