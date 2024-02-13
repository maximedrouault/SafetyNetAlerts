package com.safetynet.alerts.unitaire.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.DataReader;
import com.safetynet.alerts.service.DataWriter;
import com.safetynet.alerts.service.FireStationService;
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
public class FireStationServiceTest {

    @Mock
    private DataReader dataReader;
    @Mock
    private DataWriter dataWriter;

    @InjectMocks
    private FireStationService fireStationService;


    @Test
    public void deleteFireStationMapping_WhenFireStationExists_ShouldReturnTrue() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean fireStationDeleted = fireStationService.deleteFireStationMapping(fireStations.get(0));

        assertTrue(fireStationDeleted);
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void deleteFireStationMapping_WhenFireStationAddressDoesNotMatch_ShouldReturnFalse() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        FireStation fireStationToDelete = FireStation.builder().address("29 15th St").station(3).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean fireStationDeleted = fireStationService.deleteFireStationMapping(fireStationToDelete);

        assertFalse(fireStationDeleted);
    }

    @Test
    public void deleteFireStationMapping_WhenFireStationNumberDoesNotMatch_ShouldReturnFalse() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        FireStation fireStationToDelete = FireStation.builder().address("1509 Culver St").station(2).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        boolean fireStationDeleted = fireStationService.deleteFireStationMapping(fireStationToDelete);

        assertFalse(fireStationDeleted);
    }

    @Test
    public void updateFireStationMapping_WhenFireStationExists_ShouldReturnUpdatedFireStation() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        FireStation fireStationToUpdate = FireStation.builder().address("1509 Culver St").station(2).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<FireStation> updatedFireStation = fireStationService.updateFireStationMapping(fireStationToUpdate);

        assertTrue(updatedFireStation.isPresent());
        assertEquals("1509 Culver St", updatedFireStation.get().getAddress());
        assertEquals(2, updatedFireStation.get().getStation());
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void updateFireStationMapping_WhenFireStationDoesNotExistOrNotFound_ShouldReturnEmptyOptional() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        FireStation fireStationToUpdate = FireStation.builder().address("29 15th St").station(2).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<FireStation> updatedFireStation = fireStationService.updateFireStationMapping(fireStationToUpdate);

        assertTrue(updatedFireStation.isEmpty());
    }

    @Test
    public void addFireStationMapping_WhenFireStationDoesNotExist_ShouldReturnAddedFireStation() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        FireStation fireStationToAdd = FireStation.builder().address("1509 Culver St").station(3).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<FireStation> addedFireStation = fireStationService.addFireStationMapping(fireStationToAdd);

        assertTrue(addedFireStation.isPresent());
        assertEquals("1509 Culver St", addedFireStation.get().getAddress());
        assertEquals(3, addedFireStation.get().getStation());
        verify(dataWriter, times(1)).dataWrite(any(DataContainer.class));
    }

    @Test
    public void addFireStationMapping_WhenFireStationAlreadyExists_ShouldReturnEmptyOptional() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        FireStation fireStationToAdd = FireStation.builder().address("1509 Culver St").station(3).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<FireStation> addedFireStation = fireStationService.addFireStationMapping(fireStationToAdd);

        assertTrue(addedFireStation.isEmpty());
    }

    @Test
    public void addFireStationMapping_WhenFireStationSameAddressesMatch_ShouldReturnEmptyOptional() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        fireStations.add(FireStation.builder().address("1509 Culver St").station(2).build());
        FireStation fireStationToAdd = FireStation.builder().address("1509 Culver St").station(2).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<FireStation> addedFireStation = fireStationService.addFireStationMapping(fireStationToAdd);

        assertTrue(addedFireStation.isEmpty());
    }

    @Test
    public void addFireStationMapping_WhenFireStationSameNumbersMatch_ShouldReturnEmptyOptional() throws Exception {
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(FireStation.builder().address("1509 Culver St").station(3).build());
        fireStations.add(FireStation.builder().address("29 15th St").station(3).build());
        FireStation fireStationToAdd = FireStation.builder().address("29 15th St").station(3).build();
        DataContainer dataContainer = DataContainer.builder().firestations(fireStations).build();

        when(dataReader.dataRead()).thenReturn(dataContainer);

        Optional<FireStation> addedFireStation = fireStationService.addFireStationMapping(fireStationToAdd);

        assertTrue(addedFireStation.isEmpty());
    }
}