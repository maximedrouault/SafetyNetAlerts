package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FireStationService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    // CRUD

    public boolean deleteFireStationMapping(FireStation fireStationToDelete) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        Optional<FireStation> foundFirestation = fireStations.stream()
                .filter(existingFireStation ->
                        existingFireStation.getAddress().equals(fireStationToDelete.getAddress()) &&
                        existingFireStation.getStation() == fireStationToDelete.getStation())
                .findFirst();

        if (foundFirestation.isPresent()) {
            fireStations.remove(foundFirestation.get());

            dataWriter.dataWrite(dataContainer);
            log.info("Fire Station mapping successfully deleted");

            return true;

        } else {
            log.error("Fire Station mapping not found");
            return false;
        }
    }

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
            log.info("Fire Station mapping successfully updated");

            return Optional.of(updatedFireStation);

        } else {
            log.error("Not updated, Fire station not found");
            return Optional.empty();
        }
    }

    public Optional<FireStation> addFireStationMapping(FireStation fireStationToAdd) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        Optional<FireStation> foundFireStation = fireStations.stream()
                .filter(existingFireStation ->
                        existingFireStation.getAddress().equals(fireStationToAdd.getAddress()) &&
                        existingFireStation.getStation() == fireStationToAdd.getStation())
                .findFirst();

        if (foundFireStation.isPresent()) {
            log.error("Not added, Fire station mapping already exist");
            return Optional.empty();

        } else {
            fireStations.add(fireStationToAdd);

            dataWriter.dataWrite(dataContainer);
            log.info("Fire station mapping successfully added");

            return Optional.of(fireStationToAdd);
        }
    }
}