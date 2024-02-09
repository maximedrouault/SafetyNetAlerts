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

    private FireStation fireStation;
    private String jsonBody;

    @BeforeEach
    public void setUp() throws Exception {
        fireStation = FireStation.builder().build();
        jsonBody = objectMapper.writeValueAsString(fireStation); // Convert object to JSON
    }

    @Test
    public void deleteFireStationMapping_whenMappingExists_shouldReturnOk() throws Exception {
        when(fireStationService.deleteFireStationMapping(fireStation)).thenReturn(true);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFireStationMapping_whenMappingDoesNotExist_shouldReturnNotFound() throws Exception {
        when(fireStationService.deleteFireStationMapping(fireStation)).thenReturn(false);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
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
}