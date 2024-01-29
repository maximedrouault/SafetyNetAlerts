package com.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class FireStationCoverageResponseDTO {

    private List<PersonFireStationCoverageDTO> persons;
    private int adultsCount;
    private int childrenCount;

}
