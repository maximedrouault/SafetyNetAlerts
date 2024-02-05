package com.safetynet.alerts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonFireAddressInfoDTO {

    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

}