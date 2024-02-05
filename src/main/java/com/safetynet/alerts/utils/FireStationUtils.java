package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FireStationUtils {

    public Optional<Integer> getFireStationNumberByAddress(List<FireStation> fireStations, String address) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .map(FireStation::getStation)
                .findFirst();
    }

    public List<String> getAddressesCoveredByFireStations(List<FireStation> fireStations, List<Integer> stationNumbers) {
        return fireStations.stream()
                .filter(fireStation -> stationNumbers.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .toList();
    }

    public List<String> getAddressesCoveredByFireStation(List<FireStation> fireStations, int stationNumber) {
        return fireStations.stream()
                .filter(fireStation -> stationNumber == fireStation.getStation())
                .map(FireStation::getAddress)
                .toList();
    }
}