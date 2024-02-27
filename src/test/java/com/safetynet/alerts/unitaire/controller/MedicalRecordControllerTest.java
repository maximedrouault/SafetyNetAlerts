package com.safetynet.alerts.unitaire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    private MedicalRecord medicalRecord;
    private String jsonBody;

    @BeforeEach
    public void setUp() throws Exception {
        medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .birthdate("03/06/1984")
                .medications(List.of("aznol:350mg", "hydrapermazol:100mg"))
                .allergies(List.of("nillacilan"))
                .build();

        jsonBody = objectMapper.writeValueAsString(medicalRecord); // Convert object to JSON
    }


    @Test
    public void deleteMedicalRecord_whenMedicalRecordExists_shouldReturnOk() throws Exception {
        when(medicalRecordService.deleteMedicalRecord("John", "Boyd")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMedicalRecord_whenMedicalRecordDoesNotExist_shouldReturnNotFound() throws Exception {
        when(medicalRecordService.deleteMedicalRecord("Unknown", "Person")).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Unknown")
                        .param("lastName", "Person"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMedicalRecord_whenMedicalRecordExists_shouldReturnOk() throws Exception {
                Optional<MedicalRecord> updatedMedicalRecord = Optional.of(medicalRecord);
        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenReturn(updatedMedicalRecord);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMedicalRecord_whenMedicalRecordDoesNotExist_shouldReturnNotFound() throws Exception {
        Optional<MedicalRecord> updatedMedicalRecord = Optional.empty();
        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenReturn(updatedMedicalRecord);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMedicalRecord_whenMedicalRecordExists_shouldReturnConflict() throws Exception {
        Optional<MedicalRecord> addedMedicalRecord = Optional.empty();
        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(addedMedicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isConflict());
    }

    @Test
    public void addMedicalRecord_whenMedicalRecordDoesNotExist_shouldReturnOK() throws Exception {
        Optional<MedicalRecord> addedMedicalRecord = Optional.of(medicalRecord);
        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(addedMedicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void getMedicalRecords_whenMedicalRecordsDoNotExist_shouldReturnNotFound() throws Exception {
        Optional<List<MedicalRecord>> medicalRecords = Optional.empty();
        when(medicalRecordService.getMedicalRecords()).thenReturn(medicalRecords);

        mockMvc.perform(get("/medicalRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getMedicalRecords_whenMedicalRecordsExist_shouldReturnOK() throws Exception {
        Optional<List<MedicalRecord>> medicalRecords = Optional.of(List.of(medicalRecord));
        when(medicalRecordService.getMedicalRecords()).thenReturn(medicalRecords);

        mockMvc.perform(get("/medicalRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }
}