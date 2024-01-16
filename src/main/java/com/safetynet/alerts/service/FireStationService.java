package com.safetynet.alerts.service;

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
}