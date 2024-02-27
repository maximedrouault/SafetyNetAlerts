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
    public void deleteFireStation_whenFireStationExists_shouldReturnStatusOK() {
        String address = "1509 Culver St";
        int stationNumber = 3;

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?address=" + address + "&stationNumber=" + stationNumber, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deleteFireStation_whenFireStationDoesNotExistMatchByAddress_shouldReturnStatusNotFound() {
        String address = "Unknown";
        int stationNumber = 3;

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?address=" + address + "&stationNumber=" + stationNumber, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void deleteFireStation_whenFireStationDoesNotExistMatchByStationNumber_shouldReturnStatusNotFound() {
        String address = "1509 Culver St";
        int stationNumber = 10;

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + endpoint + "?address=" + address + "&stationNumber=" + stationNumber, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void updateFireStation_whenFireStationExist_shouldReturnUpdatedFireStationAndStatusOK() {
        FireStation fireStationToUpdate = FireStation.builder().address("1509 Culver St").station(2).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(fireStationToUpdate), FireStation.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fireStationToUpdate, response.getBody());
    }

    @Test
    public void updateFireStation_whenFireStationDoesNotExist_shouldReturnStatusNotFound() {
        FireStation fireStationToUpdate = FireStation.builder().address("Unknown address").station(3).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, new HttpEntity<>(fireStationToUpdate), FireStation.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addFireStation_whenFireStationExist_shouldReturnStatusConflict() {
        FireStation fireStationToAdd = FireStation.builder().address("1509 Culver St").station(3).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(fireStationToAdd), FireStation.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addFireStation_whenFireStationDoesNotExistMatchByAddress_shouldReturnAddedFireStationAndStatusOK() {
        FireStation fireStationToAdd = FireStation.builder().address("21 Jump Street").station(1).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(fireStationToAdd), FireStation.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fireStationToAdd, response.getBody());
    }

    @Test
    public void addFireStation_whenFireStationDoesNotExistMatchByStationNumber_shouldReturnAddedFireStationAndStatusOK() {
        FireStation fireStationToAdd = FireStation.builder().address("1509 Culver St").station(5).build();

        ResponseEntity<FireStation> response = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, new HttpEntity<>(fireStationToAdd), FireStation.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fireStationToAdd, response.getBody());
    }

    @Test
    public void getFireStations_whenFireStationsExist_shouldReturnListOfFireStationsAndStatusOK() {
        ResponseEntity<List<FireStation>> response = restTemplate.exchange(baseUrl + "/firestations", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertAll("FireStations",
                () -> assertEquals(13, response.getBody().size()),
                () -> {
                    FireStation fireStation1 = response.getBody().get(0);
                    assertEquals("1509 Culver St", fireStation1.getAddress());
                    assertEquals(3, fireStation1.getStation());

                    FireStation fireStation2 = response.getBody().get(1);
                    assertEquals("29 15th St", fireStation2.getAddress());
                    assertEquals(2, fireStation2.getStation());

                    FireStation fireStation3 = response.getBody().get(2);
                    assertEquals("834 Binoc Ave", fireStation3.getAddress());
                    assertEquals(3, fireStation3.getStation());

                    FireStation fireStation4 = response.getBody().get(3);
                    assertEquals("644 Gershwin Cir", fireStation4.getAddress());
                    assertEquals(1, fireStation4.getStation());
                }
        );
    }
}