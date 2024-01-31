package com.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class FloodStationCoverageResponseDTO {

    private String address;
    private List<PersonFloodStationCoverageDTO> persons;

}
