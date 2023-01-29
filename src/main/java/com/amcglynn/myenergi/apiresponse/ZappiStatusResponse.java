package com.amcglynn.myenergi.apiresponse;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ZappiStatusResponse {
    List<ZappiStatus> zappi;
}
