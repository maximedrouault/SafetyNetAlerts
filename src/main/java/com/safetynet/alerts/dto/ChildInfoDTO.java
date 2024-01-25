package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class ChildInfoDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<Person> familyMembers;

}