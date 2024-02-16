package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloodStationCoverageResponseDTO {

    private String address;
    private List<PersonFloodStationCoverageDTO> persons;

}
