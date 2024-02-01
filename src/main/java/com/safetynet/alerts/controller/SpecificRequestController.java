package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.service.*;
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
    private final CommunityEmailService communityEmailService;
    private final PersonInfoService personInfoService;
    private final FloodStationCoverageService floodStationCoverageService;


    // ChildAlert
    @GetMapping("/childAlert")
    public ResponseEntity<List<PersonChildAlertDTO>> getChildAlert(@RequestParam String address) throws Exception {
        List<PersonChildAlertDTO> personChildAlertDTOS = childAlertService.getChildAlert(address);
        return (personChildAlertDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personChildAlertDTOS);
    }

    // FireAddressInfo
    @GetMapping("/fire")
    public ResponseEntity<List<PersonFireAddressInfoDTO>> getFireAddressInfo(@RequestParam String address) throws Exception {
        List<PersonFireAddressInfoDTO> personFireAddressInfoDTOS = fireAddressInfoService.getFireAddressInfo(address);
        return (personFireAddressInfoDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personFireAddressInfoDTOS);
    }

    // FireStationCoverage
    @GetMapping("/firestation")
    public ResponseEntity<FireStationCoverageResponseDTO> getFireStationCoverage(@RequestParam int stationNumber) throws Exception {
        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = fireStationCoverageService.getFireStationCoverage(stationNumber);
        return (fireStationCoverageResponseDTO.getPersons().isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(fireStationCoverageResponseDTO);
    }

    // PhoneAlert
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<PersonPhoneAlertDTO>> getPhoneAlert(@RequestParam(name = "firestation") int fireStationNumber) throws Exception {
        Optional<List<PersonPhoneAlertDTO>> personPhoneAlertDTOS = phoneAlertService.getPhoneAlert(fireStationNumber);
        return personPhoneAlertDTOS
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CommunityEmail
    @GetMapping("/communityEmail")
    public ResponseEntity<List<PersonCommunityEmailDTO>> getCommunityEmail(@RequestParam String city) throws Exception {
        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = communityEmailService.getCommunityEmail(city);
        return (personCommunityEmailDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personCommunityEmailDTOS);
    }

    // PersonInfo
    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam String firstName, String lastName) throws Exception {
        List<PersonInfoDTO> personInfoDTOS = personInfoService.getPersonInfo(firstName, lastName);
        return (personInfoDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personInfoDTOS);
    }

    // FloodStationCoverage
    @GetMapping("/flood")
    public ResponseEntity<List<FloodStationCoverageResponseDTO>> getFloodStationCoverage(@RequestParam(name = "stations") List<Integer> stationNumbers) throws Exception {
        List<FloodStationCoverageResponseDTO> floodStationCoverageResponseDTOS = floodStationCoverageService.getFloodStationCoverage(stationNumbers);
        return (floodStationCoverageResponseDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(floodStationCoverageResponseDTOS);
    }
}