package com.safetynet.alerts.integration.controller;

import com.safetynet.alerts.model.FireStation;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Log4j2
public class FireStationControllerIT {

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

        endpoint = "/firestation";
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
    public void deleteFireStation_whenFireStationExist_shouldReturnStatusOK() {
        FireStation FireStationToDelete = FireStation.builder().address("1509 Culver St").station(3).build();

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.DELETE, new HttpEntity<>(FireStationToDelete), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deleteFireStation_whenFireStationDoesNotExist_shouldReturnStatusNotFound() {
        FireStation FireStationToDelete = FireStation.builder().address("29 15th St").station(3).build();

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.DELETE, new HttpEntity<>(FireStationToDelete), Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void updateFireStation_whenFireStationExist_shouldReturnUpdatedFireStationAndStatusOK() {
        FireStation FireStationToUpdate = FireStation.builder().address("1509 Culver St").station(2).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(FireStationToUpdate), FireStation.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FireStationToUpdate, response.getBody());
    }

    @Test
    public void updateFireStation_whenFireStationDoesNotExist_shouldReturnStatusNotFound() {
        FireStation FireStationToDelete = FireStation.builder().address("Unknown address").station(3).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(FireStationToDelete), FireStation.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addFireStation_whenFireStationExist_shouldReturnStatusConflict() {
        FireStation FireStationToAdd = FireStation.builder().address("1509 Culver St").station(3).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(FireStationToAdd), FireStation.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addFireStation_whenFireStationDoesNotExist_shouldReturnAddedFireStationAndStatusOK() {
        FireStation FireStationToAdd = FireStation.builder().address("21 Jump Street").station(1).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(FireStationToAdd), FireStation.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FireStationToAdd, response.getBody());
    }
}