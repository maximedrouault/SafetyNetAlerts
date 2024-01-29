package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonChildAlertDTO;
import com.safetynet.alerts.dto.PersonFireAddressInfoDTO;
import com.safetynet.alerts.dto.PersonPhoneAlertDTO;
import com.safetynet.alerts.service.ChildAlertService;
import com.safetynet.alerts.service.FireAddressInfoService;
import com.safetynet.alerts.service.FireStationCoverageService;
import com.safetynet.alerts.service.PhoneAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SpecificRequestController {

    private final ChildAlertService childAlertService;
    private final FireAddressInfoService fireAddressInfoService;
    private final FireStationCoverageService fireStationCoverageService;
    private final PhoneAlertService phoneAlertService;


    // ChildAlert
    @GetMapping("/childAlert")
    public ResponseEntity<List<PersonChildAlertDTO>> getChildAlert(@RequestParam String address) throws Exception {
        Optional<List<PersonChildAlertDTO>> childInfoDTO = childAlertService.getChildAlert(address);
        return childInfoDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // FireAddressInfo
    @GetMapping("/fire")
    public ResponseEntity<List<PersonFireAddressInfoDTO>> getFireAddressInfo(@RequestParam String address) throws Exception {
        List<PersonFireAddressInfoDTO> personFireAddressInfoDTO = fireAddressInfoService.getFireAddressInfo(address);
        return (personFireAddressInfoDTO.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personFireAddressInfoDTO);
    }

    // FireStationCoverage
    @GetMapping("/firestation")
    public ResponseEntity<FireStationCoverageResponseDTO> getFireStationCoverage(@RequestParam int stationNumber) throws Exception {
        FireStationCoverageResponseDTO personsCovered = fireStationCoverageService.getFireStationCoverage(stationNumber);
        return (personsCovered.getPersons().isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personsCovered);
    }

    // PhoneAlert
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<PersonPhoneAlertDTO>> getPhoneAlert(@RequestParam(name = "firestation") int fireStationNumber) throws Exception {
        Optional<List<PersonPhoneAlertDTO>> personPhoneDTO = phoneAlertService.getPhoneAlert(fireStationNumber);
        return personPhoneDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
