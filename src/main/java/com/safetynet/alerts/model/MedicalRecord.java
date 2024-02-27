package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MedicalRecord {

    @NotBlank private String firstName;
    @NotBlank private String lastName;

    @NotNull
    @Pattern(regexp = "(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/(18\\d{2}|19\\d{2}|[2-9]\\d{3})", message = "Date must be in the format MM/dd/yyyy with right values and year >= 1800")
    private String birthdate;

    @NotNull private List<@NotBlank String> medications;
    @NotNull private List<@NotBlank String> allergies;

}