package com.safetynet.alerts.dto;

import lombok.Data;

import java.util.List;

@Data
public class FireAddressInfoResponseDTO {

    private int stationNumber;
    private List<PersonFireAddressInfoDTO> persons;

}
