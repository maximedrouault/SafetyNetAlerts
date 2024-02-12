package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.dto.FireAddressInfoResponseDTO;
import com.safetynet.alerts.dto.PersonFireAddressInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.FireAddressInfoService;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireAddressInfoServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private PersonUtils personUtils;
    @Mock
    private MedicalRecordUtils medicalRecordUtils;
    @Mock
    private FireStationUtils fireStationUtils;

    @InjectMocks
    private FireAddressInfoService fireAddressInfoService;

    @Test
    public void getFireAddressInfo_WhenPersonsCoveredByFireStation_ShouldReturnListOfPersons() throws Exception {
        String address = "1509 Culver St";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address(address).phone("841-874-6512").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("03/06/1984")
                .medications(List.of("aznol:350mg", "hydrapermazol:100mg"))
                .allergies(Collections.singletonList("nillacilan")).build());
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).medicalrecords(medicalRecords).firestations(fireStations).build();
        Map<Person, MedicalRecord> personMedicalRecordMap = new HashMap<>();
        personMedicalRecordMap.put(persons.get(0), medicalRecords.get(0));

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(dataContainer.getPersons());
        when(fireStationUtils.getFireStationNumberByAddress(anyList(), eq(address))).thenReturn(Optional.of(3));
        when(medicalRecordUtils.createPersonToMedicalRecordMap(eq(persons), anyList())).thenReturn(personMedicalRecordMap);
        when(medicalRecordUtils.getAge(anyString())).thenReturn(40);

        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = fireAddressInfoService.getFireAddressInfo(address);

        assertAll("FireAddressInfoResponseDTO",
                () -> assertEquals(3, fireAddressInfoResponseDTO.getStationNumber()),
                () -> assertEquals(1, fireAddressInfoResponseDTO.getPersons().size()),
                () -> {
                    PersonFireAddressInfoDTO personDTO = fireAddressInfoResponseDTO.getPersons().get(0);
                    assertEquals("Boyd", personDTO.getLastName());
                    assertEquals("841-874-6512", personDTO.getPhone());
                    assertEquals(40, personDTO.getAge());
                    assertEquals(List.of("aznol:350mg", "hydrapermazol:100mg"), personDTO.getMedications());
                    assertEquals(List.of("nillacilan"), personDTO.getAllergies());
                }
        );
    }

    @Test
    public void getFireAddressInfo_WhenNoBodyFoundAtAddress_ShouldReturnEmptyList() throws Exception{
        String address = "Unknown address";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").phone("841-874-6512").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(Collections.emptyList());

        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = fireAddressInfoService.getFireAddressInfo(address);

        assertEquals(0, fireAddressInfoResponseDTO.getStationNumber());
        assertTrue(fireAddressInfoResponseDTO.getPersons().isEmpty());
    }

    @Test
    public void getFireAddressInfo_WhenNoFireStationCoversTheAddress_ShouldReturnEmptyList() throws Exception {
        String address = "1509 Culver St";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address(address).phone("841-874-6512").build());
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("Unknown address").station(3).build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(dataContainer.getPersons());
        when(fireStationUtils.getFireStationNumberByAddress(anyList(), eq(address))).thenReturn(Optional.empty());

        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = fireAddressInfoService.getFireAddressInfo(address);

        assertEquals(0, fireAddressInfoResponseDTO.getStationNumber());
        assertTrue(fireAddressInfoResponseDTO.getPersons().isEmpty());
    }

    @Test
    public void getFireAddressInfo_WhenAddressIsEmpty_ShouldReturnEmptyList() throws Exception {
        String address = "Unknown address";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").phone("841-874-6512").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(Collections.emptyList());

        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = fireAddressInfoService.getFireAddressInfo(address);

        assertEquals(0, fireAddressInfoResponseDTO.getStationNumber());
        assertTrue(fireAddressInfoResponseDTO.getPersons().isEmpty());
    }
}