package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    @GetMapping("/firestation")
    public ResponseEntity<List<FireStation>> getFireStations() throws Exception {
        return fireStationService.getFireStations();
    }

    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteFireStationMapping(@RequestParam String address, @RequestParam int station) throws Exception {
        return fireStationService.deleteFireStationMapping(address, station);
    }

    @PutMapping("/firestation")
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) throws Exception {
        return fireStationService.updateFireStation(fireStation);
    }
}