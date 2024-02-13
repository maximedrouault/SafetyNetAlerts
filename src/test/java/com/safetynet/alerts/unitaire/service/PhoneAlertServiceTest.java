package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.dto.PersonPhoneAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.PhoneAlertService;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.PersonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneAlertServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private PersonUtils personUtils;
    @Mock
    private FireStationUtils fireStationUtils;

    @InjectMocks
    private PhoneAlertService phoneAlertService;


    @Test
    public void getPhoneAlert_WhenCoverageAndCoveredPersons_ShouldReturnListOfPhoneNumbers() throws Exception {
        int fireStationNumber = 3;
        List<String> coveredAddresses = List.of("1509 Culver St");
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").phone("841-874-6512").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).firestations(List.of()).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStation(anyList(), eq(fireStationNumber))).thenReturn(coveredAddresses);
        when(personUtils.getCoveredPersonsByAddresses(anyList(), anyList())).thenReturn(persons);

        List<PersonPhoneAlertDTO> personPhoneAlertDTOS = phoneAlertService.getPhoneAlert(fireStationNumber);

        assertEquals(1, personPhoneAlertDTOS.size());
        assertEquals("841-874-6512", personPhoneAlertDTOS.get(0).getPhone());
    }

    @Test
    public void getPhoneAlert_WhenCoverageButNoCoveredPersons_ShouldReturnEmptyList() throws Exception {
        int fireStationNumber = 3;
        List<String> coveredAddresses = List.of("1509 Culver St");
        List<Person> persons = new ArrayList<>();
        DataContainer dataContainer = DataContainer.builder().persons(persons).firestations(List.of()).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStation(anyList(), eq(fireStationNumber))).thenReturn(coveredAddresses);
        when(personUtils.getCoveredPersonsByAddresses(anyList(), anyList())).thenReturn(persons);

        List<PersonPhoneAlertDTO> personPhoneAlertDTOS = phoneAlertService.getPhoneAlert(fireStationNumber);

        assertEquals(0, personPhoneAlertDTOS.size());
    }

    @Test
    public void getPhoneAlert_WhenNoCoverage_ShouldReturnEmptyList() throws Exception {
        int fireStationNumber = 0;
        DataContainer dataContainer = DataContainer.builder().persons(List.of()).firestations(List.of()).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStation(anyList(), eq(fireStationNumber))).thenReturn(List.of());

        List<PersonPhoneAlertDTO> personPhoneAlertDTOS = phoneAlertService.getPhoneAlert(fireStationNumber);

        assertEquals(0, personPhoneAlertDTOS.size());
    }
}