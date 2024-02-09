package com.safetynet.alerts.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FireStation {

    private String address;
    private int station;

}