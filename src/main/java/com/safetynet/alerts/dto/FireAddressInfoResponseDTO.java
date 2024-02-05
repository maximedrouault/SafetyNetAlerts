package com.safetynet.alerts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FireAddressInfoResponseDTO {

    private int stationNumber;
    private List<PersonFireAddressInfoDTO> persons;

}
