package com.safetynet.alerts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FloodStationCoverageResponseDTO {

    private String address;
    private List<PersonFloodStationCoverageDTO> persons;

}
