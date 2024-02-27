package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "API Controller pour les opérations CRUD sur les FireStations")
@RestController
@RequiredArgsConstructor
public class FireStationController {

    private final FireStationService fireStationService;

    // CRUD

    @GetMapping("/firestations")
    @Operation(description = "Permet de récupérer tous les mapping Adresse-Station de pompier")
    public ResponseEntity<List<FireStation>> getFireStations() throws Exception {
        Optional<List<FireStation>> fireStations = fireStationService.getFireStations();
        return fireStations
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/firestation")
    @Operation(description = "Permet de supprimer un mapping Adresse-Station de pompier")
    public ResponseEntity<Void> deleteFireStationMapping(@RequestParam @NotBlank String address, @Min(1) int stationNumber) throws Exception {
        return fireStationService.deleteFireStationMapping(address, stationNumber) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/firestation")
    @Operation(description = "Permet de mettre à jour un mapping Adresse-Station de pompier")
    public ResponseEntity<FireStation> updateFireStationMapping(@RequestBody @Validated FireStation fireStationToUpdate) throws Exception {
        Optional<FireStation> updatedFireStation = fireStationService.updateFireStationMapping(fireStationToUpdate);
        return updatedFireStation
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/firestation")
    @Operation(description = "Permet d'ajouter un mapping Adresse-Station de pompier")
    public ResponseEntity<FireStation> addFireStationMapping(@RequestBody @Validated FireStation fireStationToAdd) throws Exception {
        Optional<FireStation> addedFireStation = fireStationService.addFireStationMapping(fireStationToAdd);
        return addedFireStation
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
}