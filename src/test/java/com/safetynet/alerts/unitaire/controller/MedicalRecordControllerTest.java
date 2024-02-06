package com.safetynet.alerts.unitaire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void deleteMedicalRecord_whenMedicalRecordExists_shouldReturnOk() throws Exception {
        MedicalRecord medicalRecordToDelete = new MedicalRecord();
        String jsonRequest = objectMapper.writeValueAsString(medicalRecordToDelete); // Convert object to JSON
        when(medicalRecordService.deleteMedicalRecord(medicalRecordToDelete)).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMedicalRecord_whenMedicalRecordDoesNotExist_shouldReturnNotFound() throws Exception {
        MedicalRecord medicalRecordToDelete = new MedicalRecord();
        String jsonRequest = objectMapper.writeValueAsString(medicalRecordToDelete);
        when(medicalRecordService.deleteMedicalRecord(medicalRecordToDelete)).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMedicalRecord_whenMedicalRecordExists_shouldReturnOk() throws Exception {
        MedicalRecord medicalRecordToUpdate = new MedicalRecord();
        String jsonRequest = objectMapper.writeValueAsString(medicalRecordToUpdate);
        Optional<MedicalRecord> updatedMedicalRecord = Optional.of(medicalRecordToUpdate);
        when(medicalRecordService.updateMedicalRecord(medicalRecordToUpdate)).thenReturn(updatedMedicalRecord);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMedicalRecord_whenMedicalRecordDoesNotExist_shouldReturnNotFound() throws Exception {
        MedicalRecord medicalRecordToUpdate = new MedicalRecord();
        String jsonRequest = objectMapper.writeValueAsString(medicalRecordToUpdate);
        Optional<MedicalRecord> updatedMedicalRecord = Optional.empty();
        when(medicalRecordService.updateMedicalRecord(medicalRecordToUpdate)).thenReturn(updatedMedicalRecord);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMedicalRecord_whenMedicalRecordExists_shouldReturnConflict() throws Exception {
        MedicalRecord medicalRecordToAdd = new MedicalRecord();
        String jsonRequest = objectMapper.writeValueAsString(medicalRecordToAdd);
        Optional<MedicalRecord> addedMedicalRecord = Optional.empty();
        when(medicalRecordService.addMedicalRecord(medicalRecordToAdd)).thenReturn(addedMedicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @Test
    public void addMedicalRecord_whenMedicalRecordDoesNotExist_shouldReturnOK() throws Exception {
        MedicalRecord medicalRecordToAdd = new MedicalRecord();
        String jsonRequest = objectMapper.writeValueAsString(medicalRecordToAdd);
        Optional<MedicalRecord> addedMedicalRecord = Optional.of(medicalRecordToAdd);
        when(medicalRecordService.addMedicalRecord(medicalRecordToAdd)).thenReturn(addedMedicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }
}