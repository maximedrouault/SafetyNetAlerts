package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.PersonInfoService;
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
public class PersonInfoServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private MedicalRecordUtils medicalRecordUtils;
    @Mock
    private PersonUtils personUtils;

    @InjectMocks
    private PersonInfoService personInfoService;


    @Test
    public void getPersonInfo_WhenPersonFound_ShouldReturnAPersonObject() throws Exception {
        String firstName = "John";
        String lastName = "Boyd";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").phone("841-874-6512").email("jaboyd@email.com").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("03/06/1984")
                .medications(List.of("aznol:350mg", "hydrapermazol:100mg"))
                .allergies(Collections.singletonList("nillacilan")).build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).medicalrecords(medicalRecords).build();
        Map<Person, MedicalRecord> personMedicalRecordMap = new HashMap<>();
        personMedicalRecordMap.put(persons.get(0), medicalRecords.get(0));

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getPersonsByFirstNameAndLastName(anyList(), eq(firstName), eq(lastName))).thenReturn(persons);
        when(medicalRecordUtils.createPersonToMedicalRecordMap(eq(persons), anyList())).thenReturn(personMedicalRecordMap);
        when(medicalRecordUtils.getAge(anyString())).thenReturn(40);

        List<PersonInfoDTO> personInfoDTOS = personInfoService.getPersonInfo(firstName, lastName);

        assertAll("personInfoDTOS",
                () -> assertEquals(1, personInfoDTOS.size()),
                () -> {
                    PersonInfoDTO personInfoDTO = personInfoDTOS.get(0);
                    assertEquals("Boyd", personInfoDTO.getLastName());
                    assertEquals("1509 Culver St", personInfoDTO.getAddress());
                    assertEquals(40, personInfoDTO.getAge());
                    assertEquals("jaboyd@email.com", personInfoDTO.getEmail());
                    assertEquals(List.of("aznol:350mg", "hydrapermazol:100mg"), personInfoDTO.getMedications());
                    assertEquals(List.of("nillacilan"), personInfoDTO.getAllergies());
                }
        );
    }

    @Test
    public void getPersonInfo_WhenPersonNotFound_ShouldReturnEmptyList() throws Exception {
        String firstName = "John";
        String lastName = "Boyd";
        DataContainer dataContainer = DataContainer.builder().persons(List.of()).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getPersonsByFirstNameAndLastName(anyList(), eq(firstName), eq(lastName))).thenReturn(List.of());

        List<PersonInfoDTO> personInfoDTOS = personInfoService.getPersonInfo(firstName, lastName);

        assertEquals(0, personInfoDTOS.size());
    }
}
