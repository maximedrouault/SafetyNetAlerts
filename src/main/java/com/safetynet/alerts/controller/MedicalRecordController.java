package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecord")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecords() throws Exception {
        return medicalRecordService.getMedicalRecords();
    }

    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) throws Exception {
        return medicalRecordService.deleteMedicalRecord(firstName, lastName);
    }

    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws Exception {
        return medicalRecordService.updateMedicalRecord(medicalRecord);
    }

    @PostMapping("medicalRecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws Exception {
        return medicalRecordService.addMedicalRecord(medicalRecord);
    }
}
