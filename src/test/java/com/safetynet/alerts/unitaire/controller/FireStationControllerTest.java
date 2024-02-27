package com.safetynet.alerts.unitaire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
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

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    @Autowired
    private ObjectMapper objectMapper;

    private FireStation fireStation;
    private String jsonBody;

    @BeforeEach
    public void setUp() throws Exception {
        fireStation = FireStation.builder()
                .address("1509 Culver St")
                .station(3)
                .build();

        jsonBody = objectMapper.writeValueAsString(fireStation); // Convert object to JSON
    }

    @Test
    public void deleteFireStationMapping_whenMappingExists_shouldReturnOk() throws Exception {
        when(fireStationService.deleteFireStationMapping("1509 Culver St", 3)).thenReturn(true);

        mockMvc.perform(delete("/firestation")
                        .param("address", "1509 Culver St")
                        .param("stationNumber", "3"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFireStationMapping_whenMappingDoesNotExist_shouldReturnNotFound() throws Exception {
        when(fireStationService.deleteFireStationMapping("Unknown FireStation", 0)).thenReturn(false);

        mockMvc.perform(delete("/firestation")
                        .param("address", "Unknown FireStation")
                        .param("stationNumber", "0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFireStationMapping_whenMappingExists_shouldReturnOk() throws Exception {
        Optional<FireStation> updatedFireStation = Optional.of(fireStation);
        when(fireStationService.updateFireStationMapping(fireStation)).thenReturn(updatedFireStation);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFireStationMapping_whenMappingDoesNotExist_shouldReturnNotFound() throws Exception {
        Optional<FireStation> updatedFireStation = Optional.empty();
        when(fireStationService.updateFireStationMapping(fireStation)).thenReturn(updatedFireStation);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addFireStationMapping_whenMappingExists_shouldReturnConflict() throws Exception {
        Optional<FireStation> addedFireStation = Optional.empty();
        when(fireStationService.addFireStationMapping(fireStation)).thenReturn(addedFireStation);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isConflict());
    }

    @Test
    public void addFireStationMapping_whenMappingDoesNotExist_shouldReturnOK() throws Exception {
        Optional<FireStation> addedFireStation = Optional.of(fireStation);
        when(fireStationService.addFireStationMapping(fireStation)).thenReturn(addedFireStation);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void getFireStations_whenFireStationsDoNotExist_shouldReturnNotFound() throws Exception {
        Optional<List<FireStation>> fireStations = Optional.empty();
        when(fireStationService.getFireStations()).thenReturn(fireStations);

        mockMvc.perform(get("/firestations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFireStations_whenFireStationsExist_shouldReturnOK() throws Exception {
        Optional<List<FireStation>> fireStations = Optional.of(List.of(fireStation));
        when(fireStationService.getFireStations()).thenReturn(fireStations);

        mockMvc.perform(get("/firestations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }
}