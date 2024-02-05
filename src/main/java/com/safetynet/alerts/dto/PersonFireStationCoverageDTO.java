package com.safetynet.alerts.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonFireStationCoverageDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

}