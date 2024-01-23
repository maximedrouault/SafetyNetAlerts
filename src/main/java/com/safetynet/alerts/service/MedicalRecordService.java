package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    // CRUD

    public boolean deleteMedicalRecord(MedicalRecord medicalRecordToDelete) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();

        Optional<MedicalRecord> foundMedicalRecord = medicalRecords.stream()
                .filter(existingMedicalRecord ->
                        existingMedicalRecord.getFirstName().equals(medicalRecordToDelete.getFirstName()) &&
                        existingMedicalRecord.getLastName().equals(medicalRecordToDelete.getLastName()))
                .findFirst();

        if (foundMedicalRecord.isPresent()) {
            medicalRecords.remove(foundMedicalRecord.get());

            dataWriter.dataWrite(dataContainer);
            log.info("MedicalRecord successfully deleted");

            return true;

        } else {
            log.error("MedicalRecord not found");
            return false;
        }
    }

    public Optional<MedicalRecord> updateMedicalRecord(MedicalRecord medicalRecordToUpdate) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();

        Optional<MedicalRecord> foundMedicalRecord = medicalRecords.stream()
                .filter(existingMedicalRecord ->
                        existingMedicalRecord.getFirstName().equals(medicalRecordToUpdate.getFirstName()) &&
                        existingMedicalRecord.getLastName().equals(medicalRecordToUpdate.getLastName()))
                .findFirst();

        if (foundMedicalRecord.isPresent()) {
            MedicalRecord updatedMedicalRecord = foundMedicalRecord.get();

            updatedMedicalRecord.setBirthdate(medicalRecordToUpdate.getBirthdate());
            updatedMedicalRecord.setMedications(medicalRecordToUpdate.getMedications());
            updatedMedicalRecord.setAllergies(medicalRecordToUpdate.getAllergies());

            dataWriter.dataWrite(dataContainer);
            log.info("Medical record successfully updated");

            return Optional.of(updatedMedicalRecord);

        } else {
            log.error("Not updated, Medical record not found");
            return Optional.empty();
        }
    }

    public Optional<MedicalRecord> addMedicalRecord(MedicalRecord medicalRecordToAdd) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();

        Optional<MedicalRecord> foundMedicalRecord = medicalRecords.stream()
                .filter(existingMedicalRecord ->
                        existingMedicalRecord.getFirstName().equals(medicalRecordToAdd.getFirstName()) &&
                        existingMedicalRecord.getLastName().equals(medicalRecordToAdd.getLastName()))
                .findFirst();

        if (foundMedicalRecord.isPresent()) {
            log.error("Not added, Medical record already exist");
            return Optional.empty();

        } else {
            medicalRecords.add(medicalRecordToAdd);

            dataWriter.dataWrite(dataContainer);
            log.info("Medical record successfully added");

            return Optional.of(medicalRecordToAdd);
        }
    }
}