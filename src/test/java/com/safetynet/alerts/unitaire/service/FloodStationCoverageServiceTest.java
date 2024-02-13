package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.dto.FloodStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonFloodStationCoverageDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.FloodStationCoverageService;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FloodStationCoverageServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private MedicalRecordUtils medicalRecordUtils;
    @Mock
    private PersonUtils personUtils;
    @Mock
    private FireStationUtils fireStationUtils;

    @InjectMocks
    private FloodStationCoverageService floodStationCoverageService;

    @Test
    public void getFloodStationCoverage_WhenCoveragesAndCoveredPersons_ShouldReturnListOfPersonsGroupedByAddress() throws Exception {
        List<Integer> stationNumbers = List.of(3);
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").phone("841-874-6512").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("03/06/1984")
                .medications(List.of("aznol:350mg", "hydrapermazol:100mg"))
                .allergies(Collections.singletonList("nillacilan")).build());
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).medicalrecords(medicalRecords).firestations(fireStations).build();
        Map<Person, MedicalRecord> personMedicalRecordMap = new HashMap<>();
        personMedicalRecordMap.put(persons.get(0), medicalRecords.get(0));
        List<String> coveredAddresses = List.of("1509 Culver St");

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStations(anyList(), anyList())).thenReturn(coveredAddresses);
        when(personUtils.getCoveredPersonsByAddresses(anyList(), eq(coveredAddresses))).thenReturn(persons);
        when(medicalRecordUtils.createPersonToMedicalRecordMap(eq(persons), anyList())).thenReturn(personMedicalRecordMap);
        when(medicalRecordUtils.getAge(anyString())).thenReturn(40);

        List<FloodStationCoverageResponseDTO> floodStationCoverageResponseDTOS = floodStationCoverageService.getFloodStationCoverage(stationNumbers);

        assertAll("floodStationCoverageResponseDTOS",
                () -> assertEquals(1, floodStationCoverageResponseDTOS.size()),
                () -> {
                    FloodStationCoverageResponseDTO floodStationCoverageResponseDTO1 = floodStationCoverageResponseDTOS.get(0);
                    assertEquals("1509 Culver St", floodStationCoverageResponseDTO1.getAddress());
                    assertEquals(1, floodStationCoverageResponseDTO1.getPersons().size());
                    assertAll("personFloodStationCoverageDTO",
                            () -> {
                                PersonFloodStationCoverageDTO personFloodStationCoverageDTO = floodStationCoverageResponseDTO1.getPersons().get(0);
                                assertEquals("Boyd", personFloodStationCoverageDTO.getLastName());
                                assertEquals("841-874-6512", personFloodStationCoverageDTO.getPhone());
                                assertEquals(40, personFloodStationCoverageDTO.getAge());
                                assertEquals(List.of("aznol:350mg", "hydrapermazol:100mg"), personFloodStationCoverageDTO.getMedications());
                                assertEquals(List.of("nillacilan"), personFloodStationCoverageDTO.getAllergies());
                            }
                    );
                }
        );
    }

    @Test
    public void getFloodStationCoverage_WhenCoverageButNoCoveredPersons_ShouldReturnEmptyList() throws Exception {
        List<Integer> stationNumbers = List.of(3);
        DataContainer dataContainer = DataContainer.builder().persons(List.of()).medicalrecords(List.of()).firestations(List.of()).build();
        List<String> coveredAddresses = List.of("1509 Culver St");

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStations(anyList(), anyList())).thenReturn(coveredAddresses);
        when(personUtils.getCoveredPersonsByAddresses(anyList(), eq(coveredAddresses))).thenReturn(List.of());

        List<FloodStationCoverageResponseDTO> floodStationCoverageResponseDTO = floodStationCoverageService.getFloodStationCoverage(stationNumbers);

        assertEquals(0, floodStationCoverageResponseDTO.size());
    }

    @Test
    public void getFloodStationCoverage_WhenNoCoverage_ShouldReturnEmptyList() throws Exception {
        List<Integer> stationNumbers = List.of(0);
        DataContainer dataContainer = DataContainer.builder().persons(List.of()).medicalrecords(List.of()).firestations(List.of()).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStations(anyList(), anyList())).thenReturn(List.of());

        List<FloodStationCoverageResponseDTO> floodStationCoverageResponseDTO = floodStationCoverageService.getFloodStationCoverage(stationNumbers);

        assertEquals(0, floodStationCoverageResponseDTO.size());
    }

    @Test
    public void getFloodStationCoverage_WhenStationNumbersIsEmpty_ShouldReturnEmptyList() throws Exception {
        List<Integer> stationNumbers = List.of();
        DataContainer dataContainer = DataContainer.builder().persons(List.of()).medicalrecords(List.of()).firestations(List.of()).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStations(anyList(), anyList())).thenReturn(List.of());

        List<FloodStationCoverageResponseDTO> floodStationCoverageResponseDTO = floodStationCoverageService.getFloodStationCoverage(stationNumbers);

        assertEquals(0, floodStationCoverageResponseDTO.size());
    }
}
