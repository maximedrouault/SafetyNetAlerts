package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class FireStationController {

    private final FireStationService fireStationService;

    // CRUD

    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteFireStationMapping(@RequestBody FireStation fireStationToDelete) throws Exception {
        return fireStationService.deleteFireStationMapping(fireStationToDelete) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/firestation")
    public ResponseEntity<FireStation> updateFireStationMapping(@RequestBody FireStation fireStationToUpdate) throws Exception {
        Optional<FireStation> updatedFireStation = fireStationService.updateFireStationMapping(fireStationToUpdate);
        return updatedFireStation
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/firestation")
    public ResponseEntity<FireStation> addFireStationMapping(@RequestBody FireStation fireStationToAdd) throws Exception {
        Optional<FireStation> addedFireStation = fireStationService.addFireStationMapping(fireStationToAdd);
        return addedFireStation
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
}