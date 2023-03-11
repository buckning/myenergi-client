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
    private KiloWattHour imported;
    private KiloWattHour exported;
    private KiloWattHour solarConsumption;
    private Tariff tariff;

    public ImportedEnergyHourSummary(ZappiHistory history) {
        this.imported = new KiloWattHour(history.getImported());
        this.exported = new KiloWattHour(history.getGridExport());
        this.solarConsumption = new KiloWattHour(history.getSolarGeneration());
        tariff = TARIFFS.getTariff(history.getHour());
        date = LocalDateTime.of(history.getYear(), Month.of(history.getMonth()), history.getDayOfMonth(),
                history.getHour(), 0);
    }

    public double getSolarSavings() {
        // consumption is what is generate minus what is exported. Saving is based on what the cost of import
        return tariff.getImportCostPerKwh() * (solarConsumption.minus(exported).getDouble());
    }

    public double getImportCost() {
        return tariff.getImportCostPerKwh() * imported.getDouble();
    }

    public double getExportCost() {
        return tariff.getExportCostPerKwh() * exported.getDouble();
    }
}
