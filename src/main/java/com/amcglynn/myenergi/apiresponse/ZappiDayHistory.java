package com.amcglynn.myenergi.apiresponse;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZappiDayHistory {
    private final Map<String, List<ZappiHistory>> zappiReadings = new HashMap<>();
    private static final int MINUTES_PER_DAY = 1440;
    private String id;

    @JsonAnyGetter
    public List<ZappiHistory> getReadings() {
        return zappiReadings.get(id);
    }

    @JsonAnySetter
    public void setReadings(String id, List<ZappiHistory> value) {
        this.id = id;
        zappiReadings.put(id, value);
    }

    public int getExpectedReadings() {
        return MINUTES_PER_DAY;
    }
}
