package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "API Controller pour les opérations CRUD sur les MedicalRecords")
@RestController
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    // CRUD

    @GetMapping("/medicalRecords")
    @Operation(description = "Permet de récupérer tous les medicalRecords")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecords() throws Exception {
        Optional<List<MedicalRecord>> medicalRecords = medicalRecordService.getMedicalRecords();
        return medicalRecords
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/medicalRecord")
    @Operation(description = "Permet de supprimer un enregistrement médical")
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam @NotBlank String firstName, @NotBlank String lastName) throws Exception {
        return medicalRecordService.deleteMedicalRecord(firstName, lastName) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/medicalRecord")
    @Operation(description = "Permet de mettre à jour un enregistrement médical")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody @Validated MedicalRecord medicalRecordToUpdate) throws Exception {
        Optional<MedicalRecord> updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecordToUpdate);
        return updatedMedicalRecord
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("medicalRecord")
    @Operation(description = "Permet d'ajouter un enregistrement médical")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody @Validated MedicalRecord medicalRecordToAdd) throws Exception {
        Optional<MedicalRecord> addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecordToAdd);
        return addedMedicalRecord
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
}
