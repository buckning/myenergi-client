package com.amcglynn.myenergi;

import com.amcglynn.myenergi.units.KiloWattHour;
import lombok.Getter;
import lombok.ToString;

import java.time.YearMonth;
import java.util.List;

@ToString
@Getter
public class ZappiMonthSummary {
    private YearMonth yearMonth;
    private final KiloWattHour solarGeneration;
    private final KiloWattHour exported;
    private final KiloWattHour imported;
    private final KiloWattHour evTotal;

    public ZappiMonthSummary(YearMonth yearMonth, List<ZappiDaySummary> dataPoints) {
        this.yearMonth = yearMonth;
        var solarGenerationTemp = new KiloWattHour(0);
        var exportedTemp = new KiloWattHour(0);
        var importedTemp = new KiloWattHour(0);
        var evTotalTemp = new KiloWattHour(0);

        for (var dp : dataPoints) {
            solarGenerationTemp = solarGenerationTemp.add(dp.getSolarGeneration());
            exportedTemp = exportedTemp.add(dp.getExported());
            importedTemp = importedTemp.add(dp.getImported());
            evTotalTemp = evTotalTemp.add(dp.getEvSummary().getTotal());
        }

        this.solarGeneration = new KiloWattHour(solarGenerationTemp);
        this.exported = new KiloWattHour(exportedTemp);
        this.imported = new KiloWattHour(importedTemp);
        this.evTotal = new KiloWattHour(evTotalTemp);
    }
}
