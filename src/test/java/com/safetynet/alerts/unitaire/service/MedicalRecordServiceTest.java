package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.DataWriter;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private DataWriter dataWriter;

    @InjectMocks
    private MedicalRecordService medicalRecordService;


    @Test
    public void deleteMedicalRecord_WhenMedicalRecordExists_ShouldReturnTrue() throws Exception {
        List<MedicalRecord> MedicalRecords = new ArrayList<>();
        MedicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        DataContainer dataContainer = DataContainer.builder().medicalrecords(MedicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean deletedMedicalRecord = medicalRecordService.deleteMedicalRecord(MedicalRecords.get(0));

        assertTrue(deletedMedicalRecord);
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void deleteMedicalRecord_WhenMedicalRecordFirstNameDoesNotMatch_ShouldReturnFalse() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        MedicalRecord medicalRecordToDelete = MedicalRecord.builder().firstName("Tenley").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean deletedMedicalRecord = medicalRecordService.deleteMedicalRecord(medicalRecordToDelete);

        assertFalse(deletedMedicalRecord);
    }

    @Test
    public void deleteMedicalRecord_WhenMedicalRecordLastNameDoesNotMatch_ShouldReturnFalse() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        MedicalRecord medicalRecordToDelete = MedicalRecord.builder().firstName("John").lastName("Foster").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean deletedMedicalRecord = medicalRecordService.deleteMedicalRecord(medicalRecordToDelete);

        assertFalse(deletedMedicalRecord);
    }

    @Test
    public void updateMedicalRecord_WhenMedicalRecordExists_ShouldReturnUpdatedMedicalRecord() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("03/06/1984")
                .medications(List.of("aznol:350mg", "hydrapermazol:100mg")).allergies(List.of("nillacilan")).build());
        MedicalRecord medicalRecordToUpdate = MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("03/06/1989")
                .medications(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg")).allergies(List.of()).build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<MedicalRecord> updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecordToUpdate);

        assertTrue(updatedMedicalRecord.isPresent());
        assertAll("updatedMedicalRecord", () -> {
            assertEquals("John", updatedMedicalRecord.get().getFirstName());
            assertEquals("Boyd", updatedMedicalRecord.get().getLastName());
            assertEquals("03/06/1989", updatedMedicalRecord.get().getBirthdate());
            assertEquals(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), updatedMedicalRecord.get().getMedications());
            assertEquals(List.of(), updatedMedicalRecord.get().getAllergies());
        });
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void updateMedicalRecord_WhenMedicalRecordFirstNameDoesNotMatch_ShouldReturnEmptyOptional() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        MedicalRecord medicalRecordToUpdate = MedicalRecord.builder().firstName("Tenley").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<MedicalRecord> updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecordToUpdate);

        assertTrue(updatedMedicalRecord.isEmpty());
    }

    @Test
    public void updateMedicalRecord_WhenMedicalRecordLastNameDoesNotMatch_ShouldReturnEmptyOptional() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        MedicalRecord medicalRecordToUpdate = MedicalRecord.builder().firstName("John").lastName("Foster").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<MedicalRecord> updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecordToUpdate);

        assertTrue(updatedMedicalRecord.isEmpty());
    }

    @Test
    public void addMedicalRecord_WhenMedicalRecordDoesNotExist_ShouldReturnAddedMedicalRecord() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord medicalRecordToAdd = MedicalRecord.builder().firstName("John").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<MedicalRecord> addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecordToAdd);

        assertTrue(addedMedicalRecord.isPresent());
        assertEquals(addedMedicalRecord.get(), medicalRecordToAdd);
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void addMedicalRecord_WhenMedicalRecordAlreadyExists_ShouldReturnEmptyOptional() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        MedicalRecord medicalRecordToAdd = MedicalRecord.builder().firstName("John").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<MedicalRecord> addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecordToAdd);

        assertTrue(addedMedicalRecord.isEmpty());
    }

    @Test
    public void addMedicalRecord_WhenMedicalRecordSameFirstNameMatch_ShouldReturnEmptyOptional() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        medicalRecords.add(MedicalRecord.builder().firstName("Tenley").lastName("Boyd").build());
        MedicalRecord medicalRecordToAdd = MedicalRecord.builder().firstName("Tenley").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<MedicalRecord> addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecordToAdd);

        assertTrue(addedMedicalRecord.isEmpty());
    }

    @Test
    public void addMedicalRecord_WhenMedicalRecordSameLastNameMatch_ShouldReturnEmptyOptional() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Foster").build());
        MedicalRecord medicalRecordToAdd = MedicalRecord.builder().firstName("John").lastName("Foster").build();
        DataContainer dataContainer = DataContainer.builder().medicalrecords(medicalRecords).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<MedicalRecord> addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecordToAdd);

        assertTrue(addedMedicalRecord.isEmpty());
    }
}