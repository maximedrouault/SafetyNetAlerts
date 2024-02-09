package com.safetynet.alerts.unitaire.utils;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.utils.FireStationUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FireStationUtilsTest {

    private static FireStationUtils fireStationUtils;

    @BeforeAll
    public static void setUp() {
        fireStationUtils = new FireStationUtils();
    }


    private List<FireStation> getFireStationsForTest() {
        return List.of(
                FireStation.builder().address("1509 Culver St").station(3).build(),
                FireStation.builder().address("29 15th St").station(2).build()
        );
    }


    // getFireStationNumberByAddress
    @Test
    public void getFireStationNumberByAddressWithMatchingAddress() {
        List<FireStation> fireStations = getFireStationsForTest();

        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(fireStations, "1509 Culver St");

        assertTrue(fireStationNumber.isPresent());
        assertEquals(3, fireStationNumber.get());
    }

    @Test
    public void getFireStationNumberByAddressWhenAddressNotFound() {
        List<FireStation> fireStations = getFireStationsForTest();

        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(fireStations, "unknown address");

        assertTrue(fireStationNumber.isEmpty());
    }

    @Test
    public void getFireStationNumberByAddressWhenFireStationsListIsEmpty() {
        List<FireStation> emptyListOfFireStations = List.of();

        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(emptyListOfFireStations, "1509 Culver St");

        assertTrue(fireStationNumber.isEmpty());
    }

    @Test
    public void getFireStationNumberByAddressWhenAddressIsEmpty() {
        List<FireStation> fireStations = getFireStationsForTest();

        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(fireStations, "");

        assertTrue(fireStationNumber.isEmpty());
    }


    // getAddressesCoveredByFireStations
    @Test
    public void getAddressesCoveredByFireStationsWithMatchingStationNumbers() {
        List<FireStation> fireStations = getFireStationsForTest();
        List<Integer> stationNumbers = List.of(3, 2);

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(fireStations, stationNumbers);

        assertEquals(2, coveredAddresses.size());
        assertEquals("1509 Culver St", coveredAddresses.get(0));
        assertEquals("29 15th St", coveredAddresses.get(1));
    }

    @Test
    public void getAddressesCoveredByFireStationsWhenStationNumbersNotFound() {
        List<FireStation> fireStations = getFireStationsForTest();
        List<Integer> stationNumbers = List.of(10, 11);

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(fireStations, stationNumbers);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationsWhenFireStationsListIsEmpty() {
        List<FireStation> emptyListOfFireStations = List.of();
        List<Integer> stationNumbers = List.of(3, 2);

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(emptyListOfFireStations, stationNumbers);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationsWhenStationNumbersIsEmpty() {
        List<FireStation> fireStations = getFireStationsForTest();
        List<Integer> stationNumbers = List.of();

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(fireStations, stationNumbers);

        assertTrue(coveredAddresses.isEmpty());
    }


    // getAddressesCoveredByFireStation
    @Test
    public void getAddressesCoveredByFireStationWithMatchingStationNumber() {
        List<FireStation> fireStations = getFireStationsForTest();

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(fireStations, 3);

        assertEquals(1, coveredAddresses.size());
        assertEquals("1509 Culver St", coveredAddresses.get(0));
    }

    @Test
    public void getAddressesCoveredByFireStationWhenStationNumberNotFound() {
        List<FireStation> fireStations = getFireStationsForTest();

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(fireStations, 10);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationWhenFireStationsListIsEmpty() {
        List<FireStation> emptyListOfFireStations = List.of();

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(emptyListOfFireStations, 3);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationWhenStationNumberIsNegative() {
        List<FireStation> fireStations = getFireStationsForTest();

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(fireStations, -1);

        assertTrue(coveredAddresses.isEmpty());
    }
}
