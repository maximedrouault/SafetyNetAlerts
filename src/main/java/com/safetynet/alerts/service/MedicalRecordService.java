package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final DataReader dataReader;

    public ResponseEntity<List<MedicalRecord>> getMedicalRecords() throws Exception {
            List<MedicalRecord> medicalRecords = dataReader.dataRead().getMedicalrecords();

            if (medicalRecords.isEmpty()) {
                log.error("No Medical Records found");
                return ResponseEntity.notFound().build();
            } else {
                log.info("Medical Records successfully retrieved");
                return ResponseEntity.ok(medicalRecords);
            }
    }
}