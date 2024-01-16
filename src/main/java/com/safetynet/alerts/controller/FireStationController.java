package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    @GetMapping("/firestation")
    public ResponseEntity<List<FireStation>> getFireStations() throws Exception {
        return fireStationService.getFireStations();
    }

}