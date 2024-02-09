package com.safetynet.alerts.unitaire.utils;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.PersonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonUtilsTest {

    private static PersonUtils personUtils;

    @BeforeAll
    public static void setUp() {
        personUtils = new PersonUtils();
    }


    private List<Person> getPersonsForTest() {
        return List.of(
                Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").city("Culver").build(),
                Person.builder().firstName("Jacob").lastName("Boyd").address("1509 Culver St").city("Culver").build(),
                Person.builder().firstName("Foster").lastName("Shepard").address("748 Townings Dr").city("Culver").build(),
                Person.builder().firstName("John").lastName("Boyd").address("951 LoneTree Rd").city("Culver").build()
        );
    }

    // getCoveredPersonsByAddress
    @Test
    public void getCoveredPersonsWithMatchingAddress() {
        List<Person> persons = getPersonsForTest();

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(persons, "1509 Culver St");

        assertEquals(2, coveredPersons.size());
        assertEquals("1509 Culver St", coveredPersons.get(0).getAddress());
        assertEquals("1509 Culver St", coveredPersons.get(1).getAddress());
    }

    @Test
    public void getCoveredPersonsWhenAddressNotFound() {
        List<Person> persons = getPersonsForTest();

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(persons, "unknown address");

        assertTrue(coveredPersons.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getCoveredPersonsWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = List.of();

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(emptyListOfPersons, "1509 Culver St");

        assertTrue(coveredPersons.isEmpty());
    }

    @Test
    public void getCoveredPersonsWhenAddressIsEmpty() {
        List<Person> persons = getPersonsForTest();

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(persons, "");

        assertTrue(coveredPersons.isEmpty());
    }


    // getPersonsInCity
    @Test
    public void getPersonsInCityWithMatchingCity() {
        List<Person> persons = getPersonsForTest();

        List<Person> personsInCity = personUtils.getPersonsInCity(persons, "Culver");

        assertEquals(4, personsInCity.size());
        assertFalse(personsInCity.stream().anyMatch(person -> !person.getCity().equals("Culver")));
    }

    @Test
    public void getPersonsInCityWhenCityNotFound() {
        List<Person> persons = getPersonsForTest();

        List<Person> personsInCity = personUtils.getPersonsInCity(persons, "unknown city");

        assertTrue(personsInCity.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getPersonsInCityWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = List.of();

        List<Person> personsInCity = personUtils.getPersonsInCity(emptyListOfPersons, "Culver");

        assertTrue(personsInCity.isEmpty());
    }

    @Test
    public void getPersonsInCityWhenCityIsEmpty() {
        List<Person> persons = getPersonsForTest();

        List<Person> personsInCity = personUtils.getPersonsInCity(persons, "");

        assertTrue(personsInCity.isEmpty());
    }


    // getPersonsByFirstNameAndLastName
    @Test
    public void getPersonsByFirstNameAndLastNameWithMatchingFirstNameAndLastName() {
        List<Person> persons = getPersonsForTest();

        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "John", "Boyd");

        assertEquals(2, personList.size());
        assertEquals("John", personList.get(0).getFirstName()); // We expect to have two homonyms to test this case
        assertEquals("Boyd", personList.get(0).getLastName());
        assertEquals("John", personList.get(1).getFirstName());
        assertEquals("Boyd", personList.get(1).getLastName());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenFirstNameNotFound() {
        List<Person> persons = getPersonsForTest();

        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "unknown firstName", "Boyd");

        assertTrue(personList.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenLastNameNotFound() {
        List<Person> persons = getPersonsForTest();

        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "John", "unknown lastName");

        assertTrue(personList.isEmpty());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = List.of();

        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(emptyListOfPersons, "John", "Boyd");

        assertTrue(personList.isEmpty());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenFirstNameIsEmpty() {
        List<Person> persons = getPersonsForTest();

        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "", "Boyd");

        assertTrue(personList.isEmpty());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenLastNameIsEmpty() {
        List<Person> persons = getPersonsForTest();

        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "John", "");

        assertTrue(personList.isEmpty());    }


    // getCoveredPersonsByAddresses
    @Test
    public void getCoveredPersonsByAddressesWithMatchingAddresses() {
        List<Person> persons = getPersonsForTest();
        List<String> addresses = List.of("748 Townings Dr", "951 LoneTree Rd");

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(persons, addresses);

        assertEquals(2, coveredPersons.size());
        assertEquals("Foster", coveredPersons.get(0).getFirstName());
        assertEquals("Shepard", coveredPersons.get(0).getLastName());
        assertEquals("John", coveredPersons.get(1).getFirstName());
        assertEquals("Boyd", coveredPersons.get(1).getLastName());
    }

    @Test
    public void getCoveredPersonsByAddressesWhenAddressesNotFound() {
        List<Person> persons = getPersonsForTest();
        List<String> unknownAddresses = List.of("unknown address");

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(persons, unknownAddresses);

        assertTrue(coveredPersons.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getCoveredPersonsByAddressesWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = List.of();
        List<String> addresses = List.of("748 Townings Dr", "951 LoneTree Rd");

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(emptyListOfPersons, addresses);

        assertTrue(coveredPersons.isEmpty());
    }

    @Test
    public void getCoveredPersonsByAddressesWhenAddressesIsEmpty() {
        List<Person> persons = getPersonsForTest();
        List<String> addresses = List.of();

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(persons, addresses);

        assertTrue(coveredPersons.isEmpty());
    }
}