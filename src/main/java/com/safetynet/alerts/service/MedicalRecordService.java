package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
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
    private final DataWriter dataWriter;

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

    public ResponseEntity<Void> deleteMedicalRecord(String firstName, String lastName) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();

        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                medicalRecords.remove(medicalRecord);

                log.info("MedicalRecord successfully deleted");
                dataWriter.dataWrite(dataContainer);

                return ResponseEntity.ok().build();
            }
        }

        log.error("MedicalRecord not found");
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<MedicalRecord> updateMedicalRecord(MedicalRecord medicalRecordToUpdate) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();

        for (MedicalRecord existingMedicalRecord : medicalRecords) {
            if (existingMedicalRecord.getFirstName().equals(medicalRecordToUpdate.getFirstName()) &&
                    existingMedicalRecord.getLastName().equals(medicalRecordToUpdate.getLastName())) {

                existingMedicalRecord.setBirthdate(medicalRecordToUpdate.getBirthdate());
                existingMedicalRecord.setMedications(medicalRecordToUpdate.getMedications());
                existingMedicalRecord.setAllergies(medicalRecordToUpdate.getAllergies());

                log.info("Medical record successfully updated");
                dataWriter.dataWrite(dataContainer);

                return ResponseEntity.ok(existingMedicalRecord);
            }
        }

        log.error("Not updated, Medical record not found");
        return ResponseEntity.notFound().build();
    }
}