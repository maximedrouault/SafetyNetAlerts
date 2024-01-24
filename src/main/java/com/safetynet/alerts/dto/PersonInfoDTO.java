package com.safetynet.alerts.dto;

import lombok.Data;

@Data
public class PersonInfoDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private int adultsCount;
    private int childrenCount;

}