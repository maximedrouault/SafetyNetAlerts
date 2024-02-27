package com.safetynet.alerts.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FireStation {

    @NotBlank
    private String address;

    @Min(value = 1, message = "The number of station must be above zero")
    private int station;

}