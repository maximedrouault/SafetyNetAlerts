package com.safetynet.alerts.unitaire.utils;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.PersonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonUtilsTest {

    private static PersonUtils personUtils;
    private List<Person> persons;

    @BeforeAll
    public static void setUp() {
        personUtils = new PersonUtils();
    }

    @BeforeEach
    public void setUpPerTest() {
        persons = new ArrayList<>();

        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Boyd");
        person1.setAddress("1509 Culver St");
        person1.setCity("Culver");

        Person person2 = new Person();
        person2.setFirstName("Jacob");
        person2.setLastName("Boyd");
        person2.setAddress("1509 Culver St");
        person2.setCity("Culver");

        Person person3 = new Person();
        person3.setFirstName("Foster");
        person3.setLastName("Shepard");
        person3.setAddress("748 Townings Dr");
        person3.setCity("Culver");

        Person person4 = new Person();
        person4.setFirstName("John");
        person4.setLastName("Boyd");
        person4.setAddress("951 LoneTree Rd");
        person4.setCity("Culver");

        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);
    }

    // getCoveredPersonsByAddress
    @Test
    public void getCoveredPersonsWithMatchingAddress() {
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(persons, "1509 Culver St");

        assertEquals(2, coveredPersons.size());
        assertEquals("1509 Culver St", coveredPersons.get(0).getAddress());
        assertEquals("1509 Culver St", coveredPersons.get(1).getAddress());
    }

    @Test
    public void getCoveredPersonsWhenAddressNotFound() {
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(persons, "unknown address");

        assertTrue(coveredPersons.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getCoveredPersonsWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = new ArrayList<>();

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(emptyListOfPersons, "1509 Culver St");

        assertTrue(coveredPersons.isEmpty());
    }

    @Test
    public void getCoveredPersonsWhenAddressIsEmpty() {
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(persons, "");

        assertTrue(coveredPersons.isEmpty());
    }


    // getPersonsInCity
    @Test
    public void getPersonsInCityWithMatchingCity() {
        List<Person> personsInCity = personUtils.getPersonsInCity(persons, "Culver");

        assertEquals(4, personsInCity.size());
        assertFalse(personsInCity.stream().anyMatch(person -> !person.getCity().equals("Culver")));
    }

    @Test
    public void getPersonsInCityWhenCityNotFound() {
        List<Person> personsInCity = personUtils.getPersonsInCity(persons, "unknown city");

        assertTrue(personsInCity.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getPersonsInCityWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = new ArrayList<>();

        List<Person> personsInCity = personUtils.getPersonsInCity(emptyListOfPersons, "Culver");

        assertTrue(personsInCity.isEmpty());
    }

    @Test
    public void getPersonsInCityWhenCityIsEmpty() {
        List<Person> personsInCity = personUtils.getPersonsInCity(persons, "");

        assertTrue(personsInCity.isEmpty());
    }


    // getPersonsByFirstNameAndLastName
    @Test
    public void getPersonsByFirstNameAndLastNameWithMatchingFirstNameAndLastName() {
        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "John", "Boyd");

        assertEquals(2, personList.size());
        assertEquals("John", personList.get(0).getFirstName()); // We expect to have two homonyms to test this case
        assertEquals("Boyd", personList.get(0).getLastName());
        assertEquals("John", personList.get(1).getFirstName());
        assertEquals("Boyd", personList.get(1).getLastName());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenFirstNameNotFound() {
        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "unknown firstName", "Boyd");

        assertTrue(personList.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenLastNameNotFound() {
        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "John", "unknown lastName");

        assertTrue(personList.isEmpty());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = new ArrayList<>();

        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(emptyListOfPersons, "John", "Boyd");

        assertTrue(personList.isEmpty());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenFirstNameIsEmpty() {
        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "", "Boyd");

        assertTrue(personList.isEmpty());
    }

    @Test
    public void getPersonsByFirstNameAndLastNameWhenLastNameIsEmpty() {
        List<Person> personList = personUtils.getPersonsByFirstNameAndLastName(persons, "John", "");

        assertTrue(personList.isEmpty());    }


    // getCoveredPersonsByAddresses
    @Test
    public void getCoveredPersonsByAddressesWithMatchingAddresses() {
        List<String> addresses = new ArrayList<>();
        addresses.add("748 Townings Dr");
        addresses.add("951 LoneTree Rd");

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(persons, addresses);

        assertEquals(2, coveredPersons.size());
        assertEquals("Foster", coveredPersons.get(0).getFirstName());
        assertEquals("Shepard", coveredPersons.get(0).getLastName());
        assertEquals("John", coveredPersons.get(1).getFirstName());
        assertEquals("Boyd", coveredPersons.get(1).getLastName());
    }

    @Test
    public void getCoveredPersonsByAddressesWhenAddressesNotFound() {
        List<String> unknownAddresses = new ArrayList<>();
        unknownAddresses.add("unknown address");

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(persons, unknownAddresses);

        assertTrue(coveredPersons.isEmpty()); // We expect to have an empty List;
    }

    @Test
    public void getCoveredPersonsByAddressesWhenPersonsListIsEmpty() {
        List<Person> emptyListOfPersons = new ArrayList<>();
        List<String> addresses = new ArrayList<>();
        addresses.add("748 Townings Dr");
        addresses.add("951 LoneTree Rd");

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(emptyListOfPersons, addresses);

        assertTrue(coveredPersons.isEmpty());
    }

    @Test
    public void getCoveredPersonsByAddressesWhenAddressesIsEmpty() {
        List<String> addresses = new ArrayList<>();

        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(persons, addresses);

        assertTrue(coveredPersons.isEmpty());
    }
}
