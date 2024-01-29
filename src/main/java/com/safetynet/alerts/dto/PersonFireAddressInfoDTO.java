package com.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonFireAddressInfoDTO {

    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;
    private int stationNumber;

}