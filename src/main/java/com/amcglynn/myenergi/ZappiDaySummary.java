package com.amcglynn.myenergi;

import com.amcglynn.myenergi.apiresponse.ZappiHistory;
import com.amcglynn.myenergi.units.Joule;
import com.amcglynn.myenergi.units.KiloWattHour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ZappiDaySummary {
    private final KiloWattHour solarGeneration;
    private final KiloWattHour exported;
    private final KiloWattHour imported;
    private final KiloWattHour consumed;
    private final EvSummary evSummary;
    private int sampleSize;

    public ZappiDaySummary(List<ZappiHistory> dataPoints) {
        var solarGenerationJoules = new Joule();
        var exportedJoules = new Joule();
        var importedJoules = new Joule();
        var evBoostJoules = new Joule();
        var evDivertedJoules = new Joule();

        detectMissingDataPoints(dataPoints);

        for (var dp : dataPoints) {
            solarGenerationJoules = solarGenerationJoules.add(dp.getSolarGeneration());
            evBoostJoules = evBoostJoules.add(dp.getBoost());
            evDivertedJoules = evDivertedJoules.add(dp.getZappiDiverted());
            exportedJoules = exportedJoules.add(dp.getGridExport());
            importedJoules = importedJoules.add(dp.getImported());
            sampleSize++;
        }

        this.solarGeneration = new KiloWattHour(solarGenerationJoules);
        this.exported = new KiloWattHour(exportedJoules);
        this.imported = new KiloWattHour(importedJoules);
        this.consumed = new KiloWattHour(solarGenerationJoules.add(importedJoules).subtract(exportedJoules));
        this.evSummary = new EvSummary(
                new KiloWattHour(evDivertedJoules),
                new KiloWattHour(evBoostJoules),
                new KiloWattHour(evBoostJoules.add(evDivertedJoules)));
    }

    @AllArgsConstructor
    public static class EvSummary {
        private KiloWattHour diverted;
        private KiloWattHour boost;
        private KiloWattHour total;

        public KiloWattHour getDiverted() {
            return this.diverted;
        }

        public KiloWattHour getBoost() {
            return this.boost;
        }

        public KiloWattHour getTotal() {
            return this.total;
        }
    }

    private void detectMissingDataPoints(List<ZappiHistory> dataPoints) {
        // store current and previous
        // calculate the time period between the two
        // if it is greater than 1, the interval distance is to be calculated
        // the reading at current and previous are to be taken and then a slope is to be calculated for the two points
        // The gaps are then to be filled in
    }
}
