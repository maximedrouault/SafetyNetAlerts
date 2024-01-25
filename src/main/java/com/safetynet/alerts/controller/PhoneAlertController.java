package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonPhoneDTO;
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
public class PhoneAlertController {

    private final PhoneAlertService phoneAlertService;

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<PersonPhoneDTO>> getPhoneAlert(@RequestParam(name = "firestation") int fireStationNumber) throws Exception {
        Optional<List<PersonPhoneDTO>> personPhoneDTO = phoneAlertService.getPhoneAlert(fireStationNumber);
        return personPhoneDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}