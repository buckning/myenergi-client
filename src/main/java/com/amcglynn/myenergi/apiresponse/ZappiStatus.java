package com.amcglynn.myenergi.apiresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZappiStatus {
    @JsonProperty("sno")
    private String serialNumber;
    @JsonProperty("gen")
    private Long solarGeneration = 0L;
    @JsonProperty("div")
    private Long carDiversionAmountWatts = 0L;
    @JsonProperty("che")
    private Double chargeAddedThisSessionKwh;
    @JsonProperty("grd")
    private Long gridWatts = 0L; // minus means pushing back to grid, positive means importing
    @JsonProperty("zmo")
    private int zappiChargeMode;  //1=Fast, 2=Eco, 3=Eco+, 4=Stopped

    @JsonProperty("pst")
    private String evConnectionStatus;

//    {
//        "zappi": [
//        {
//            "sno": serialnumberhere,
//                "dat": "28-01-2023",
//                "tim": "14:13:21",
//                "ectp2": -860,
//                "ectt1": "Internal Load",
//                "ectt2": "Grid",
//                "ectt3": "None",
//                "bsm": 0,
//                "bst": 0,
//                "cmt": 254,
//                "dst": 1,
//                "div": 0,
//                "frq": 50.02,
//                "fwv": "3562S4.525",
//                "gen": 1666,
//                "grd": -868,
//                "pha": 1,
//                "pri": 1,
//                "sta": 5,
//                "tz": 0,
//                "vol": 2411,
//                "che": 24.43,
//                "bss": 0,
//                "lck": 23,
//                "pst": "B2",
//                "zmo": 3,
//                "pwm": 5300,
//                "zs": 2562,
//                "rdc": -3,
//                "rac": 1,
//                "rrac": -6,
//                "zsh": 10,
//                "zsl": 2,
//                "ectt4": "None",
//                "ectt5": "None",
//                "ectt6": "None",
//                "newAppAvailable": false,
//                "newBootloaderAvailable": false,
//                "beingTamperedWith": false,
//                "batteryDischargeEnabled": false,
//                "mgl": 30,
//                "sbh": 17,
//                "sbk": 5
//        }
//    ]
//    }
}
