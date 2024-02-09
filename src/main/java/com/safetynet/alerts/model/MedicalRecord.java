package com.safetynet.alerts.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

}