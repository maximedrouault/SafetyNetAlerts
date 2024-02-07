package com.safetynet.alerts.unitaire.controller;

import com.safetynet.alerts.controller.SpecificRequestController;
import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SpecificRequestController.class)
public class SpecificRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildAlertService childAlertService;
    @MockBean
    private FireAddressInfoService fireAddressInfoService;
    @MockBean
    private FireStationCoverageService fireStationCoverageService;
    @MockBean
    private PhoneAlertService phoneAlertService;
    @MockBean
    private CommunityEmailService communityEmailService;
    @MockBean
    private PersonInfoService personInfoService;
    @MockBean
    private FloodStationCoverageService floodStationCoverageService;


    // ChildAlertController
    @Test
    public void getChildAlert_whenResponseIsNotEmpty_shouldReturnOk() throws Exception {
        List<PersonChildAlertDTO> personChildAlertDTOS = Collections.singletonList(
                PersonChildAlertDTO.builder().build()
        );
        when(childAlertService.getChildAlert(anyString())).thenReturn(personChildAlertDTOS);

        mockMvc.perform(get("/childAlert")
                        .param("address", "some address"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getChildAlert_whenResponseIsEmpty_shouldReturnNotFound() throws Exception {
        when(childAlertService.getChildAlert(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/childAlert")
                        .param("address", "some address"))
                .andExpect(status().isNotFound());
    }

    // FireAddressInfoController
    @Test
    public void getFireAddressInfo_whenResponseIsNotEmpty_shouldReturnOk() throws Exception {
        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = FireAddressInfoResponseDTO.builder()
                .persons(Collections.singletonList(
                        PersonFireAddressInfoDTO.builder().build()
                ))
                .build();
        when(fireAddressInfoService.getFireAddressInfo(anyString())).thenReturn(fireAddressInfoResponseDTO);

        mockMvc.perform(get("/fire")
                        .param("address", "some address"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getFireAddressInfo_whenResponseIsEmpty_shouldReturnNotFound() throws Exception {
        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = FireAddressInfoResponseDTO.builder()
                .persons(Collections.emptyList())
                .build();
        when(fireAddressInfoService.getFireAddressInfo(anyString())).thenReturn(fireAddressInfoResponseDTO);

        mockMvc.perform(get("/fire")
                        .param("address", "some address"))
                .andExpect(status().isNotFound());
    }

    // FireStationCoverageController
    @Test
    public void getFireStationCoverage_whenResponseIsNotEmpty_shouldReturnOk() throws Exception {
        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = FireStationCoverageResponseDTO.builder()
                .persons(Collections.singletonList(
                        PersonFireStationCoverageDTO.builder().build()
                ))
                .build();
        when(fireStationCoverageService.getFireStationCoverage(anyInt())).thenReturn(fireStationCoverageResponseDTO);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getFireStationCoverage_whenResponseIsEmpty_shouldReturnNotFound() throws Exception {
        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = FireStationCoverageResponseDTO.builder()
                .persons(Collections.emptyList())
                .build();
        when(fireStationCoverageService.getFireStationCoverage(anyInt())).thenReturn(fireStationCoverageResponseDTO);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isNotFound());
    }

    // PhoneAlertController
    @Test
    public void getPhoneAlert_whenResponseIsNotEmpty_shouldReturnOk() throws Exception {
        List<PersonPhoneAlertDTO> personPhoneAlertDTOS = Collections.singletonList(
                PersonPhoneAlertDTO.builder().build()
        );
        when(phoneAlertService.getPhoneAlert(anyInt())).thenReturn(personPhoneAlertDTOS);

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getPhoneAlert_whenResponseIsEmpty_shouldReturnNotFound() throws Exception {
        when(phoneAlertService.getPhoneAlert(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isNotFound());
    }

    // CommunityEmailController
    @Test
    public void getCommunityEmail_whenResponseIsNotEmpty_shouldReturnOk() throws Exception {
        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = Collections.singletonList(
                PersonCommunityEmailDTO.builder().build()
        );
        when(communityEmailService.getCommunityEmail(anyString())).thenReturn(personCommunityEmailDTOS);

        mockMvc.perform(get("/communityEmail")
                        .param("city", "some city"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getCommunityEmail_whenResponseIsEmpty_shouldReturnNotFound() throws Exception {
        when(communityEmailService.getCommunityEmail(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/communityEmail")
                        .param("city", "some city"))
                .andExpect(status().isNotFound());
    }

    // PersonInfoController
    @Test
    public void getPersonInfo_whenResponseIsNotEmpty_shouldReturnOk() throws Exception {
        List<PersonInfoDTO> personInfoDTOS = Collections.singletonList(
                PersonInfoDTO.builder().build()
        );
        when(personInfoService.getPersonInfo(anyString(), anyString())).thenReturn(personInfoDTOS);

        mockMvc.perform(get("/personInfo")
                        .param("firstName", "some firstName")
                        .param("lastName", "some lastName"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getPersonInfo_whenResponseIsEmpty_shouldReturnNotFound() throws Exception {
        when(personInfoService.getPersonInfo(anyString(), anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/personInfo")
                        .param("firstName", "some firstName")
                        .param("lastName", "some lastName"))
                .andExpect(status().isNotFound());
    }

    // FloodStationCoverageController
    @Test
    public void getFloodStationCoverage_whenResponseIsNotEmpty_shouldReturnOk() throws Exception {
        List<FloodStationCoverageResponseDTO> floodStationCoverageResponseDTOS = Collections.singletonList(
                FloodStationCoverageResponseDTO.builder().build());
        when(floodStationCoverageService.getFloodStationCoverage(anyList())).thenReturn(floodStationCoverageResponseDTOS);

        mockMvc.perform(get("/flood")
                        .param("stations", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getFloodStationCoverage_whenResponseIsEmpty_shouldReturnNotFound() throws Exception {
        when(floodStationCoverageService.getFloodStationCoverage(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/flood")
                        .param("stations", "1", "2"))
                .andExpect(status().isNotFound());
    }
}