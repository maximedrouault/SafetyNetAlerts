package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.Person;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonChildAlertDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<Person> familyMembers;

}