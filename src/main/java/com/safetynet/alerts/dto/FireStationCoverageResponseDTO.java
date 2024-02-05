package com.safetynet.alerts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FireStationCoverageResponseDTO {

    private List<PersonFireStationCoverageDTO> persons;
    private int adultsCount;
    private int childrenCount;

}
