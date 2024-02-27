package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing fire station data.
 * This class provides methods to perform CRUD (Create, Read, Update, Delete) operations on fire station mappings.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class FireStationService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    // CRUD

    /**
     * Get fireStations.
     *
     * @return An Optional containing the list of fireStations if found, or empty if not found.
     * @throws Exception If an error occurs while retrieving fireStations.
     */
    public Optional<List<FireStation>> getFireStations() throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        if (fireStations.isEmpty()) {
            log.error("FireStations not found");
            return Optional.empty();

        } else {
            log.info("FireStations successfully retrieved");
            return Optional.of(fireStations);
        }
    }

    /**
     * Deletes a fire station mapping.
     *
     * @param address The address covered by the FireStation to delete.
     * @param stationNumber The number of the FireStation to be deleted.
     * @return True if the mapping is successfully deleted, false otherwise.
     * @throws Exception If an error occurs while performing the delete operation.
     */
    public boolean deleteFireStationMapping(String address, int stationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        Optional<FireStation> foundFirestation = fireStations.stream()
                .filter(existingFireStation ->
                        existingFireStation.getAddress().equals(address) &&
                        existingFireStation.getStation() == stationNumber)
                .findFirst();

        if (foundFirestation.isPresent()) {
            FireStation deletedFireStation = foundFirestation.get();
            fireStations.remove(deletedFireStation);

            dataWriter.dataWrite(dataContainer);
            log.info("Fire Station mapping successfully deleted for fire station number: '{}' and address: '{}'.", deletedFireStation.getStation(), deletedFireStation.getAddress());

            return true;

        } else {
            log.error("Fire Station mapping not found for fire station number: '{}' and address: '{}'.", stationNumber, address);
            return false;
        }
    }

    /**
     * Updates a fire station mapping.
     *
     * @param fireStationToUpdate The FireStation object with updated information.
     * @return An Optional containing the updated FireStation if successful, or empty if not found.
     * @throws Exception If an error occurs while performing the update operation.
     */
    public Optional<FireStation> updateFireStationMapping(FireStation fireStationToUpdate) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        Optional<FireStation> foundFireStation = fireStations.stream()
                .filter(existingFireStation ->
                        existingFireStation.getAddress().equals(fireStationToUpdate.getAddress()))
                .findFirst();

        if (foundFireStation.isPresent()) {
            FireStation updatedFireStation = foundFireStation.get();

            updatedFireStation.setStation(fireStationToUpdate.getStation());

            dataWriter.dataWrite(dataContainer);
            log.info("Fire Station mapping successfully updated for fire station: '{}' and address: '{}'.", updatedFireStation.getStation(), updatedFireStation.getAddress());

            return Optional.of(updatedFireStation);

        } else {
            log.error("Not updated, Fire station not found for fire station number: '{}' and address: '{}'.", fireStationToUpdate.getStation(), fireStationToUpdate.getAddress());
            return Optional.empty();
        }
    }

    /**
     * Adds a new fire station mapping.
     *
     * @param fireStationToAdd The FireStation object to be added.
     * @return An Optional containing the added FireStation if successful, or empty if it already exists.
     * @throws Exception If an error occurs while performing the add operation.
     */
    public Optional<FireStation> addFireStationMapping(FireStation fireStationToAdd) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        Optional<FireStation> foundFireStation = fireStations.stream()
                .filter(existingFireStation ->
                        existingFireStation.getAddress().equals(fireStationToAdd.getAddress()) &&
                        existingFireStation.getStation() == fireStationToAdd.getStation())
                .findFirst();

        if (foundFireStation.isPresent()) {
            log.error("Not added, Fire station mapping already exist for fire station number: '{}' and address: '{}'.", fireStationToAdd.getStation(), fireStationToAdd.getAddress());
            return Optional.empty();

        } else {
            fireStations.add(fireStationToAdd);

            dataWriter.dataWrite(dataContainer);
            log.info("Fire station mapping successfully added for fire station number: '{}' and address: '{}'.", fireStationToAdd.getStation(), fireStationToAdd.getAddress());

            return Optional.of(fireStationToAdd);
        }
    }
}