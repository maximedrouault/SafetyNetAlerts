package com.safetynet.alerts.unitaire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void deleteFireStationMapping_whenMappingExists_shouldReturnOk() throws Exception {
        FireStation fireStationToDelete = new FireStation();
        String jsonRequest = objectMapper.writeValueAsString(fireStationToDelete); // Convert object to JSON
        when(fireStationService.deleteFireStationMapping(fireStationToDelete)).thenReturn(true);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFireStationMapping_whenMappingDoesNotExist_shouldReturnNotFound() throws Exception {
        FireStation fireStationToDelete = new FireStation();
        String jsonRequest = objectMapper.writeValueAsString(fireStationToDelete);
        when(fireStationService.deleteFireStationMapping(fireStationToDelete)).thenReturn(false);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFireStationMapping_whenMappingExists_shouldReturnOk() throws Exception {
        FireStation fireStationToUpdate = new FireStation();
        String jsonRequest = objectMapper.writeValueAsString(fireStationToUpdate);
        Optional<FireStation> updatedFireStation = Optional.of(fireStationToUpdate);
        when(fireStationService.updateFireStationMapping(fireStationToUpdate)).thenReturn(updatedFireStation);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFireStationMapping_whenMappingDoesNotExist_shouldReturnNotFound() throws Exception {
        FireStation fireStationToUpdate = new FireStation();
        String jsonRequest = objectMapper.writeValueAsString(fireStationToUpdate);
        Optional<FireStation> updatedFireStation = Optional.empty();
        when(fireStationService.updateFireStationMapping(fireStationToUpdate)).thenReturn(updatedFireStation);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addFireStationMapping_whenMappingExists_shouldReturnConflict() throws Exception {
        FireStation fireStationToAdd = new FireStation();
        String jsonRequest = objectMapper.writeValueAsString(fireStationToAdd);
        Optional<FireStation> addedFireStation = Optional.empty();
        when(fireStationService.addFireStationMapping(fireStationToAdd)).thenReturn(addedFireStation);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @Test
    public void addFireStationMapping_whenMappingDoesNotExist_shouldReturnOK() throws Exception {
        FireStation fireStationToAdd = new FireStation();
        String jsonRequest = objectMapper.writeValueAsString(fireStationToAdd);
        Optional<FireStation> addedFireStation = Optional.of(fireStationToAdd);
        when(fireStationService.addFireStationMapping(fireStationToAdd)).thenReturn(addedFireStation);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }
}