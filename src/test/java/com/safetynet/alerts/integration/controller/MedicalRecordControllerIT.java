package com.safetynet.alerts.integration.controller;

import com.safetynet.alerts.model.MedicalRecord;
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
public class MedicalRecordControllerIT {

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

        endpoint = "/medicalRecord";
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
    public void deleteMedicalRecord_whenMedicalRecordExists_shouldReturnStatusOK() {
        String firstName = "John";
        String lastName = "Boyd";

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deleteMedicalRecord_whenMedicalRecordDoesNotExistMatchByFirstName_shouldReturnStatusNotFound() {
        String firstName = "Unknown FirstName";
        String lastName = "Boyd";

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deleteMedicalRecord_whenMedicalRecordDoesNotExistMatchByLastName_shouldReturnStatusNotFound() {
        String firstName = "John";
        String lastName = "Unknown LastName";

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void updateMedicalRecord_whenMedicalRecordExist_shouldReturnUpdatedMedicalRecordAndStatusOK() {
        MedicalRecord medicalRecordToUpdate = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .birthdate("01/01/1980")
                .medications(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"))
                .allergies(List.of())
                .build();

        ResponseEntity<MedicalRecord> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(medicalRecordToUpdate), MedicalRecord.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medicalRecordToUpdate, response.getBody());
    }

    @Test
    public void updateMedicalRecord_whenMedicalRecordDoesNotExist_shouldReturnStatusNotFound() {
        MedicalRecord medicalRecordToUpdate = MedicalRecord.builder()
                .firstName("Jacob")
                .lastName("Foster")
                .birthdate("03/06/1989")
                .medications(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"))
                .allergies(List.of())
                .build();

        ResponseEntity<MedicalRecord> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(medicalRecordToUpdate), MedicalRecord.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addMedicalRecord_whenMedicalRecordExist_shouldReturnStatusConflict() {
        MedicalRecord medicalRecordToAdd = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .birthdate("03/06/1989")
                .medications(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"))
                .allergies(List.of())
                .build();

        ResponseEntity<MedicalRecord> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(medicalRecordToAdd), MedicalRecord.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addMedicalRecord_whenMedicalRecordDoesNotExistMatchByFirstName_shouldReturnAddedMedicalRecordAndStatusOK() {
        MedicalRecord medicalRecordToAdd = MedicalRecord.builder()
                .firstName("John")
                .lastName("Foster")
                .birthdate("03/06/1989")
                .medications(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"))
                .allergies(List.of())
                .build();

        ResponseEntity<MedicalRecord> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(medicalRecordToAdd), MedicalRecord.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medicalRecordToAdd, response.getBody());
    }

    @Test
    public void addMedicalRecord_whenMedicalRecordDoesNotExistMatchByLastName_shouldReturnAddedMedicalRecordAndStatusOK() {
        MedicalRecord medicalRecordToAdd = MedicalRecord.builder()
                .firstName("Shepard")
                .lastName("Boyd")
                .birthdate("03/06/1989")
                .medications(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"))
                .allergies(List.of())
                .build();

        ResponseEntity<MedicalRecord> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(medicalRecordToAdd), MedicalRecord.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medicalRecordToAdd, response.getBody());
    }

    @Test
    public void getMedicalRecords_whenMedicalRecordsExist_shouldReturnListOfMedicalRecordsAndStatusOK() {
        ResponseEntity<List<MedicalRecord>> response = restTemplate.exchange(baseUrl + "/medicalRecords", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertAll("MedicalRecords",
                () -> assertEquals(23, response.getBody().size()),
                () -> {
                    MedicalRecord medicalRecord1 = response.getBody().get(0);
                    assertEquals("John", medicalRecord1.getFirstName());
                    assertEquals("Boyd", medicalRecord1.getLastName());
                    assertEquals("03/06/1984", medicalRecord1.getBirthdate());
                    assertEquals(List.of("aznol:350mg", "hydrapermazol:100mg"), medicalRecord1.getMedications());
                    assertEquals(List.of("nillacilan"), medicalRecord1.getAllergies());

                    MedicalRecord medicalRecord2 = response.getBody().get(1);
                    assertEquals("Jacob", medicalRecord2.getFirstName());
                    assertEquals("Boyd", medicalRecord2.getLastName());
                    assertEquals("03/06/1989", medicalRecord2.getBirthdate());
                    assertEquals(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), medicalRecord2.getMedications());
                    assertEquals(List.of(), medicalRecord2.getAllergies());
                }
        );
    }
}