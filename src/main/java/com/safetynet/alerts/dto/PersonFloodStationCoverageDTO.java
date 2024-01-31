package com.safetynet.alerts.dto;

import com.safetynet.alerts.interfaces.CommonMedicalInfo;
import lombok.Data;

import java.util.List;

@Data
public class PersonFloodStationCoverageDTO implements CommonMedicalInfo {

    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

}