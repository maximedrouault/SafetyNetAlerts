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

        for (FireStation fireStation : fireStations) {
            if (fireStation.getAddress().equals(address) && fireStation.getStation() == station) {
                fireStations.remove(fireStation);

                log.info("Fire Station mapping for address '{}' and station '{}' successfully deleted", address, station);
                dataWriter.dataWrite(dataContainer);

                return ResponseEntity.ok().build();
            }
        }

        log.error("Fire Station mapping for address '{}' and station '{}' not found", address, station);
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<FireStation> updateFireStation(FireStation fireStationToUpdate) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();

        for (FireStation existingFireStation : fireStations) {
            if (existingFireStation.getAddress().equals(fireStationToUpdate.getAddress())) {

                existingFireStation.setStation(fireStationToUpdate.getStation());

                log.info("Fire station mapping successfully updated");
                dataWriter.dataWrite(dataContainer);

                return ResponseEntity.ok(existingFireStation);
            }
        }

        log.error("Not updated, Fire station not found");
        return ResponseEntity.notFound().build();
    }
}