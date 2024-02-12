package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.dto.FireStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonFireStationCoverageDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.FireStationCoverageService;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireStationCoverageServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private MedicalRecordUtils medicalRecordUtils;
    @Mock
    private PersonUtils personUtils;
    @Mock
    private FireStationUtils fireStationUtils;

    @InjectMocks
    private FireStationCoverageService fireStationCoverageService;

    @Test
    public void getFireStationCoverage_WhenCoverageAndCoveredPersons_ShouldReturnListOfPersonsWithCounts() throws Exception {
        int stationNumber = 3;
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").phone("841-874-6512").build());
        persons.add(Person.builder().firstName("Tenley").lastName("Boyd").address("1509 Culver St").phone("841-874-6512").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).medicalrecords(List.of()).firestations(List.of()).build();
        List<String> coveredAddresses = List.of("1509 Culver St");
        int[] adultsAndChildrenCounts = new int[]{1, 1};

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStation(anyList(), eq(stationNumber))).thenReturn(coveredAddresses);
        when(personUtils.getCoveredPersonsByAddresses(anyList(), anyList())).thenReturn(dataContainer.getPersons());
        when(medicalRecordUtils.countAdultsAndChildren(anyList(), anyList())).thenReturn(adultsAndChildrenCounts);

        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = fireStationCoverageService.getFireStationCoverage(stationNumber);

        assertAll("fireStationCoverageResponseDTO",
                () -> assertEquals(2, fireStationCoverageResponseDTO.getPersons().size()),
                () -> assertEquals(1, fireStationCoverageResponseDTO.getAdultsCount()),
                () -> assertEquals(1, fireStationCoverageResponseDTO.getChildrenCount()),
                () -> {
                    PersonFireStationCoverageDTO adultDTO = fireStationCoverageResponseDTO.getPersons().get(0);
                    assertEquals("John", adultDTO.getFirstName());
                    assertEquals("Boyd", adultDTO.getLastName());
                    assertEquals("1509 Culver St", adultDTO.getAddress());
                    assertEquals("841-874-6512", adultDTO.getPhone());
                },
                () ->{
                    PersonFireStationCoverageDTO childDTO = fireStationCoverageResponseDTO.getPersons().get(1);
                    assertEquals("Tenley", childDTO.getFirstName());
                    assertEquals("Boyd", childDTO.getLastName());
                    assertEquals("1509 Culver St", childDTO.getAddress());
                    assertEquals("841-874-6512", childDTO.getPhone());
                }
        );
    }

    @Test
    public void getFireStationCoverage_WhenCoverageButNoCoveredPersons_ShouldReturnEmptyResponseObject() throws Exception {
        int stationNumber = 3;
        DataContainer dataContainer = DataContainer.builder().persons(List.of()).medicalrecords(List.of()).firestations(List.of()).build();
        List<String> coveredAddresses = List.of("1509 Culver St");

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStation(anyList(), eq(stationNumber))).thenReturn(coveredAddresses);
        when(personUtils.getCoveredPersonsByAddresses(anyList(), anyList())).thenReturn(List.of());

        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = fireStationCoverageService.getFireStationCoverage(stationNumber);

        assertEquals(0, fireStationCoverageResponseDTO.getPersons().size());
        assertEquals(0,fireStationCoverageResponseDTO.getAdultsCount());
        assertEquals(0, fireStationCoverageResponseDTO.getChildrenCount());
    }

    @Test
    public void getFireStationCoverage_WhenNoCoverage_ShouldReturnEmptyResponseObject() throws Exception {
        int stationNumber = 0;
        DataContainer dataContainer = DataContainer.builder().persons(List.of()).medicalrecords(List.of()).firestations(List.of()).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(fireStationUtils.getAddressesCoveredByFireStation(anyList(), eq(stationNumber))).thenReturn(List.of());

        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = fireStationCoverageService.getFireStationCoverage(stationNumber);

        assertEquals(0, fireStationCoverageResponseDTO.getPersons().size());
        assertEquals(0,fireStationCoverageResponseDTO.getAdultsCount());
        assertEquals(0, fireStationCoverageResponseDTO.getChildrenCount());
    }
}