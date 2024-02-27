package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank private String address;
    @NotBlank private String city;
    @NotBlank private String zip;
    @NotBlank private String phone;
    @NotBlank private String email;

}