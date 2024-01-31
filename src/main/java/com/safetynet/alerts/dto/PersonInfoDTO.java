package com.safetynet.alerts.dto;

import com.safetynet.alerts.interfaces.CommonMedicalInfo;
import lombok.Data;

import java.util.List;

@Data
public class PersonInfoDTO implements CommonMedicalInfo {

    private String lastName;
    private String address;
    private int age;
    private String email;
    private List<String> medications;
    private List<String> allergies;

}