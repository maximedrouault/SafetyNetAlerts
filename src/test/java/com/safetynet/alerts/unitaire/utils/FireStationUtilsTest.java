package com.safetynet.alerts.unitaire.utils;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.utils.FireStationUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class FireStationUtilsTest {

    private static FireStationUtils fireStationUtils;
    private List<FireStation> fireStations;

    @BeforeAll
    public static void setUp() {
        fireStationUtils = new FireStationUtils();
    }

    @BeforeEach
    public void setUpPerTest() {
        fireStations = new ArrayList<>();

        FireStation fireStation1 = new FireStation();
        fireStation1.setAddress("1509 Culver St");
        fireStation1.setStation(3);

        FireStation fireStation2 = new FireStation();
        fireStation2.setAddress("29 15th St");
        fireStation2.setStation(2);

        fireStations.add(fireStation1);
        fireStations.add(fireStation2);
    }


    // getFireStationNumberByAddress
    @Test
    public void getFireStationNumberByAddressWithMatchingAddress() {
        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(fireStations, "1509 Culver St");

        assertTrue(fireStationNumber.isPresent());
        assertEquals(3, fireStationNumber.get());
    }

    @Test
    public void getFireStationNumberByAddressWhenAddressNotFound() {
        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(fireStations, "unknown address");

        assertTrue(fireStationNumber.isEmpty());
    }

    @Test
    public void getFireStationNumberByAddressWhenFireStationsListIsEmpty() {
        List<FireStation> emptyListOfFireStations = new ArrayList<>();

        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(emptyListOfFireStations, "1509 Culver St");

        assertTrue(fireStationNumber.isEmpty());
    }

    @Test
    public void getFireStationNumberByAddressWhenAddressIsEmpty() {
        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(fireStations, "");

        assertTrue(fireStationNumber.isEmpty());
    }


    // getAddressesCoveredByFireStations
    @Test
    public void getAddressesCoveredByFireStationsWithMatchingStationNumbers() {
        List<Integer> stationNumbers = new ArrayList<>();
        stationNumbers.add(3);
        stationNumbers.add(2);

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(fireStations, stationNumbers);

        assertEquals(2, coveredAddresses.size());
        assertEquals("1509 Culver St", coveredAddresses.get(0));
        assertEquals("29 15th St", coveredAddresses.get(1));
    }

    @Test
    public void getAddressesCoveredByFireStationsWhenStationNumbersNotFound() {
        List<Integer> stationNumbers = new ArrayList<>();
        stationNumbers.add(10);
        stationNumbers.add(11);

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(fireStations, stationNumbers);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationsWhenFireStationsListIsEmpty() {
        List<FireStation> emptyListOfFireStations = new ArrayList<>();
        List<Integer> stationNumbers = new ArrayList<>();
        stationNumbers.add(3);
        stationNumbers.add(2);

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(emptyListOfFireStations, stationNumbers);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationsWhenStationNumbersIsEmpty() {
        List<Integer> stationNumbers = new ArrayList<>();

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(fireStations, stationNumbers);

        assertTrue(coveredAddresses.isEmpty());
    }


    // getAddressesCoveredByFireStation
    @Test
    public void getAddressesCoveredByFireStationWithMatchingStationNumber() {
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(fireStations, 3);

        assertEquals(1, coveredAddresses.size());
        assertEquals("1509 Culver St", coveredAddresses.get(0));
    }

    @Test
    public void getAddressesCoveredByFireStationWhenStationNumberNotFound() {
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(fireStations, 10);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationWhenFireStationsListIsEmpty() {
        List<FireStation> emptyListOfFireStations = new ArrayList<>();

        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(emptyListOfFireStations, 3);

        assertTrue(coveredAddresses.isEmpty());
    }

    @Test
    public void getAddressesCoveredByFireStationWhenStationNumberIsNegative() {
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(fireStations, -1);

        assertTrue(coveredAddresses.isEmpty());
    }
}
