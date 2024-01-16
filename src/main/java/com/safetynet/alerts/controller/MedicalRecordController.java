package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecord")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecords() throws Exception {
        return medicalRecordService.getMedicalRecords();
    }

}
