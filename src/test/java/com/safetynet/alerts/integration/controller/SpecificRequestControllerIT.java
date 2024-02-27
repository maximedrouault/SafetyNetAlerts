package com.safetynet.alerts.integration.controller;

import com.safetynet.alerts.dto.*;
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
public class SpecificRequestControllerIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // Create a temporary data file. Copy "testData.json" to "tempTestData.json" for tests.
        Path sourceTestData = Paths.get("src/test/resources/testData.json");
        Path tempTestData = Paths.get("src/test/resources/tempTestData.json");

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


    // ChildAlert
    @Test
    public void getChildAlert_whenPersonChildAlertDTOSExist_shouldReturnStatusOK() {
        String endpoint = "/childAlert";
        String address = "1509 Culver St";

        ResponseEntity<List<PersonChildAlertDTO>> response = restTemplate.exchange(baseUrl + endpoint + "?address=" + address, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertAll("PersonChildAlertDTOS",
                () -> assertEquals(2, response.getBody().size()),
                () -> {
                    PersonChildAlertDTO child1 = response.getBody().get(0);
                    assertEquals(4, child1.getFamilyMembers().size());
                    assertEquals("Roger", child1.getFirstName());
                    assertEquals("Boyd", child1.getLastName());
                    assertTrue(child1.getAge() > 0);

                    Person familyMember1OfChild1 = child1.getFamilyMembers().get(0);
                    assertEquals("John", familyMember1OfChild1.getFirstName());
                    assertEquals("Boyd", familyMember1OfChild1.getLastName());
                    assertEquals("1509 Culver St", familyMember1OfChild1.getAddress());
                    assertEquals("Culver", familyMember1OfChild1.getCity());
                    assertEquals("97451", familyMember1OfChild1.getZip());
                    assertEquals("841-874-6512", familyMember1OfChild1.getPhone());
                    assertEquals("jaboyd@email.com", familyMember1OfChild1.getEmail());

                    Person familyMembers2OfChild1 = child1.getFamilyMembers().get(1);
                    assertEquals("Jacob", familyMembers2OfChild1.getFirstName());
                    assertEquals("Boyd", familyMembers2OfChild1.getLastName());

                    Person familyMembers3OfChild1 = child1.getFamilyMembers().get(2);
                    assertEquals("Tenley", familyMembers3OfChild1.getFirstName());
                    assertEquals("Boyd", familyMembers3OfChild1.getLastName());

                    Person familyMembers4OfChild1 = child1.getFamilyMembers().get(3);
                    assertEquals("Felicia", familyMembers4OfChild1.getFirstName());
                    assertEquals("Boyd", familyMembers4OfChild1.getLastName());

                    PersonChildAlertDTO child2 = response.getBody().get(1);
                    assertEquals(4, child2.getFamilyMembers().size());
                    assertEquals("Tenley", child2.getFirstName());
                    assertEquals("Boyd", child2.getLastName());
                    assertTrue(child2.getAge() > 0);

                    Person familyMember1OfChild2 = child2.getFamilyMembers().get(0);
                    assertEquals("John", familyMember1OfChild2.getFirstName());
                    assertEquals("Boyd", familyMember1OfChild2.getLastName());
                    assertEquals("1509 Culver St", familyMember1OfChild2.getAddress());
                    assertEquals("Culver", familyMember1OfChild2.getCity());
                    assertEquals("97451", familyMember1OfChild2.getZip());
                    assertEquals("841-874-6512", familyMember1OfChild2.getPhone());
                    assertEquals("jaboyd@email.com", familyMember1OfChild2.getEmail());

                    Person familyMembers2OfChild2 = child2.getFamilyMembers().get(1);
                    assertEquals("Jacob", familyMembers2OfChild2.getFirstName());
                    assertEquals("Boyd", familyMembers2OfChild2.getLastName());

                    Person familyMembers3OfChild2 = child2.getFamilyMembers().get(2);
                    assertEquals("Roger", familyMembers3OfChild2.getFirstName());
                    assertEquals("Boyd", familyMembers3OfChild2.getLastName());

                    Person familyMembers4OfChild2 = child2.getFamilyMembers().get(3);
                    assertEquals("Felicia", familyMembers4OfChild2.getFirstName());
                    assertEquals("Boyd", familyMembers4OfChild2.getLastName());
                }
        );
    }

    @Test
    public void getChildAlert_whenPersonChildAlertDTOSDoesNotExist_shouldReturnStatusNotFound() {
        String endpoint = "/childAlert";
        String address = "21 Jump Street";

        ResponseEntity<List<PersonChildAlertDTO>> response = restTemplate.exchange(baseUrl + endpoint + "?address=" + address, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    // FireAddressInfo
    @Test
    public void getFireAddressInfo_whenFireAddressInfoResponseDTOExists_shouldReturnStatusOK() {
        String endpoint = "/fire";
        String address = "1509 Culver St";

        ResponseEntity<FireAddressInfoResponseDTO> response = restTemplate.exchange(baseUrl + endpoint + "?address=" + address, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getStationNumber());
        assertEquals(5, response.getBody().getPersons().size());
        assertAll("FireAddressInfoResponseDTO", () -> {
                    PersonFireAddressInfoDTO personFireAddressInfoDTO1 = response.getBody().getPersons().get(0);
                    assertEquals("Boyd", personFireAddressInfoDTO1.getLastName());
                    assertEquals("841-874-6512", personFireAddressInfoDTO1.getPhone());
                    assertTrue(personFireAddressInfoDTO1.getAge() > 0);
                    assertEquals(List.of("aznol:350mg", "hydrapermazol:100mg"), personFireAddressInfoDTO1.getMedications());
                    assertEquals(List.of("nillacilan"), personFireAddressInfoDTO1.getAllergies());

                    PersonFireAddressInfoDTO personFireAddressInfoDTO2 = response.getBody().getPersons().get(1);
                    assertEquals("Boyd", personFireAddressInfoDTO2.getLastName());
                    assertEquals("841-874-6513", personFireAddressInfoDTO2.getPhone());
                    assertTrue(personFireAddressInfoDTO2.getAge() > 0);
                    assertEquals(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), personFireAddressInfoDTO2.getMedications());
                    assertEquals(List.of(), personFireAddressInfoDTO2.getAllergies());

                    PersonFireAddressInfoDTO personFireAddressInfoDTO3 = response.getBody().getPersons().get(2);
                    assertEquals("Boyd", personFireAddressInfoDTO3.getLastName());
                    assertEquals("841-874-6512", personFireAddressInfoDTO3.getPhone());
                    assertTrue(personFireAddressInfoDTO3.getAge() > 0);
                    assertEquals(List.of(), personFireAddressInfoDTO3.getMedications());
                    assertEquals(List.of("peanut"), personFireAddressInfoDTO3.getAllergies());

                    PersonFireAddressInfoDTO personFireAddressInfoDTO4 = response.getBody().getPersons().get(3);
                    assertEquals("Boyd", personFireAddressInfoDTO4.getLastName());
                    assertEquals("841-874-6512", personFireAddressInfoDTO4.getPhone());
                    assertTrue(personFireAddressInfoDTO4.getAge() > 0);
                    assertEquals(List.of(), personFireAddressInfoDTO4.getMedications());
                    assertEquals(List.of(), personFireAddressInfoDTO4.getAllergies());

                    PersonFireAddressInfoDTO personFireAddressInfoDTO5 = response.getBody().getPersons().get(4);
                    assertEquals("Boyd", personFireAddressInfoDTO5.getLastName());
                    assertEquals("841-874-6544", personFireAddressInfoDTO5.getPhone());
                    assertTrue(personFireAddressInfoDTO5.getAge() > 0);
                    assertEquals(List.of("tetracyclaz:650mg"), personFireAddressInfoDTO5.getMedications());
                    assertEquals(List.of("xilliathal"), personFireAddressInfoDTO5.getAllergies());
                }
        );
    }

    @Test
    public void getFireAddressInfo_whenFireAddressInfoResponseDTODoesNotExist_shouldReturnStatusNotFound() {
        String endpoint = "/fire";
        String address = "21 Jump Street";

        ResponseEntity<List<PersonChildAlertDTO>> response = restTemplate.exchange(baseUrl + endpoint + "?address=" + address, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    // FireStationCoverage
    @Test
    public void getFireStationCoverage_whenFireStationCoverageResponseDTOExists_shouldReturnStatusOK() {
        String endpoint = "/firestation";
        int stationNumber = 1;

        ResponseEntity<FireStationCoverageResponseDTO> response = restTemplate.exchange(baseUrl + endpoint + "?stationNumber=" + stationNumber, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(7, response.getBody().getPersons().size());
        assertEquals(6, response.getBody().getAdultsCount());
        assertEquals(1, response.getBody().getChildrenCount());
        assertAll("FireStationCoverageResponseDTO", () -> {
                    PersonFireStationCoverageDTO personFireStationCoverageDTO1 = response.getBody().getPersons().get(0);
                    assertEquals("John", personFireStationCoverageDTO1.getFirstName());
                    assertEquals("Boyd", personFireStationCoverageDTO1.getLastName());
                    assertEquals("644 Gershwin Cir", personFireStationCoverageDTO1.getAddress());
                    assertEquals("841-874-6512", personFireStationCoverageDTO1.getPhone());

                    PersonFireStationCoverageDTO personFireStationCoverageDTO2 = response.getBody().getPersons().get(1);
                    assertEquals("Peter", personFireStationCoverageDTO2.getFirstName());
                    assertEquals("Duncan", personFireStationCoverageDTO2.getLastName());
                    assertEquals("644 Gershwin Cir", personFireStationCoverageDTO2.getAddress());
                    assertEquals("841-874-6512", personFireStationCoverageDTO2.getPhone());

                    PersonFireStationCoverageDTO personFireStationCoverageDTO3 = response.getBody().getPersons().get(2);
                    assertEquals("Reginold", personFireStationCoverageDTO3.getFirstName());
                    assertEquals("Walker", personFireStationCoverageDTO3.getLastName());
                    assertEquals("908 73rd St", personFireStationCoverageDTO3.getAddress());
                    assertEquals("841-874-8547", personFireStationCoverageDTO3.getPhone());

                    PersonFireStationCoverageDTO personFireStationCoverageDTO4 = response.getBody().getPersons().get(3);
                    assertEquals("Jamie", personFireStationCoverageDTO4.getFirstName());
                    assertEquals("Peters", personFireStationCoverageDTO4.getLastName());
                    assertEquals("908 73rd St", personFireStationCoverageDTO4.getAddress());
                    assertEquals("841-874-7462", personFireStationCoverageDTO4.getPhone());

                    PersonFireStationCoverageDTO personFireStationCoverageDTO5 = response.getBody().getPersons().get(4);
                    assertEquals("Brian", personFireStationCoverageDTO5.getFirstName());
                    assertEquals("Stelzer", personFireStationCoverageDTO5.getLastName());
                    assertEquals("947 E. Rose Dr", personFireStationCoverageDTO5.getAddress());
                    assertEquals("841-874-7784", personFireStationCoverageDTO5.getPhone());

                    PersonFireStationCoverageDTO personFireStationCoverageDTO6 = response.getBody().getPersons().get(5);
                    assertEquals("Shawna", personFireStationCoverageDTO6.getFirstName());
                    assertEquals("Stelzer", personFireStationCoverageDTO6.getLastName());
                    assertEquals("947 E. Rose Dr", personFireStationCoverageDTO6.getAddress());
                    assertEquals("841-874-7784", personFireStationCoverageDTO6.getPhone());

                    PersonFireStationCoverageDTO personFireStationCoverageDTO7 = response.getBody().getPersons().get(6);
                    assertEquals("Kendrik", personFireStationCoverageDTO7.getFirstName());
                    assertEquals("Stelzer", personFireStationCoverageDTO7.getLastName());
                    assertEquals("947 E. Rose Dr", personFireStationCoverageDTO7.getAddress());
                    assertEquals("841-874-7784", personFireStationCoverageDTO7.getPhone());
                }
        );
    }

    @Test
    public void getFireStationCoverage_whenFireStationCoverageResponseDTODoesNotExist_shouldReturnStatusNotFound() {
        String endpoint = "/firestation";
        int stationNumber = 0;

        ResponseEntity<FireStationCoverageResponseDTO> response = restTemplate.exchange(baseUrl + endpoint + "?stationNumber=" + stationNumber, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    // PhoneAlert
    @Test
    public void getPhoneAlert_whenPersonPhoneAlertDTOSExist_shouldReturnStatusOK() {
        String endpoint = "/phoneAlert";
        int stationNumber = 2;

        ResponseEntity<List<PersonPhoneAlertDTO>> response = restTemplate.exchange(baseUrl + endpoint + "?firestation=" + stationNumber, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
        assertAll("PersonPhoneAlertDTO", () -> {
                    assertEquals("841-874-6513", response.getBody().get(0).getPhone());
                    assertEquals("841-874-7878", response.getBody().get(1).getPhone());
                    assertEquals("841-874-7512", response.getBody().get(2).getPhone());
                    assertEquals("841-874-7512", response.getBody().get(3).getPhone());
                    assertEquals("841-874-7458", response.getBody().get(4).getPhone());
                }
        );
    }

    @Test
    public void getPhoneAlert_whenPersonPhoneAlertDTOSDoNotExist_shouldReturnStatusNotFound() {
        String endpoint = "/phoneAlert";
        int stationNumber = 0;

        ResponseEntity<List<PersonPhoneAlertDTO>> response = restTemplate.exchange(baseUrl + endpoint + "?firestation=" + stationNumber, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    // CommunityEmail
    @Test
    public void getCommunityEmail_whenPersonCommunityEmailDTOSExist_shouldReturnStatusOK() {
        String endpoint = "/communityEmail";
        String city = "Culver";

        ResponseEntity<List<PersonCommunityEmailDTO>> response = restTemplate.exchange(baseUrl + endpoint + "?city=" + city, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(24, response.getBody().size());
        assertAll("PersonCommunityEmailDTO", () -> {
            assertEquals("jaboyd@email.com", response.getBody().get(0).getEmail());
            assertEquals("jaboyd@email.com", response.getBody().get(1).getEmail());
            assertEquals("drk@email.com", response.getBody().get(2).getEmail());
            assertEquals("tenz@email.com", response.getBody().get(3).getEmail());
            assertEquals("jaboyd@email.com", response.getBody().get(4).getEmail());
            assertEquals("jaboyd@email.com", response.getBody().get(5).getEmail());
            assertEquals("drk@email.com", response.getBody().get(6).getEmail());
            assertEquals("tenz@email.com", response.getBody().get(7).getEmail());
            assertEquals("jaboyd@email.com", response.getBody().get(8).getEmail());
            assertEquals("jaboyd@email.com", response.getBody().get(9).getEmail());
            assertEquals("tcoop@ymail.com", response.getBody().get(10).getEmail());
            assertEquals("lily@email.com", response.getBody().get(11).getEmail());
            assertEquals("soph@email.com", response.getBody().get(12).getEmail());
            assertEquals("ward@email.com", response.getBody().get(13).getEmail());
            assertEquals("zarc@email.com", response.getBody().get(14).getEmail());
            assertEquals("reg@email.com", response.getBody().get(15).getEmail());
            assertEquals("jpeter@email.com", response.getBody().get(16).getEmail());
            assertEquals("jpeter@email.com", response.getBody().get(17).getEmail());
            assertEquals("aly@imail.com", response.getBody().get(18).getEmail());
            assertEquals("bstel@email.com", response.getBody().get(19).getEmail());
            assertEquals("ssanw@email.com", response.getBody().get(20).getEmail());
            assertEquals("bstel@email.com", response.getBody().get(21).getEmail());
            assertEquals("clivfd@ymail.com", response.getBody().get(22).getEmail());
            assertEquals("gramps@email.com", response.getBody().get(23).getEmail());
        });
    }

    @Test
    public void getCommunityEmail_whenPersonCommunityEmailDTOSDoNotExist_shouldReturnStatusNotFound() {
        String endpoint = "/communityEmail";
        String city = "San Francisco";

        ResponseEntity<PersonCommunityEmailDTO> response = restTemplate.exchange(baseUrl + endpoint + "?city=" + city, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    // PersonInfo
    @Test
    public void getPersonInfo_whenPersonInfoDTOSExist_shouldReturnStatusOK() {
        String endpoint = "/personInfo";
        String firstName = "Jacob";
        String lastName = "Boyd";

        ResponseEntity<List<PersonInfoDTO>> response = restTemplate.exchange(
                baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertAll("PersonInfoDTO", () -> {
            PersonInfoDTO personInfoDTO = response.getBody().get(0);
            assertEquals("Boyd", personInfoDTO.getLastName());
            assertEquals("1509 Culver St", personInfoDTO.getAddress());
            assertEquals(34, personInfoDTO.getAge());

            assertEquals("drk@email.com", personInfoDTO.getEmail());
            assertEquals(List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), personInfoDTO.getMedications());
            assertEquals(List.of(), personInfoDTO.getAllergies());
        });
    }

    @Test
    public void getPersonInfo_whenPersonInfoDTOSDoNotExistMatchByFirstName_shouldReturnStatusNotFound() {
        String endpoint = "/personInfo";
        String firstName = "Dark";
        String lastName = "Vador";

        ResponseEntity<List<PersonInfoDTO>> response = restTemplate.exchange(
                baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void getPersonInfo_whenPersonInfoDTOSDoNotExistMatchByLastName_shouldReturnStatusNotFound() {
        String endpoint = "/personInfo";
        String firstName = "John";
        String lastName = "Vador";

        ResponseEntity<List<PersonInfoDTO>> response = restTemplate.exchange(
                baseUrl + endpoint + "?firstName=" + firstName + "&lastName=" + lastName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    // FloodStationCoverage
    @Test
    public void getFloodStationCoverage_whenFloodStationCoverageResponseDTOSExist_shouldReturnStatusOK() {
        String endpoint = "/flood/stations";
        String stationsNumbers = "1, 3";

        ResponseEntity<List<FloodStationCoverageResponseDTO>> response = restTemplate.exchange(
                baseUrl + endpoint + "?stations=" + stationsNumbers,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(7, response.getBody().size());
        assertAll("FloodStationCoverageResponseDTO", () -> {
            FloodStationCoverageResponseDTO floodStationCoverageResponseDTO1 = response.getBody().get(0);
            assertEquals("748 Townings Dr", floodStationCoverageResponseDTO1.getAddress());
            assertEquals(2, floodStationCoverageResponseDTO1.getPersons().size());

                PersonFloodStationCoverageDTO personFloodStationCoverageDTO11 = floodStationCoverageResponseDTO1.getPersons().get(0);
                assertEquals("Shepard", personFloodStationCoverageDTO11.getLastName());
                assertEquals("841-874-6544", personFloodStationCoverageDTO11.getPhone());
                assertTrue(personFloodStationCoverageDTO11.getAge() > 0);
                assertEquals(List.of(), personFloodStationCoverageDTO11.getMedications());
                assertEquals(List.of(), personFloodStationCoverageDTO11.getAllergies());

                PersonFloodStationCoverageDTO personFloodStationCoverageDTO12 = floodStationCoverageResponseDTO1.getPersons().get(1);
                assertEquals("Ferguson", personFloodStationCoverageDTO12.getLastName());
                assertEquals("841-874-6741", personFloodStationCoverageDTO12.getPhone());
                assertTrue(personFloodStationCoverageDTO12.getAge() > 0);
                assertEquals(List.of(), personFloodStationCoverageDTO12.getMedications());
                assertEquals(List.of(), personFloodStationCoverageDTO12.getAllergies());

            FloodStationCoverageResponseDTO floodStationCoverageResponseDTO2 = response.getBody().get(1);
            assertEquals("947 E. Rose Dr", floodStationCoverageResponseDTO2.getAddress());
            assertEquals(3, floodStationCoverageResponseDTO2.getPersons().size());

                PersonFloodStationCoverageDTO personFloodStationCoverageDTO21 = floodStationCoverageResponseDTO2.getPersons().get(0);
                assertEquals("Stelzer", personFloodStationCoverageDTO21.getLastName());
                assertEquals("841-874-7784", personFloodStationCoverageDTO21.getPhone());
                assertTrue(personFloodStationCoverageDTO21.getAge() > 0);
                assertEquals(List.of("ibupurin:200mg", "hydrapermazol:400mg"), personFloodStationCoverageDTO21.getMedications());
                assertEquals(List.of("nillacilan"), personFloodStationCoverageDTO21.getAllergies());

                PersonFloodStationCoverageDTO personFloodStationCoverageDTO22 = floodStationCoverageResponseDTO2.getPersons().get(1);
                assertEquals("Stelzer", personFloodStationCoverageDTO22.getLastName());
                assertEquals("841-874-7784", personFloodStationCoverageDTO22.getPhone());
                assertTrue(personFloodStationCoverageDTO22.getAge() > 0);
                assertEquals(List.of(), personFloodStationCoverageDTO22.getMedications());
                assertEquals(List.of(), personFloodStationCoverageDTO22.getAllergies());

                PersonFloodStationCoverageDTO personFloodStationCoverageDTO23 = floodStationCoverageResponseDTO2.getPersons().get(2);
                assertEquals("Stelzer", personFloodStationCoverageDTO23.getLastName());
                assertEquals("841-874-7784", personFloodStationCoverageDTO23.getPhone());
                assertTrue(personFloodStationCoverageDTO23.getAge() > 0);
                assertEquals(List.of("noxidian:100mg", "pharmacol:2500mg"), personFloodStationCoverageDTO23.getMedications());
                assertEquals(List.of(), personFloodStationCoverageDTO23.getAllergies());

            assertEquals("908 73rd St", response.getBody().get(2).getAddress());
            assertEquals(2, response.getBody().get(2).getPersons().size());
            assertEquals("644 Gershwin Cir", response.getBody().get(3).getAddress());
            assertEquals(2, response.getBody().get(3).getPersons().size());
            assertEquals("1509 Culver St", response.getBody().get(4).getAddress());
            assertEquals(5, response.getBody().get(4).getPersons().size());
            assertEquals("112 Steppes Pl", response.getBody().get(5).getAddress());
            assertEquals(3, response.getBody().get(5).getPersons().size());
            assertEquals("834 Binoc Ave", response.getBody().get(6).getAddress());
            assertEquals(1, response.getBody().get(6).getPersons().size());

        });
    }

    @Test
    public void getFloodStationCoverage_whenFloodStationCoverageResponseDTOSDoNotExist_shouldReturnStatusNotFound() {
        String endpoint = "/flood/stations";
        String stationsNumbers = "0, 10";

        ResponseEntity<List<FloodStationCoverageResponseDTO>> response = restTemplate.exchange(
                baseUrl + endpoint + "?stations=" + stationsNumbers,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}