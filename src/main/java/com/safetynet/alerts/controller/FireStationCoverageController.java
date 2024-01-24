package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.FireStationCoverageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class FireStationCoverageController {

    private final FireStationCoverageService fireStationCoverageService;

    @GetMapping("/firestation")
    public ResponseEntity<List<PersonInfoDTO>> getFireStationCoverage(@RequestParam int stationNumber) throws Exception {
        Optional<List<PersonInfoDTO>> personsCovered = fireStationCoverageService.getFireStationCoverage(stationNumber);
        return personsCovered
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
