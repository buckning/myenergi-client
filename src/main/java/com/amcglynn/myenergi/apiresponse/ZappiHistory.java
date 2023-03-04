package com.amcglynn.myenergi.apiresponse;

import com.amcglynn.myenergi.units.Joule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZappiHistory {
    @JsonProperty("yr")
    private int year;

    @JsonProperty("dom")
    private int dayOfMonth;

    @JsonProperty("mon")
    private int month;

    @JsonProperty("hr")
    private int hour;

    @JsonProperty("min")
    private Integer minute = 0;

    @JsonProperty("dow")
    private String dayOfWeek;

    @JsonProperty("gep")
    private Long solarGenerationJoules = 0L;     //Generated positive in Joules. gen is generated nevative

    @JsonProperty("exp")
    private Long gridExportJoules = 0L;

    @JsonProperty("h1b")
    private Long zappiBoostModeJoules = 0L;     // either manual or scheduled boost

    @JsonProperty("h1d")
    private Long zappiDivertedModeJoules = 0L;  // General usage either from PV or from the grid or both

    @JsonProperty("imp")
    private Long importedJoules = 0L;

    public Joule getGridExport() {
        return new Joule(gridExportJoules);
    }

    public Joule getImported() {
        return new Joule(importedJoules);
    }

    public Joule getBoost() {
        return new Joule(zappiBoostModeJoules);
    }

    public Joule getZappiDiverted() {
        return new Joule(zappiDivertedModeJoules);
    }

    public Joule getSolarGeneration() {
        return new Joule(solarGenerationJoules);
    }
}
