package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FireStationService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    public ResponseEntity<List<FireStation>> getFireStations() throws Exception {
            List<FireStation> fireStations = dataReader.dataRead().getFirestations();

            if (fireStations.isEmpty()) {
                log.error("No Fire Stations found");
                return ResponseEntity.notFound().build();
            } else {
                log.info("Fire Stations successfully retrieved");
                return ResponseEntity.ok(fireStations);
            }
    }

    public ResponseEntity<Void> deleteFireStationMapping(String address, int station) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        for (FireStation existingFireStation : fireStations) {
            if (existingFireStation.getAddress().equals(address) && existingFireStation.getStation() == station) {

                fireStations.remove(existingFireStation);

                dataWriter.dataWrite(dataContainer);
                log.info("Fire Station mapping for address '{}' and station '{}' successfully deleted", address, station);

                return ResponseEntity.ok().build();
            }
        }

        log.error("Fire Station mapping for address '{}' and station '{}' not found", address, station);
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<FireStation> updateFireStationMapping(FireStation fireStationToUpdate) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        for (FireStation existingFireStation : fireStations) {
            if (existingFireStation.getAddress().equals(fireStationToUpdate.getAddress())) {

                existingFireStation.setStation(fireStationToUpdate.getStation());

                dataWriter.dataWrite(dataContainer);
                log.info("Fire station mapping successfully updated");

                return ResponseEntity.ok(existingFireStation);
            }
        }

        log.error("Not updated, Fire station not found");
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<FireStation> addFireStationMapping(FireStation fireStationToAdd) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        for (FireStation existingFireStation : fireStations) {
            if (existingFireStation.getStation() == fireStationToAdd.getStation() &&
            existingFireStation.getAddress().equals(fireStationToAdd.getAddress())) {

                log.error("Not added, Fire station mapping already exist");
                return ResponseEntity.badRequest().build();
            }
        }

        fireStations.add(fireStationToAdd);

        dataWriter.dataWrite(dataContainer);
        log.info("Fire station mapping successfully added");

        return ResponseEntity.ok(fireStationToAdd);
    }
}