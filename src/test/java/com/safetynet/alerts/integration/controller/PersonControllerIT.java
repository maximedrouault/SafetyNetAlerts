package com.safetynet.alerts.integration.controller;

import com.safetynet.alerts.model.Person;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Log4j2
public class PersonControllerIT {

    @LocalServerPort
    private int port;

    private String baseUrl;
    private String endpoint;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // Create a temporary data file. Copy "testData.json" to "tempTestData.json" for tests.
        Path sourceTestData = Paths.get("src/test/resources/testData.json");
        Path tempTestData = Paths.get("src/test/resources/tempTestData.json");

        endpoint = "/person";
        baseUrl = "http://localhost:" + port;

        try {
            Files.copy(sourceTestData, tempTestData, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Temp data file for test copied successfully");
        } catch (IOException e) {
            log.error("Error copying temp data file for test : " + e.getMessage());
            fail("Exception occurred during setup: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        // Delete the temporary testData file after test.
        Path tempTestData = Paths.get("src/test/resources/tempTestData.json");

        try {
            Files.delete(tempTestData);
            log.debug("Temp data file for test deleted successfully");
        } catch (IOException e) {
            log.error("Error deleting temp data file for test : " + e.getMessage());
            fail("Exception occurred during teardown: " + e.getMessage());
        }
    }


    @Test
    public void deletePerson_whenPersonExists_shouldReturnStatusOK() {
        String firstName = "John";
        String lastName = "Boyd";

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deletePerson_whenPersonDoesNotExistMatchByFirstName_shouldReturnStatusNotFound() {
        String firstName = "Unknown FirstName";
        String lastName = "Boyd";

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deletePerson_whenPersonDoesNotExistMatchByLastName_shouldReturnStatusNotFound() {
        String firstName = "John";
        String lastName = "Unknown LastName";

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void updatePerson_whenPersonExist_shouldReturnUpdatedPersonAndStatusOK() {
        Person personToUpdate = Person.builder().firstName("Jacob").lastName("Boyd").address("29 15th St")
                .city("Culvert").zip("97451").phone("841-874-6513").email("jaboyd@email.com").build();

        ResponseEntity<Person> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(personToUpdate), Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personToUpdate, response.getBody());
    }

    @Test
    public void updatePerson_whenPersonDoesNotExist_shouldReturnStatusNotFound() {
        Person personToUpdate = Person.builder().firstName("Jacob").lastName("Foster").address("1509 Culver St")
                .city("Culver").zip("97451").phone("841-874-6513").email("drk@email.com").build();

        ResponseEntity<Person> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(personToUpdate), Person.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addPerson_whenPersonExist_shouldReturnStatusConflict() {
        Person personToAdd = Person.builder().firstName("Jacob").lastName("Boyd").address("1509 Culver St")
                .city("Culver").zip("97451").phone("841-874-6513").email("drk@email.com").build();

        ResponseEntity<Person> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(personToAdd), Person.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addPerson_whenPersonDoesNotExistMatchByFirstName_shouldReturnAddedPersonAndStatusOK() {
        Person personToAdd = Person.builder().firstName("Jean").lastName("Boyd").address("29 15th St")
                .city("Culver").zip("97451").phone("841-874-6513").email("drk@email.com").build();

        ResponseEntity<Person> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(personToAdd), Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personToAdd, response.getBody());
    }

    @Test
    public void addPerson_whenPersonDoesNotExistMatchByLastName_shouldReturnAddedPersonAndStatusOK() {
        Person personToAdd = Person.builder().firstName("Jacob").lastName("DUPONT").address("29 15th St")
                .city("Culver").zip("97451").phone("841-874-6513").email("drk@email.com").build();

        ResponseEntity<Person> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(personToAdd), Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personToAdd, response.getBody());
    }

    @Test
    public void getPersons_whenPersonsExist_shouldReturnListOfPersonsAndStatusOK() {
        ResponseEntity<List<Person>> response = restTemplate.exchange(baseUrl + "/persons", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertAll("Persons",
                () -> assertEquals(24, response.getBody().size()),
                () -> {
                    Person person1 = response.getBody().get(0);
                    assertEquals("John", person1.getFirstName());
                    assertEquals("Boyd", person1.getLastName());
                    assertEquals("1509 Culver St", person1.getAddress());
                    assertEquals("Culver", person1.getCity());
                    assertEquals("97451", person1.getZip());
                    assertEquals("841-874-6512", person1.getPhone());
                    assertEquals("jaboyd@email.com", person1.getEmail());

                    Person person2 = response.getBody().get(2);
                    assertEquals("Jacob", person2.getFirstName());
                    assertEquals("Boyd", person2.getLastName());
                    assertEquals("1509 Culver St", person2.getAddress());
                    assertEquals("Culver", person2.getCity());
                    assertEquals("97451", person2.getZip());
                    assertEquals("841-874-6513", person2.getPhone());
                    assertEquals("drk@email.com", person2.getEmail());
                }
        );
    }
}