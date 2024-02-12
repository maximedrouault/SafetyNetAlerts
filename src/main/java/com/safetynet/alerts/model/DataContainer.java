package com.safetynet.alerts.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DataContainer {

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

}