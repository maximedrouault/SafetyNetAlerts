package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing medical records.
 * This class provides methods for performing CRUD operations on medical records.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class MedicalRecordService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    // CRUD

    /**
     * Deletes a medical record.
     *
     * @param medicalRecordToDelete The medical record to delete.
     * @return True if the medical record was successfully deleted, false otherwise.
     * @throws Exception If an error occurs while deleting the medical record.
     */
    public boolean deleteMedicalRecord(MedicalRecord medicalRecordToDelete) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();

        Optional<MedicalRecord> foundMedicalRecord = medicalRecords.stream()
                .filter(existingMedicalRecord ->
                        existingMedicalRecord.getFirstName().equals(medicalRecordToDelete.getFirstName()) &&
                        existingMedicalRecord.getLastName().equals(medicalRecordToDelete.getLastName()))
                .findFirst();

        if (foundMedicalRecord.isPresent()) {
            MedicalRecord medicalRecordRemoved = foundMedicalRecord.get();
            medicalRecords.remove(medicalRecordRemoved);

            dataWriter.dataWrite(dataContainer);
            log.info("MedicalRecord successfully deleted for person with Firstname: '{}' and Lastname: '{}'.", medicalRecordRemoved.getFirstName(), medicalRecordRemoved.getLastName());

            return true;

        } else {
            log.error("MedicalRecord not found for person with Firstname: '{}' and Lastname: '{}'.", medicalRecordToDelete.getFirstName(), medicalRecordToDelete.getLastName());
            return false;
        }
    }

    /**
     * Updates a medical record.
     *
     * @param medicalRecordToUpdate The medical record to update.
     * @return An Optional containing the updated medical record if found, or empty if not found.
     * @throws Exception If an error occurs while updating the medical record.
     */
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
            log.info("Medical record successfully updated for person with Firstname: '{}' and Lastname: '{}'.", updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());

            return Optional.of(updatedMedicalRecord);

        } else {
            log.error("Not updated, Medical record not found for person with Firstname: '{}' and Lastname: '{}'.", medicalRecordToUpdate.getFirstName(), medicalRecordToUpdate.getLastName());
            return Optional.empty();
        }
    }

    /**
     * Adds a new medical record.
     *
     * @param medicalRecordToAdd The medical record to add.
     * @return An Optional containing the added medical record if it doesn't already exist, or empty if it already exists.
     * @throws Exception If an error occurs while adding the medical record.
     */
    public Optional<MedicalRecord> addMedicalRecord(MedicalRecord medicalRecordToAdd) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();

        Optional<MedicalRecord> foundMedicalRecord = medicalRecords.stream()
                .filter(existingMedicalRecord ->
                        existingMedicalRecord.getFirstName().equals(medicalRecordToAdd.getFirstName()) &&
                        existingMedicalRecord.getLastName().equals(medicalRecordToAdd.getLastName()))
                .findFirst();

        if (foundMedicalRecord.isPresent()) {
            log.error("Not added, Medical record already exist for person with Firstname: '{}' and Lastname: '{}'.", medicalRecordToAdd.getFirstName(), medicalRecordToAdd.getLastName());
            return Optional.empty();

        } else {
            medicalRecords.add(medicalRecordToAdd);

            dataWriter.dataWrite(dataContainer);
            log.info("Medical record successfully added for person with Firstname: '{}' and Lastname: '{}'.", medicalRecordToAdd.getFirstName(), medicalRecordToAdd.getLastName());

            return Optional.of(medicalRecordToAdd);
        }
    }
}