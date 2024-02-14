package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.DataWriter;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private DataWriter dataWriter;

    @InjectMocks
    private PersonService personService;


    @Test
    public void deletePerson_WhenPersonExists_ShouldReturnTrue() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean deletedPerson = personService.deletePerson(persons.get(0));

        assertTrue(deletedPerson);
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void deletePerson_WhenPersonFirstNameDoesNotMatch_ShouldReturnFalse() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        Person personToDelete = Person.builder().firstName("Tenley").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean deletedPerson = personService.deletePerson(personToDelete);

        assertFalse(deletedPerson);
    }

    @Test
    public void deletePerson_WhenPersonLastNameDoesNotMatch_ShouldReturnFalse() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        Person personToDelete = Person.builder().firstName("John").lastName("Foster").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean deletedPerson = personService.deletePerson(personToDelete);

        assertFalse(deletedPerson);
    }

    @Test
    public void updatePerson_WhenPersonExists_ShouldReturnUpdatedPerson() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St")
                .city("Culver").zip("97451").phone("841-874-6512").email("jaboyd@email.com").build());
        Person personToUpdate = Person.builder().firstName("John").lastName("Boyd").address("29 15th St")
                .city("Culvert").zip("97452").phone("841-874-6513").email("drk@email.com").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<Person> updatedPerson = personService.updatePerson(personToUpdate);

        assertTrue(updatedPerson.isPresent());
        assertAll("updatedPerson", () -> {
            assertEquals("John", updatedPerson.get().getFirstName());
            assertEquals("Boyd", updatedPerson.get().getLastName());
            assertEquals("29 15th St", updatedPerson.get().getAddress());
            assertEquals("Culvert", updatedPerson.get().getCity());
            assertEquals("97452", updatedPerson.get().getZip());
            assertEquals("841-874-6513", updatedPerson.get().getPhone());
            assertEquals("drk@email.com", updatedPerson.get().getEmail());
        });
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void updatePerson_WhenPersonFirstNameDoesNotMatch_ShouldReturnEmptyOptional() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        Person personToUpdate = Person.builder().firstName("Tenley").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<Person> updatedPerson = personService.updatePerson(personToUpdate);

        assertTrue(updatedPerson.isEmpty());
    }

    @Test
    public void updatePerson_WhenPersonLastNameDoesNotMatch_ShouldReturnEmptyOptional() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        Person personToUpdate = Person.builder().firstName("John").lastName("Foster").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<Person> updatedPerson = personService.updatePerson(personToUpdate);

        assertTrue(updatedPerson.isEmpty());
    }

    @Test
    public void addPerson_WhenPersonDoesNotExist_ShouldReturnAddedFireStation() throws Exception {
        List<Person> persons = new ArrayList<>();
        Person personToAdd = Person.builder().firstName("John").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<Person> addedPerson = personService.addPerson(personToAdd);

        assertTrue(addedPerson.isPresent());
        assertEquals(addedPerson.get(), personToAdd);
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void addPerson_WhenPersonAlreadyExists_ShouldReturnEmptyOptional() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        Person personToAdd = Person.builder().firstName("John").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<Person> addedPerson = personService.addPerson(personToAdd);

        assertTrue(addedPerson.isEmpty());
    }

    @Test
    public void addPerson_WhenPersonSameFirstNameMatch_ShouldReturnEmptyOptional() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        persons.add(Person.builder().firstName("Tenley").lastName("Boyd").build());
        Person personToAdd = Person.builder().firstName("Tenley").lastName("Boyd").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<Person> addedPerson = personService.addPerson(personToAdd);

        assertTrue(addedPerson.isEmpty());
    }

    @Test
    public void addPerson_WhenPersonSameLastNameMatch_ShouldReturnEmptyOptional() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        persons.add(Person.builder().firstName("John").lastName("Foster").build());
        Person personToAdd = Person.builder().firstName("John").lastName("Foster").build();
        DataContainer dataContainer = DataContainer.builder().persons(persons).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<Person> addedPerson = personService.addPerson(personToAdd);

        assertTrue(addedPerson.isEmpty());
    }
}