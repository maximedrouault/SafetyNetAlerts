package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.dto.PersonCommunityEmailDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.CommunityEmailService;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.utils.PersonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommunityEmailServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private PersonUtils personUtils;

    @InjectMocks
    private CommunityEmailService communityEmailService;

    @Test
    public void getCommunityEmail_WhenPersonsFound_ShouldReturnTheListOfPersonsEmails() throws Exception {
        String city = "Culver";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").city(city).email("jaboyd@email.com").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getPersonsInCity(anyList(), eq(city))).thenReturn(dataContainer.getPersons());

        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = communityEmailService.getCommunityEmail(city);

        assertEquals(1, personCommunityEmailDTOS.size());
        assertEquals("jaboyd@email.com", personCommunityEmailDTOS.get(0).getEmail());
    }

    @Test
    public void getCommunityEmail_WhenNoBodyFound_ShouldReturnEmptyResponseObject() throws Exception {
        String city = "Unknown city";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").city("Culver").email("jaboyd@email.com").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getPersonsInCity(anyList(), eq(city))).thenReturn(Collections.emptyList());

        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = communityEmailService.getCommunityEmail(city);

        assertEquals(0, personCommunityEmailDTOS.size());
    }

    @Test
    public void getCommunityEmail_WhenCityIsEmpty_ShouldReturnEmptyResponseObject() throws Exception {
        String city = "";
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").city("Culver").email("jaboyd@email.com").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);
        when(personUtils.getPersonsInCity(anyList(), eq(city))).thenReturn(Collections.emptyList());

        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = communityEmailService.getCommunityEmail(city);

        assertEquals(0, personCommunityEmailDTOS.size());
    }
}