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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());
        // Create a simulated year of birthdate to make the test immutable
        String simulatedYearOfBirtDate = LocalDate.now().minusYears(10).format(DateTimeFormatter.ofPattern("yyyy"));
        medicalRecords.add(MedicalRecord.builder().firstName("Tenley").lastName("Boyd").birthdate("01/01/" + simulatedYearOfBirtDate).build());
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

        assertEquals(1, personChildAlertDTOS.size());
        assertEquals("Tenley", personChildAlertDTOS.get(0).getFirstName());
        assertEquals("Boyd", personChildAlertDTOS.get(0).getLastName());
        assertEquals(10, personChildAlertDTOS.get(0).getAge());
        assertEquals(1, personChildAlertDTOS.get(0).getFamilyMembers().size());
        assertEquals(persons.get(0), personChildAlertDTOS.get(0).getFamilyMembers().get(0));
        assertEquals("John", personChildAlertDTOS.get(0).getFamilyMembers().get(0).getFirstName());
        assertEquals("Boyd", personChildAlertDTOS.get(0).getFamilyMembers().get(0).getLastName());
        assertEquals("1509 Culver St", personChildAlertDTOS.get(0).getFamilyMembers().get(0).getAddress());
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
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());
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
        persons.add(Person.builder().firstName("John").lastName("Boyd").address(address).build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());
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
}