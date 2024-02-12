package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.dto.PersonChildAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.ChildAlertService;
import com.safetynet.alerts.service.DataReader;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChildAlertServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private PersonUtils personUtils;
    @Mock
    private MedicalRecordUtils medicalRecordUtils;

    @InjectMocks
    private ChildAlertService childAlertService;

    @Test
    public void getChildAlert_WhenChildrenIsPresent_ShouldReturnListOfChildren() throws Exception {
        String address = "1509 Culver St";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address(address).build());
        persons.add(Person.builder().firstName("Tenley").lastName("Boyd").address(address).build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("03/06/1984").build());
        medicalRecords.add(MedicalRecord.builder().firstName("Tenley").lastName("Boyd").birthdate("02/18/2012").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).medicalrecords(medicalRecords).build();
        Map<Person, MedicalRecord> personMedicalRecordMap = new HashMap<>();
        personMedicalRecordMap.put(persons.get(0), medicalRecords.get(0));
        personMedicalRecordMap.put(persons.get(1), medicalRecords.get(1));

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(dataContainer.getPersons());
        when(medicalRecordUtils.createPersonToMedicalRecordMap(eq(persons), anyList())).thenReturn(personMedicalRecordMap);
        when(medicalRecordUtils.getChildren(personMedicalRecordMap)).thenReturn(Collections.singletonList(persons.get(1)));
        when(medicalRecordUtils.getAge(anyString())).thenReturn(10);
        when(medicalRecordUtils.getFamilyMembers(anyList(), any(Person.class))).thenReturn(Collections.singletonList(persons.get(0)));

        List<PersonChildAlertDTO> personChildAlertDTOS = childAlertService.getChildAlert(address);

        assertAll("personChildAlertDTOS",
                () -> assertEquals(1, personChildAlertDTOS.size()),
                () -> {
                    PersonChildAlertDTO personChildAlertDTO = personChildAlertDTOS.get(0);
                    assertEquals("Tenley", personChildAlertDTO.getFirstName());
                    assertEquals("Boyd", personChildAlertDTO.getLastName());
                    assertEquals(10, personChildAlertDTO.getAge());
                    assertEquals(1, personChildAlertDTO.getFamilyMembers().size());
                    assertEquals(persons.get(0), personChildAlertDTO.getFamilyMembers().get(0));
                    assertEquals("John", personChildAlertDTO.getFamilyMembers().get(0).getFirstName());
                    assertEquals("Boyd", personChildAlertDTO.getFamilyMembers().get(0).getLastName());
                    assertEquals("1509 Culver St", personChildAlertDTO.getFamilyMembers().get(0).getAddress());
                }
        );
    }

    @Test
    public void getChildAlert_WhenNoBodyFoundAtAddress_ShouldReturnEmptyList() throws Exception {
        String address = "Unknown address";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(Collections.emptyList());

        List<PersonChildAlertDTO> personChildAlertDTOS = childAlertService.getChildAlert(address);

        assertEquals(0, personChildAlertDTOS.size());
    }

    @Test
    public void getChildAlert_WhenChildrenIsNotPresent_ShouldReturnEmptyList() throws Exception {
        String address = "1509 Culver St";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address(address).build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("03/06/1984").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).medicalrecords(medicalRecords).build();
        Map<Person, MedicalRecord> personMedicalRecordMap = new HashMap<>();
        personMedicalRecordMap.put(persons.get(0), medicalRecords.get(0));

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(dataContainer.getPersons());
        when(medicalRecordUtils.createPersonToMedicalRecordMap(eq(persons), anyList())).thenReturn(personMedicalRecordMap);
        when(medicalRecordUtils.getChildren(personMedicalRecordMap)).thenReturn(Collections.emptyList());

        List<PersonChildAlertDTO> personChildAlertDTOS = childAlertService.getChildAlert(address);

        assertEquals(0, personChildAlertDTOS.size());
    }

    @Test
    public void getChildAlert_WhenAddressIsEmpty_ShouldReturnEmptyList() throws Exception {
        String address = "";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getCoveredPersonsByAddress(anyList(), eq(address))).thenReturn(Collections.emptyList());

        List<PersonChildAlertDTO> personChildAlertDTOS = childAlertService.getChildAlert(address);

        assertEquals(0, personChildAlertDTOS.size());
    }
}